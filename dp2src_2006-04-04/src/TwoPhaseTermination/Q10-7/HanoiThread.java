public class HanoiThread extends Thread {
    // 終了要求が出されたらtrue
    private volatile boolean shutdownRequested = false;
    // 終了要求が出された時刻
    private volatile long requestedTimeMillis = 0;

    // 終了要求
    public void shutdownRequest() {
        requestedTimeMillis = System.currentTimeMillis();
        shutdownRequested = true;
        interrupt();
    }

    // 終了要求が出されたかどうかのテスト
    public boolean isShutdownRequested() {
        return shutdownRequested;
    }

    // 動作
    public void run() {
        try {
            for (int level = 0; !isShutdownRequested(); level++) {
                System.out.println("==== Level " + level + " ====");
                doWork(level, 'A', 'B', 'C');
                System.out.println("");
            }
        } catch (InterruptedException e) {
        } finally {
            doShutdown();
        }
    }

    // 作業
    private void doWork(int level, char posA, char posB, char posC) throws InterruptedException {
        if (level > 0) {
            doWork(level - 1, posA, posC, posB);
            System.out.print(posA + "->" + posB + " ");
            doWork(level - 1, posC, posB, posA);
        }
    }

    // 終了処理
    private void doShutdown() {
        long time = System.currentTimeMillis() - requestedTimeMillis;
        System.out.println("doShutdown: Latency = " + time + " msec.");
    }
}
