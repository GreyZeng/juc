package git.snippets.juc;

/**
 * 缓存行对齐
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since 1.8
 */
public class CacheLinePadding {
  public static T[] arr = new T[2];

  static {
    arr[0] = new T();
    arr[1] = new T();
  }

  public static void main(String[] args) throws Exception {
    Thread t1 = new Thread(() -> {
      for (long i = 0; i < 1000_0000L; i++) {
        arr[0].x = i;
      }
    });

    Thread t2 = new Thread(() -> {
      for (long i = 0; i < 1000_0000L; i++) {
        arr[1].x = i;
      }
    });

    final long start = System.nanoTime();
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println((System.nanoTime() - start) / 100_0000);
  }
  private static class Padding {
    public volatile long p1, p2, p3, p4, p5, p6, p7;
  }

  // T这个类extends Padding与否，会影响整个流程的执行时间，如果继承了，会减少执行时间，
  // 因为继承Padding后，arr[0]和arr[1]一定不在同一个缓存行里面，所以不需要同步数据，速度就更快一些了。
  private static class T extends Padding   {
    public volatile long x = 0L;
  }
}
