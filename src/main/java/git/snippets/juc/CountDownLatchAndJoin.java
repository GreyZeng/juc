package git.snippets.juc;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch可以用Join替代
 */
public class CountDownLatchAndJoin {
	public static void main(String[] args) {
		useCountDownLatch();
		useJoin();
	}

	public static void useCountDownLatch() {
		// use countdownlatch
		long start = System.currentTimeMillis();
		Thread[] threads = new Thread[100000];
		CountDownLatch latch = new CountDownLatch(threads.length);

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				int result = 0;
				for (int i1 = 0; i1 < 1000; i1++) {
					result += i1;
				}
				// System.out.println("Current thread " + Thread.currentThread().getName() + " finish cal result " + result);
				latch.countDown();
			});
		}
		for (Thread thread : threads) {
			thread.start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();

		System.out.println("end latch down, time is " + (end - start));

	}

	public static void useJoin() {
		long start = System.currentTimeMillis();

		// use join
		Thread[] threads = new Thread[100000];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				int result = 0;
				for (int i1 = 0; i1 < 1000; i1++) {
					result += i1;
				}
				// System.out.println("Current thread " + Thread.currentThread().getName() + " finish cal result " + result);
			});
		}
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long end = System.currentTimeMillis();

		System.out.println("end join, time is " + (end - start));
	}
}
