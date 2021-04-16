package git.snippets.juc;

import java.io.IOException;

/**
 * 一个同步方法可以调用另外一个同步方法，一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁.
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class SynchronizedReentry implements Runnable {


    public static void main(String[] args) throws IOException {
        SynchronizedReentry myRun = new SynchronizedReentry();
        Thread thread = new Thread(myRun, "t1");
        Thread thread2 = new Thread(myRun, "t2");
        thread.start();
        thread2.start();
        System.in.read();

    }

    synchronized void m1(String content) {
        System.out.println(this);
        System.out.println("m1 get content is " + content);
        m2(content);
    }

    synchronized void m2(String content) {
        System.out.println(this);
        System.out.println("m2 get content is " + content);

    }

    @Override
    public void run() {
        m1(Thread.currentThread().getName());
    }


}
