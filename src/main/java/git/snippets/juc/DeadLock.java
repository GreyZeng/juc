package git.snippets.juc;

/**
 * 模拟死锁
 */
public class DeadLock implements Runnable {
    int flag = 1;
    static final Object o1 = new Object();
    static final Object o2 = new Object();

    public static void main(String[] args) {
        DeadLock lock = new DeadLock();
        DeadLock lock2 = new DeadLock();
        lock.flag = 1;
        lock2.flag = 0;
        Thread t1 = new Thread(lock);
        Thread t2 = new Thread(lock2);
        t1.start();
        t2.start();

    }

    @Override
    public void run() {
        System.out.println("flag = " + flag);
        if (flag == 1) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("1");
                }
            }
        }
        if (flag == 0) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (o2) {
                    System.out.println("0");
                }
            }
        }
    }

}
