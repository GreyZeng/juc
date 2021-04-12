package git.snippets.juc;

import java.util.concurrent.*;

/**
 * Java创建线程的五种不同的方式
 * Grey 2020/12/11
 **/
public class HelloThread {
    public static void main(String[] args) throws Exception {
        MyFirstThread t1 = new MyFirstThread();
        Thread t2 = new Thread(new MySecondThread());
        Thread t3 = new Thread(new FutureTask<>(new CallableThreadTest()));
        Thread t4 = new Thread(()->{
            System.out.println("方式4：使用lambada表达式来创建线程。");
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            System.out.println("方式5：使用线程池来创建线程。");
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        
    }

    static class MyFirstThread extends Thread {
        @Override
        public void run() {
            System.out.println("方式1：继承Thread类并重写run方法来创建线程");
        }
    }

    /**
     * 方式二， 实现Runnable接口来创建线程
     */
    static class MySecondThread implements Runnable {

        @Override
        public void run() {
            System.out.println("方式2：实现Runnable方式来创建线程");
        }
    }

    static class CallableThreadTest implements Callable<Integer> {
        @Override
        public Integer call() {
            int i = 0;
            for (i = 0; i < 10; i++) {
                i++;
            }
            System.out.println("方式3，实现Callable接口方式来创建有返回值的线程，返回值是："+i);
            return i;
        }
    }
}