package git.snippets.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Java线程状态
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class ThreadState {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(()->{
            try {
                System.out.println("2. 线程状态:"+Thread.currentThread().getState());
                Thread.sleep(1_000);
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
        });
        System.out.println("1. 线程状态:" + t.getState());
        t.start();
        t.join();
        System.out.println("3. 线程状态：" + t.getState());

        final Object o = new Object();
        Thread t2 = new Thread(()->{
            synchronized(o) {

            }
        });
        new Thread(()->{
            synchronized(o) {
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) { 
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(1);
        t2.start();
        Thread.sleep(10);
        System.out.println("6: 线程状态："+t2.getState());

        Thread t3 = new Thread(()->{
            LockSupport.park();
        });
        t3.start();
        Thread.sleep(100);
        System.out.println("8：线程状态："+t3.getState());
        LockSupport.unpark(t3);


        Thread t4 = new Thread(()->{
            LockSupport.park();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t4.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("4: 线程状态：" + t4.getState());
        
        LockSupport.unpark(t4);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("5: 线程状态：" + t4.getState());
    
        
        final Lock lock = new ReentrantLock();
        Thread t5 = new Thread(()->{
            lock.lock();
            
            lock.unlock();
        });
        new Thread(()->{
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            lock.unlock();
        }).start();
        TimeUnit.SECONDS.sleep(1);
        t5.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("7: 线程状态：" + t5.getState());
        
    
    }
}
