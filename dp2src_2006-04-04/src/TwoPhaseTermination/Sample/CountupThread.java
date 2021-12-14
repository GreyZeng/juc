public class CountupThread extends Thread {
    // カウンタの値
    private long counter = 0;

    // 終了要求が出されたらtrue
    private volatile boolean shutdownRequested = false;

    // 終了要求
    public void shutdownRequest() {
        shutdownRequested = true;
        interrupt();
    }

    // 終了要求が出されたかどうかのテスト
    public boolean isShutdownRequested() {
        return shutdownRequested;
    }

    // 動作
    public final void run() {
        try {
            while (!isShutdownRequested()) {
                doWork();
            }
        } catch (InterruptedException e) {
        } finally {
            doShutdown();
        }
    }

    // 作業
    private void doWork() throws InterruptedException {
        counter++;
        System.out.println("doWork: counter = " + counter);
        Thread.sleep(500);
    }

    // 終了処理
    private void doShutdown() {
        System.out.println("doShutdown: counter = " + counter);
    }
}
