package j8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyt
 * @program socketcommunication
 * @description sockect的服务端，用于接收8583的请求报文
 * @create 2020-04-20 23:56
 **/
public class Server implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    /** 定义一个线程池*/
    private static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);

    private static MessageFactory mfact;

    private Socket socket;

    /** 构造函数*/
    Server(Socket sock) throws IOException {
        socket = sock;
    }

    public void run() {
        int count = 0;
        byte[] lenbuf = new byte[2];

        try {
            //For high volume apps you will be better off only reading the stream in this thread
            //and then using another thread to parse the buffers and process the requests
            //Otherwise the network buffer might fill up and you can miss a request.
            while (socket != null && socket.isConnected() && Thread.currentThread().isAlive() && !Thread.currentThread().isInterrupted()) {
                // 读取字节流
                if (socket.getInputStream().read(lenbuf) == 2) {
                    // 获取报文长度
                    int size = ((lenbuf[0] & 0xff) << 8) | (lenbuf[1] & 0xff);
                    byte[] buf = new byte[size];
                    //We're not expecting ETX in this case
                    socket.getInputStream().read(buf);
                    count++;
                    //Set a job to parse the message and respond -- 设置一个任务去解析报文并响应
                    //Delay it a bit to pretend we're doing something important -- 延迟一个字节的时间，假装在做其他重要的事情
                    threadPool.schedule(new Processor(buf, socket), 400, TimeUnit.MILLISECONDS);
                }
            }
        } catch (IOException ex) {
            log.error("Exception occurred...", ex);
        }
        log.debug("Exiting after reading {} requests", count);
        try {
            socket.close();
        } catch (IOException ex) {}
    }

    /**
     * 解析请求报文并组装响应报文
     */
    private class Processor implements Runnable {

        private byte[] msg;
        private Socket sock;

        Processor(byte[] buf, Socket s) {
            msg = buf;
            sock = s;
        }

        public void run() {
            try {
                log.debug("解析请求报文: '{}'", new String(msg));
                IsoMessage incoming = mfact.parseMessage(msg, 12);
                // 创建响应
                IsoMessage response = mfact.createResponse(incoming);
                response.setField(11, incoming.getField(11));
                response.setField(7, incoming.getField(7));
                response.setValue(38, System.currentTimeMillis() % 1000000, IsoType.NUMERIC, 6);
                response.setValue(39, 0, IsoType.NUMERIC, 2);
                response.setValue(61, String.format("Dynamic data generated on %1$tF at %1$tT", new Date()),
                        IsoType.LLLVAR, 0);
                log.debug("发送响应信息 {}", response.getField(38));
                response.write(sock.getOutputStream(), 2);
            } catch (ParseException ex) {
                log.error("Parsing incoming message", ex);
            } catch (IOException ex) {
                log.error("Sending response", ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        mfact = ConfigParser.createFromClasspathConfig("j8583.xml");
        log.info("启动socketServer,指定端口号...");
        System.out.println("启动socketServer,指定端口号...");
        ServerSocket server = new ServerSocket(9999);
        log.info("等待连接...");
        while (true) {
            Socket sock = server.accept();
            log.info("新的连接来自 {}:{}", sock.getInetAddress(), sock.getPort());
            System.out.println("新的连接来自 : " +  sock.getInetAddress() + " :"+ sock.getPort());
            // 启用一个新的线程去执行server，解析请求报文并响应回去
            new Thread(new Server(sock), "j8583-server").start();
        }
    }
}
