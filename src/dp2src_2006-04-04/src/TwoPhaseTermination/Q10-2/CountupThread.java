public class CountupThread extends Thread {
    // カウンタの値
    private long counter = 0;

    // 終了要求
    public void shutdownRequest() {
        interrupt();
    }

    // 動作
    public void run() {
        try {
            while (!isInterrupted()) {
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
