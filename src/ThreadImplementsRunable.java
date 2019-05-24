/**
 * @author wyt
 * @date 2019-5-22
 * @description 创建Java线程，实现Runnable接口 ，需创建Thread对象并实例化，可以重写run()方法
 */
public class ThreadImplementsRunable implements Runnable{

    /**
     * 新建一个线程对象，处于新建状态
     * Thread 继承Runable，常用的构造函数是
     * Thread(Runnable threadOb,String threadName);
     * threadOb为继承Runnable类的实例
     * threadName 指定新的线程名字
     */
    private Thread thread;

    /**
     * 可为其指定一个名字
     */
    private String threadName;


    ThreadImplementsRunable(String threadName){
        this.threadName = threadName;
        System.out.println("新建线程：" + threadName);
    }

    @Override
    public void run() {
        System.out.println(threadName + "运行中……：");
        try {
            for (int i = 5; i > 0 ; i--) {
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

