public class GracefulThread extends Thread {
    // 終了要求が出されたらtrue
    private volatile boolean shutdownRequested = false;

    // 終了要求
    public final void shutdownRequest() {
        shutdownRequested = true;
        interrupt();
    }

    // 終了要求が出されたかどうかのテスト
    public final boolean isShutdownRequested() {
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
    protected void doWork() throws InterruptedException {
    }

    // 終了処理
    protected void doShutdown() {
    }
}
