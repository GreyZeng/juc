package git.snippets.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/4/25
 * @since
 */
public class ForkJoinPoolUsage implements Calculator {
    private ForkJoinPool pool;

    public ForkJoinPoolUsage() {
        // 也可以使用公用的 ForkJoinPool：
        // pool = ForkJoinPool.commonPool()
        pool = new ForkJoinPool();
    }

    public static void useRecursiveAction() throws InterruptedException {
        // 创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // 提交可分解的PrintTask任务
        forkJoinPool.submit(new MyRecursiveAction(0, 1000));

        while (!forkJoinPool.isTerminated()) {
            forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);
        }
        // 关闭线程池
        forkJoinPool.shutdown();
    }

    public static void useRecursiveTask() {
        long[] numbers = LongStream.rangeClosed(1, 1000).toArray();
        Calculator calculator = new ForkJoinPoolUsage();
        System.out.println(calculator.sumUp(numbers)); // 打印结果500500
    }

    public static void main(String[] args) throws InterruptedException {
        useRecursiveTask();
        useRecursiveAction();
    }

    @Override
    public long sumUp(long[] numbers) {
        return pool.invoke(new SumTask(numbers, 0, numbers.length - 1));
    }

    private static class MyRecursiveAction extends RecursiveAction {

        /**
         * 每个"小任务"最多只打印20个数
         */
        private static final int MAX = 20;

        private int start;
        private int end;

        public MyRecursiveAction(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            //当end-start的值小于MAX时，开始打印
            if ((end - start) < MAX) {
                for (int i = start; i < end; i++) {
                    System.out.println(Thread.currentThread().getName() + "-i的值" + i);
                }
            } else {
                // 将大任务分解成两个小任务
                int middle = (start + end) / 2;
                MyRecursiveAction left = new MyRecursiveAction(start, middle);
                MyRecursiveAction right = new MyRecursiveAction(middle, end);
                left.fork();
                right.fork();
            }
        }


    }

    private static class SumTask extends RecursiveTask<Long> {
        private long[] numbers;
        private int from;
        private int to;

        public SumTask(long[] numbers, int from, int to) {
            this.numbers = numbers;
            this.from = from;
            this.to = to;
        }


        @Override
        protected Long compute() {

            // 当需要计算的数字小于6时，直接计算结果
            if (to - from < 6) {
                long total = 0;
                for (int i = from; i <= to; i++) {
                    total += numbers[i];
                }
                return total;
                // 否则，把任务一分为二，递归计算
            } else {
                int middle = (from + to) / 2;
                SumTask taskLeft = new SumTask(numbers, from, middle);
                SumTask taskRight = new SumTask(numbers, middle + 1, to);
                taskLeft.fork();
                taskRight.fork();
                return taskLeft.join() + taskRight.join();
            }
        }
    }
}

interface Calculator {
    long sumUp(long[] numbers);
}