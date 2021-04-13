作者：[Grey](https://www.cnblogs.com/greyzeng/)

原文地址：[Java多线程学习笔记](https://www.cnblogs.com/greyzeng/p/14176141.html)


## 什么是程序，进程和线程？

- 程序是计算机的可执行文件
- 进程是计算机资源分配的基本单位
- 线程是资源调度执行的基本单位
  - 一个程序里面不同的执行路径
  - 多个线程共享进程中的资源

## 线程和进程的关系

线程就是轻量级进程，是程序执行的最小单位。

多进程的方式也可以实现并发，为什么我们要使用多线程？

1. 共享资源在线程间的通信比较容易。
2. 线程开销更小。

## 进程和线程的区别？

- 进程是一个独立的运行环境，而线程是在进程中执行的一个任务。他们两个本质的区别是是否单独占有内存地址空间及其它系统资源（比如I/O）。
- 进程单独占有一定的内存地址空间，所以进程间存在内存隔离，数据是分开的，数据共享复杂但是同步简单，各个进程之间互不干扰；而线程共享所属进程占有的内存地址空间和资源，数据共享简单，但是同步复杂。
- 进程单独占有一定的内存地址空间，一个进程出现问题不会影响其他进程，不影响主程序的稳定性，可靠性高；一个线程崩溃可能影响整个程序的稳定性，可靠性较低。
- 进程单独占有一定的内存地址空间，进程的创建和销毁不仅需要保存寄存器和栈信息，还需要资源的分配回收以及页调度，开销较大；线程只需要保存寄存器和栈信息，开销较小。
- 进程是操作系统进行资源分配的基本单位，而线程是操作系统进行调度的基本单位，即CPU分配时间的单位。

## 什么是线程切换？

从底层角度上看，CPU主要由如下三部分组成，分别是：

- ALU: 计算单元
- Registers: 寄存器组
- PC：存储到底执行到哪条指令

T1线程在执行的时候，将T1线程的指令放在PC，数据放在Registers，假设此时要切换成T2线程，T1线程的指令和数据放cache，然后把T2线程的指令放PC，数据放Registers，执行T2线程即可。

以上的整个过程是通过操作系统来调度的，且线程的调度是要消耗资源的，所以，线程不是设置越多越好。

## 单核CPU设定多线程是否有意义？

有意义，因为线程的操作中可能有不消耗CPU的操作，比如：等待网络的传输，或者线程sleep，此时就可以让出CPU去执行其他线程。可以充分利用CPU资源。

- CPU密集型
- IO密集型

## 线程数量是不是设置的越大越好？

不是，因为线程切换要消耗资源。

示例：
单线程和多线程来累加1亿个数。-> CountSum.java

## 工作线程数（线程池中线程数量）设多少合适？

- 和CPU的核数有关

- 最好是通过压测来评估。通过profiler性能分析工具jProfiler，或者Arthas

- 公式
```
N = Ncpu * Ucpu * (1 + W/C)
```

其中：

- Ncpu是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors() 得到

- Ucpu是期望的CPU利用率（该值应该介于0和1之间）

- W/C是等待时间和计算时间的比率。

## Java中创建线程的方式

1. 继承Thread类，重写run方法
2. 实现Runnable接口，实现run方法，这比方式1更好，因为一个类实现了Runnable以后，还可以继承其他类
3. 使用lambda表达式
4. 通过线程池创建
5. 通过Callable/Future创建（需要返回值的时候）

具体示例可见：HelloThread.java

## 线程状态

- NEW 

> 线程刚刚创建，还没有启动
> 即：刚刚New Thread的时候，还没有调用start方法时候，就是这个状态

  
- RUNNABLE

> 可运行状态，由线程调度器可以安排执行，包括以下两种情况：
> - READY
> - RUNNING
> 
> READY和RUNNING通过yield来切换   

- WAITING
> 等待被唤醒

- TIMED_WAITING
> 隔一段时间后自动唤醒

- BLOCKED

> 被阻塞，正在等待锁
> 只有在synchronized的时候在会进入BLOCKED状态

- TERMINATED
> 线程执行完毕后，是这个状态

## 线程状态切换

![java_thread_state](https://img2020.cnblogs.com/blog/683206/202104/683206-20210412191009944-1947255770.png)

## 线程基本操作

### sleep

当前线程睡一段时间

### yield

这是一个静态方法，一旦执行，它会使当前线程让出一下CPU。但要注意，让出CPU并不表示当前线程不执行了。当前线程在让出CPU后，还会进行CPU资源的争夺，但是是否能够再次被分配到就不一定了。

### join

等待另外一个线程的结束，当前线程才会运行

```java
public class ThreadBasicOperation {
    static volatile int sum = 0;

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(()->{
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
        });
        t.start();
        // join 方法表示主线程愿意等待子线程执行完毕后才继续执行
        // 如果不使用join方法，那么sum输出的可能是一个很小的值，因为还没等子线程
        // 执行完毕后，主线程就已经执行了打印sum的操作
        t.join();
        System.out.println(sum);
    }
}
```

示例代码：ThreadBasicOperation.java

### interrupt

- interrupt()
> 打断某个线程(设置标志位)

- isInterrupted()
> 查询某线程是否被打断过(查询标志位)

- static interrrupted
> 查询当前线程是否被打断过，并重置打断标志位

示例代码：ThreadInterrupt.java

## 如何结束一个线程

### 不推荐的方式

- stop方法
- suspend/resume方法
  
以上两种方式都不建议使用, 因为会产生数据不一致的问题，因为会释放所有的锁。

### 优雅的方式

如果不依赖循环的具体次数或者中间状态, 可以通过设置标志位的方式来控制

```java
public class ThreadFinished {
    private static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException {

        // 推荐方式:设置标志位
        Thread t3 = new Thread(() -> {
            long i = 0L;
            while (flag) {
                i++;
            }
            System.out.println("count sum i = " + i);
        });
        t3.start();
        TimeUnit.SECONDS.sleep(1);
        flag = false;
    }
}
```

如果要依赖循环的具体次数或者中间状态, 则可以用interrupt方式

```java
public class ThreadFinished {

    public static void main(String[] args) throws InterruptedException {
        // 推荐方式:使用interrupt
        Thread t4 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("t4 end");
        });
        t4.start();
        TimeUnit.SECONDS.sleep(1);
        t4.interrupt();
    }
}
```

示例代码: ThreadFinished.java


## 并发编程的三大特性

### 可见性

每个线程会保存一份拷贝到线程本地缓存,使用volatile,可以保持线程之间数据可见性。

如下示例: ThreadVisible.java

```java
public class ThreadVisible {
    
    static volatile   boolean  flag = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(()->{
            while(flag) {
                // 如果这里调用了System.out.println()
                // 会无论flag有没有加volatile,数据都会同步
                // 因为System.out.println()背后调用的synchronized
                // System.out.println();
            }
            System.out.println("t end");
        });
        t.start();
        TimeUnit.SECONDS.sleep(3);
        flag = false;


        // volatile修饰引用变量
        new Thread(a::m,"t2").start();
        TimeUnit.SECONDS.sleep(2);
        a.flag = false;

        // 阻塞主线程,防止主线程直接执行完毕,看不到效果
        new Scanner(System.in).next();
    }
    private static volatile A a = new A();
    static class A {
        boolean flag = true;
        void m() {
            System.out.println("m start");
            while(flag){}
            System.out.println("m end");
        }   
    }
}
```

代码说明: 
- 如在上述代码的死循环中增加了System.out.println(), 则会强制同步flag的值,无论flag本身有没有加volatile。
- 如果volatile修饰一个引用对象,如果对象的属性(成员变量)发生了改变,volatile不能保证其他线程可以观察到该变化。

关于三级缓存

![3_cache](https://img2020.cnblogs.com/blog/683206/202104/683206-20210413223658983-1491978414.png)

如上图，内存读出的数据会在L3，L2，L1上都存一份。所谓线程数据的可见性，指的就是内存中的某个数据，假如第一个CPU的一个核读取到了，和其他的核读取到这个数据之间的可见性。

在从内存中读取数据的时候，根据的是程序局部性的原理，按块来读取，这样可以提高效率，充分发挥总线CPU针脚等一次性读取更多数据的能力。

所以这里引入了一个缓存行的概念，目前一个缓存行多用**64个字节**来表示。

如何来验证CPU读取缓存行这件事，我们可以通过一个示例来说明：

```java
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

    private static class T /**extends Padding*/ {
        public volatile long x = 0L;
    } 
}

```
说明：以上代码，T这个类extends Padding与否，会影响整个流程的执行时间，如果继承了，会减少执行时间，因为继承Padding后，arr[0]和arr[1]一定不在同一个缓存行里面，所以不需要同步数据，速度就更快一些了。

> jdk1.8增加了一个注解：@Contended，标注了以后，不会在同一缓存行, 仅适用于jdk1.8
还需要增加jvm参数：

```
-XX:-RestrictContended
```

CPU为每个缓存行标记四种状态（使用两位）

- Exclusive
- Invalid
- Shared
- Modified

### 有序性

### 原子性

待续...

## 思维导图

[processon](https://www.processon.com/view/5ec513425653bb6f2a1f7da8)

## 完整源码

[Github](https://github.com/GreyZeng/juc)


## 参考资料

[多线程与高并发-马士兵](https://ke.qq.com/course/3132461?tuin=b09cbb87)

[实战Java高并发程序设计(第2版)](https://book.douban.com/subject/30358019/)

[深入浅出Java多线程](http://concurrent.redspider.group/RedSpider.html)

[Java并发编程实战](https://book.douban.com/subject/10484692/)