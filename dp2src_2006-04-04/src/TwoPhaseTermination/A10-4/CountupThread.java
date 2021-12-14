public class CountupThread extends GracefulThread {
    // カウンタの値
    private long counter = 0;

    // 作業
    @Override
    protected void doWork() throws InterruptedException {
        counter++;
        System.out.println("doWork: counter = " + counter);
        Thread.sleep(500);
    }

    // 終了処理
    @Override
    protected void doShutdown() {
        System.out.println("doShutdown: counter = " + counter);
    }
}
