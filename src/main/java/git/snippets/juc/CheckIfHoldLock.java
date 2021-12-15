package git.snippets.juc;

import git.snippets.dp2src.introduction1.jucThreadFactory.Main;

/**
 * 检查当前线程是否持有某个锁
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/12/15
 * @since 1.8
 */
public class CheckIfHoldLock {
    public static void main(String[] args) {
        new CheckIfHoldLock().test();
    }

    public synchronized void test() {
        for (int i = 0; i < 100; i++) {
            System.out.println("sleep");
            // 检查是否持有某个锁
            boolean b = Thread.holdsLock(this);
            System.out.println(b);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
