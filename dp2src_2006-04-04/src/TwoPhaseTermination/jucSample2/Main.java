import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int THREADS = 3; // スレッドの個数

    public static void main(String[] args) {
        System.out.println("BEGIN");

        // 仕事を実行するスレッドを提供するExecutorService
        ExecutorService service = Executors.newFixedThreadPool(THREADS);

        // バリアが解除されるときのアクション
        Runnable barrierAction = new Runnable() {
            public void run() {
                System.out.println("Barrier Action!");
            }
        };

        // フェーズを合わせるCyclicBarrier
        CyclicBarrier phaseBarrier = new CyclicBarrier(THREADS, barrierAction);

        // 仕事の終了を調べるCountDownLatch
        CountDownLatch doneLatch = new CountDownLatch(THREADS);

        try {
            // 仕事を開始する
            for (int t = 0; t < THREADS; t++) {
                service.execute(new MyTask(phaseBarrier, doneLatch, t));
            }
            // 仕事の終了を待つ
            System.out.println("AWAIT");
            doneLatch.await();
        } catch (InterruptedException e) {
        } finally {
            service.shutdown();
            System.out.println("END");
        }
    }
}
