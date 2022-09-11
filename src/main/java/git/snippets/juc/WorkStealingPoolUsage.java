/**
 *
 */
package git.snippets.juc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @since 1.8
 */
public class WorkStealingPoolUsage {
    public static void main(String[] args) throws IOException {
        int core = Runtime.getRuntime().availableProcessors();
        //  会自动启动cpu核数个线程去执行任务 ,其中第一个是1s执行完毕,其余都是2s执行完毕,
        //  有一个任务会进行等待,当第一个执行完毕后,会再次偷取最后一个任务执行
        ExecutorService service = Executors.newWorkStealingPool();
        service.execute(new R(1000));
        for (int i = 0; i < core; i++) {
            service.execute(new R(2000));
        }
        //由于产生的是精灵线程（守护线程、后台线程），主线程不阻塞的话，看不到输出
        System.in.read();
    }

    static class R implements Runnable {

        int time;

        R(int t) {
            this.time = t;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(time + " " + Thread.currentThread().getName());
        }
    }
}
