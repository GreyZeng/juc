package git.snippets.juc;

import java.util.concurrent.TimeUnit;

/**
 * 锁定某对象o，如果o的属性发生改变，不影响锁的使用
 * 但是如果o变成另外一个对象，则锁定的对象发生改变
 * 应该避免将锁定对象的引用变成另外的对象
 */
public class SyncSameObject {
    Object object = new Object();

    public static void main(String[] args) {
        SyncSameObject t = new SyncSameObject();
        new Thread(t::m).start();
        Thread t2 = new Thread(t::m, "t2");
        //锁对象发生改变，所以t2线程得以执行，如果注释掉这句话，线程2将永远得不到执行机会
        t.object = new Object();

        t2.start();
    }

    void m() {
        synchronized (object) {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("current thread is " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
