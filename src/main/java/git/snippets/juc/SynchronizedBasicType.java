package git.snippets.juc;

/**
 * synchronized不能锁定String常量，Integer，Long等基础类型
 *
 * 不要以字符串常量作为锁定对象
 * 在下面的例子中，m1和m2其实锁定的是同一个对象
 * 这种情况还会发生比较诡异的现象，比如你用到了一个类库，在该类库中代码锁定了字符串“Hello”，
 * 但是你读不到源码，所以你在自己的代码中也锁定了"Hello",这时候就有可能发生非常诡异的死锁阻塞，
 * 因为你的程序和你用到的类库不经意间使用了同一把锁
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class SynchronizedBasicType implements Runnable {
    public static Integer i = 0;
    static SynchronizedBasicType instance = new SynchronizedBasicType();
    static final String lock = "this is a lock";
    static final String lock1 = "this is a lock";
    public static void main(String[] args) throws InterruptedException {
    	m();
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
    public static void m() throws InterruptedException {
    	Thread m1 = new Thread(new Runnable() {
            @Override
            public void run() {
                /*synchronized (this)*/
                synchronized (lock) {
                    System.out.println("locked ...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("unlocked ...");
                }
            }
        });
    	m1.start();
        Thread.sleep(1000);
        Thread m2 = new Thread(new Runnable() {
            @Override
            public void run() {
                /*synchronized (this)*/
                synchronized (lock1) {
                    System.out.println("locked lock1 ...");
                    System.out.println("unlocked lock1 ...");
                }
            }
        });
        m2.start();
        m1.join();
        m2.join();
    }
    @Override
    public void run() {
        for (int j = 0; j < 10000000; j++) {
            synchronized (i) {
                i++;
            }
        }
    }
}
