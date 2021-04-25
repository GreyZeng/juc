package git.snippets.juc;

import java.util.concurrent.*;

/**
 * 自定义拒绝策略
 */
public class MyRejectedHandler {
    public static void main(String[] args) {
        ExecutorService service = new ThreadPoolExecutor(4, 4,
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(6),
                Executors.defaultThreadFactory(),
                new MyHandler());
    }

    static class MyHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //log("r rejected")
            //save r kafka mysql redis
            //try 3 times
            if (executor.getQueue().size() < 10000) {
                //try put again();
            }
        }
    }
}
