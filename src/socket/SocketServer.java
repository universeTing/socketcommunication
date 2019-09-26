package socket;

import java.io.DataInputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wyt
 * @date 2019-5-22
 * @description 网络编程服务器端，用于监听一个指定的端口
 * @description 1、建立服务器端
 *                 服务器建立通信ServerSocket
 *                 服务器建立Socket接收客户端连接
 *                 建立IO输入流读取客户端发送的数据
 *                 建立IO输出流向客户端发送数据消息
 *
 * */
public class SocketServer extends Thread{

    //定义一个全局变量，为了让其在构造函数中实例化后，在线程里面重复利用，监听
    private ServerSocket serverSocket;

    //通过构造函获取一个端口并监听客户端请求。
    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //设置连接超时时间，超过这个时间则关闭套接字连接
        //serverSocket.setSoTimeout(10000);
    }

    //重写线程run()方法
    public void run(){
        while(true){
            System.out.println("等待远程连接。端口号为： " + serverSocket.getLocalPort()+ "……");
            try {
                // 实例化一个socket对象，accept()方法用于监听连接请求，直到有一个请求到了就会阻塞，
                // 并且返回一个新的socket引用，该socket连接到客户端的socket
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
            // 指定一个端口去监听
            // thread会去调用本身的run()方法，具体的逻辑再run()里面执行实现
            Thread t = new SocketServer(port);
            t.run();
            t.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
