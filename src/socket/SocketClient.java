package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.io.*;

/**
 * @author wyt
 * @date 2019-5-22
 * @description 网络编程客户端
 * */
public class SocketClient{
    public static void main(String[] args){
        String serverName = "127.0.0.1";
        int port = 8888;
        try {
            System.out.println("连接到主机："+ serverName + ",端口号： " + port);
            //实例化一个套接字对象，传入端口和服务名进行连接
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址："+ client.getRemoteSocketAddress());
            //创建一个输出流，用于套接字通信传输
            OutputStream outputStreamServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStreamServer);
            out.writeUTF("Hello from " + client.getLocalSocketAddress());

            //用于接收套接字传输过来的信息
            InputStream inputStreamFromServer = client.getInputStream();
            DataInputStream inputStream = new DataInputStream(inputStreamFromServer);
            System.out.println("服务器响应： "+ inputStream.readUTF());
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }}