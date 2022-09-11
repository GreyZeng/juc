package git.snippets.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 写一个程序证明AtomXXX类的多个方法并不构成原子性
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/26
 */
public class MultiAtomMethod {
    public static void main(String[] args) {
        test3();
    }

    AtomicInteger count = new AtomicInteger(0);

    void m4() {
        for (int i = 0; i < 10000; i++) {
            if (count.get() < 999 && count.get() >= 0) { //如果未加锁,之间还会有其他线程插进来
                count.incrementAndGet();
            }
        }
    }

    public static void test3() {
        MultiAtomMethod t = new MultiAtomMethod();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(t::m4, "thread" + i));
        }
        threads.forEach(Thread::start);
        threads.forEach((o) -> {
            try {
                //join()方法阻塞调用此方法的线程,直到线程t完成，此线程再继续。通常用于在main()主线程内，等待其它线程完成再结束main()主线程。
                o.join(); //相当于在main线程中同步o线程，o执行完了，main线程才有执行的机会
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count);
    }


}
