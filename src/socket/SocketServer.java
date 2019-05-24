package socket;

import java.io.DataInputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wyt
 * @date 2019-5-22
 * @description 网络编程服务器端，用于监听一个指定的端口
 * */
public class SocketServer extends Thread{

    private ServerSocket serverSocket;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //设置连接超时时间，超过这个时间则关闭套接字连接
        serverSocket.setSoTimeout(1000);
    }

    //重写线程run()方法
    public void run(){
        while(true){
            System.out.println("等待远程连接。端口号为： " + serverSocket.getLocalPort()+ "……");
            try {
                //实例化一个socket对象
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址： " + server.getRemoteSocketAddress());

                //读取通讯传输进来的数据并打印出来
                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());
                //输出信息，传给客户端的信息
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("GOODBYB……" + server.getLocalSocketAddress());
                server.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] agrs){
        int port = 8888;
        try {
            Thread t = new SocketServer(port);
            t.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
