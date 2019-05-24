import java.util.concurrent.*;

public class TestThread implements Callable<Integer>{

    /**
     * 通过Callable来创建线程的话，必须要实现call方法，要么就将类修改为抽象类，通过子类来实现
     * @return
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception{
        int i = 0;
        for (int j = 0; j < 100; j++) {
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
        return i;
    }

    public static void main(String arg[]){
/*****************************************************几种线程的创建方式*********************************************************/
        /**
         * 实现Runable接口来创建的线程
         */
        ThreadImplementsRunable threadForRunable1 = new ThreadImplementsRunable("ThreadForRunable1");
        threadForRunable1.start();

        /**
         * 继承Tread创建的线程
         */
        ThreadExtendThread threadExtendThread = new ThreadExtendThread("ThreadExtendThread2");
        threadExtendThread.start();
        /**
         * 通过Callable和Future来创建线程
         */
        TestThread ts = new TestThread();
        FutureTask<Integer> ft = new FutureTask<>(ts);
        for(int i = 0;i< 100; i++){
            System.out.println(Thread.currentThread().getName()+" 的循环变量i的值"+i);
            if(i == 20){
                //new Thread(ft,"有返回值的线程").start();
                //不知道为什么这里不支持这种写法
            }
        }
        try
        {
            System.out.println("子线程的返回值："+ft.get());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }






/*****************************************************几种线程池测试*********************************************************/
        /**
         * 测试固定长度的线程池，指定最大运行的进行数量
         */
        int maxThread = 3;
        ExecutorService pool = Executors.newFixedThreadPool(maxThread);
        for (int i = 0; i < maxThread; i++) {
            ThreadImplementsRunable threadForRunableForPools1 = new ThreadImplementsRunable("threadForRunableForPools"+ i);
            pool.execute(threadForRunableForPools1);
        }
        //关闭线程池
        pool.shutdown();


        /**
         * 测试单线程的连接池
         */
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        ThreadImplementsRunable threadForRunableForSinglePool = new ThreadImplementsRunable("threadForRunableForSinglePool");
        pool1.execute(threadForRunableForSinglePool);
        pool1.shutdown();

        /**
         * 测试可变长的连接池
         */
        ExecutorService pool2 = Executors.newCachedThreadPool();
        ThreadImplementsRunable threadForRunableForCachedPool = new ThreadImplementsRunable("threadForRunableForCachedPool");
        pool2.execute(threadForRunableForCachedPool);
        pool2.shutdown();

        /**
         * 测试延迟连接池,指定线程数量
         */
        ScheduledExecutorService scheduledExecutorServicePool = Executors.newScheduledThreadPool(maxThread);
        for (int i = 0; i < maxThread; i++) {
            ThreadImplementsRunable scheduledExecutorServiceThread = new ThreadImplementsRunable("scheduledExecutorServiceThread" + i);
            scheduledExecutorServicePool.execute(scheduledExecutorServiceThread);
            //指定延迟的时间
            scheduledExecutorServicePool.schedule(scheduledExecutorServiceThread,1000, TimeUnit.MILLISECONDS);
        }
        scheduledExecutorServicePool.shutdown();

    }
}
