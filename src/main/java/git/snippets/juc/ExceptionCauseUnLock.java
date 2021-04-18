package git.snippets.juc;

import java.util.concurrent.TimeUnit;

public class ExceptionCauseUnLock {
    /*volatile */ boolean stop = false;

    public static void main(String[] args) {
        ExceptionCauseUnLock t = new ExceptionCauseUnLock();
        new Thread(t::m, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (t.stop) {
            int m = 1 / 0;
        }
    }

    synchronized void m() {
        while (!stop) {
            stop = true;
        }
    }
}

