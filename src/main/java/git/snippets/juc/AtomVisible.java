package git.snippets.juc;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AtomXXX类可以保证可见性吗？请写一个程序来证明
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/26
 */
public class AtomVisible {
    public static void main(String[] args) {
        test2();
    }

    AtomicBoolean running = new AtomicBoolean(true);

    void m3() {
        System.out.println("m1 start");
        while (running.get()) {  //死循环。只有running=false时，才能执行后面的语句

        }
        System.out.println("m2 end");
    }

    public static void test2() {
        AtomVisible t = new AtomVisible();
        new Thread(t::m3, "t1").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.running.getAndSet(false);

    }
}
