/**
 * @author wyt
 * @date 2019-5-22
 * @description 创建Java线程，继承Thread类，必须重写run()方法，该方法是新线程的入口点，必须调用start()方法才能执行。
 * 其本质上也是实现了Runable接口的一个实例
 */
public class ThreadExtendThread extends Thread{
    private Thread thread;
    private String threadName;

    //构造函数
    public ThreadExtendThread (String name){
        threadName = name;
    }

    public void run(){
        System.out.println(threadName + "运行中……：");
        try {
            for (int i = 3; i > 0 ; i--) {
                System.out.println(threadName + "  第"+ i + "次运行：");
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start(){
        System.out.println(" 开始: "+ threadName);
        if(thread == null){
            thread = new Thread(this,threadName);
            thread.start();
        }
    }


}
