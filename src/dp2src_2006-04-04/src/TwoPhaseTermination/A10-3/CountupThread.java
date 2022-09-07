import java.io.IOException;
import java.io.FileWriter;

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
    public void run() {
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
        System.out.println("doShutdown: Save BEGIN");
        try {
            FileWriter writer = new FileWriter("counter.txt");
            writer.write("counter = " + counter);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("doShutdown: Save END");
    }
}
