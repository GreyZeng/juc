public class ServiceThread extends GracefulThread {
    private int count = 0;

    // ì‹Æ’†
    @Override
    protected void doWork() throws InterruptedException {
        System.out.print(".");
        Thread.sleep(100);
        count++;
        if (count >= 50) {
            shutdownRequest();  // ©•ª‚ÅI—¹
        }
    }

    // I—¹ˆ—
    @Override
    protected void doShutdown() {
        System.out.println("done.");
    }
}
