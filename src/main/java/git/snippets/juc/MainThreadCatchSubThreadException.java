package git.snippets.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

// 主线程捕获子线程异常
public class MainThreadCatchSubThreadException {
    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new ThreadExceptionRunner());
            thread.start();
        } catch (Exception e) {
            System.out.println("========");
            e.printStackTrace();

        } finally {
        }
        System.out.println(123);
        // 方式一，在ThreadFactory中定义
        ExecutorService exec = Executors.newCachedThreadPool(new HandleThreadFactory());

        // 方式二，全局设置
//        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandle());
//        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ThreadExceptionRunner());
        exec.shutdown();
    }
}

class MyUncaughtExceptionHandle implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught " + e);
    }
}

class HandleThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        System.out.println("create thread t");
        Thread t = new Thread(r);
        System.out.println("set uncaughtException for t");
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandle());
        return t;
    }
}

class ThreadExceptionRunner implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("error !!!!");
    }
}