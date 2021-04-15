package git.snippets.juc;

import java.util.concurrent.TimeUnit;

/**
 * 模拟脏读
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/15
 * @since
 */
public class DirtyRead {
    String name;
    double balance;

    public static void main(String[] args) {
        DirtyRead a = new DirtyRead();
        Thread thread = new Thread(() -> a.set("zhangsan", 100.0));

        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a.getBalance("zhangsan"));
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a.getBalance("zhangsan"));
    }

    public synchronized void set(String name, double balance) {
        this.name = name;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        this.balance = balance;
    }

    // 如果get方法不加synchronized关键字，就会出现脏读情况
    public /*synchronized*/ double getBalance(String name) {
        return this.balance;
    }
}
