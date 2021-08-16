package git.snippets.juc;

import java.util.concurrent.*;

/**
 * 创建线程的方式
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/7/7
 * @since
 */
public class HelloThread {
    public static void main(String[] args) throws Exception {
        MyFirstThread t1 = new MyFirstThread();
        Thread t2 = new Thread(new MySecondThread());
        Thread t3 = new Thread(new FutureTask<>(new CallableThreadTest())); 
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()-> System.out.println("方式3：使用线程池来创建线程。"));
        t1.start();
        t2.start();
        t3.start(); 
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);       
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
            int i;
            for (i = 0; i < 10; i++) {
                i++;
            }
            System.out.println("方式4，实现Callable接口方式来创建有返回值的线程，返回值是："+i);
            return i;
        }
    } 
}