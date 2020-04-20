package j8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solab.iso8583.parse.ConfigParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyt
 * @program socketcommunication
 * @description 一个简单的8583报文组装和解析
 * @create 2020-04-20 23:27
 **/
public class Client implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static MessageFactory<IsoMessage> mfact;
    private Socket sock;

    private static ConcurrentHashMap<String, IsoMessage> pending = new ConcurrentHashMap<String, IsoMessage>();

    public Client(Socket socket) {
        sock = socket;
    }

    /**
     * 进程监听，自动读取返回响应报文
     * 线程回调函数
     */
    public void run() {
        byte[] lenbuf = new byte[2];
        try {
            //For high volume apps you will be better off only reading the stream in one thread
            //and then using another thread to parse the buffers and process the responses
            //Otherwise the network buffer might fill up and you can miss a message.
            while (sock != null && sock.isConnected()) {
                // 读取网络传输的字节流
                sock.getInputStream().read(lenbuf);
                // 获取报文长度--前两个字节
                int size = ((lenbuf[0] & 0xff) << 8) | (lenbuf[1] & 0xff);
                byte[] buf = new byte[size];
                //We're not expecting ETX in this case
                if (sock.getInputStream().read(buf) == size) {
                    try {
                        //We'll use this header length as a reference. 我们将会使用此标头长度作为参考
                        //In practice, ISO headers for any message type are the same length. 事实上，任何消息类型的ISO标头都具有相同长度
                        String respHeader = mfact.getIsoHeader(0x200);
                        System.out.println("响应报文头: " + respHeader);
                        IsoMessage resp = mfact.parseMessage(buf, respHeader == null ? 12 : respHeader.length());
                        System.out.println("读取响应报文：" + resp.debugString());
                        log.debug("读取响应报文 {} conf {}: {}", new Object[]{
                                resp.getField(11), resp.getField(38), new String(buf)});
                        pending.remove(resp.getField(11).toString());
                        // 尝试解析48域信息并打印出来
                        IsoValue<?> f48 = resp.getField(48);
                        if (f48 != null && f48.getValue() instanceof ProductData) {
                            ProductData v = (ProductData)f48.getValue();
                            log.debug("Field 48 encoded: '{}' pid:{} cat:{}", new Object[]{
                                    f48, v.getProductId(), v.getCategoryId()});
                            //System.out.println("解析响应报文 : " + resp.debugString());
                        }
                    } catch (ParseException ex) {
                        log.error("Parsing response", ex);
                    }
                } else {
                    pending.clear();
                    return;
                }
            }
        } catch (IOException ex) {
            if (done) {
                log.info("Socket closed because we're done ({} pending)", pending.size());
            } else {
                log.error(String.format("Reading responses, %d pending", pending.size()), ex);
            }
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException ex) {};
            }
        }

    }

    private static final BigDecimal[] amounts = new BigDecimal[]{
            new BigDecimal("10"), new BigDecimal("20.50"), new BigDecimal("37.44")
    };

    private static final String[] data = new String[]{
            "1234567890", "5432198765", "1020304050", "abcdefghij", "AbCdEfGhIj",
            "1a2b3c4d5e", "A1B2C3D4E5", "a0c0d0f0e0", "j5k4m3nh45"
    };
    // 完成标志
    private boolean done = false;


    protected void stop() {
        done = true;
        try {
            sock.close();
        } catch (IOException ex) {
            log.error("Couldn't close socket");
        }
        sock = null;
    }

    public static void main(String[] args) throws Exception {
        Random random = new Random(System.currentTimeMillis());
        log.debug("读取配置文件：j8583.xml");
        System.out.println("读取配置文件：j8583.xml");
        mfact = ConfigParser.createFromClasspathConfig("j8583.xml");
        mfact.setAssignDate(true);
        mfact.setTraceNumberGenerator(new SimpleTraceGenerator((int)(System.currentTimeMillis() % 10000)));
        mfact.setCustomField(48, new ProductEncoder());
        log.debug("连接服务器...");
        System.out.println("连接服务器...");
        Socket sock = new Socket("localhost", 9999);
        //Send 10 messages, then wait for the responses
        Client client = new Client(sock);
        Thread reader = new Thread(client, "j8583-client");
        reader.start();
        for (int i = 0; i < 10; i++) {
            IsoMessage req = mfact.newMessage(0x200);
            req.setValue(4, amounts[random.nextInt(amounts.length)], IsoType.AMOUNT, 0);
            req.setValue(12, req.getObjectValue(7), IsoType.TIME, 0);
            req.setValue(13, req.getObjectValue(7), IsoType.DATE4, 0);
            req.setValue(15, req.getObjectValue(7), IsoType.DATE4, 0);
            req.setValue(17, new Date(System.currentTimeMillis() + (86400*720)), IsoType.DATE_EXP, 0);
            req.setValue(37, new Long(System.currentTimeMillis() % 1000000), IsoType.NUMERIC, 12);
            req.setValue(41, data[random.nextInt(data.length)], IsoType.ALPHA, 16);
            req.setField(48, new IsoValue(IsoType.LLLVAR,
                    new ProductData((int)(System.currentTimeMillis() % 1000), data[random.nextInt(data.length)]),
                    new ProductEncoder()));
            //req.setValue(48, "string data", IsoType.LLLVAR, 0);
            pending.put(req.getField(11).toString(), req);
            log.debug("Sending request " + req.getField(11));
            System.out.println("发送请求：" + req.debugString());
            req.write(sock.getOutputStream(), 2);
        }
        log.debug("Waiting for responses");
        while (pending.size() > 0 && sock.isConnected()) {
            Thread.sleep(500);
        }
        client.stop();
        reader.interrupt();
        log.debug("DONE.");
    }
}
