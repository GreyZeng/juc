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

## 线程数量是不是设置地越大越好？

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

- static interrupted
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
还需要增加jvm参数

```
-XX:-RestrictContended
```

CPU为每个缓存行标记四种状态（使用两位） 

- Exclusive
- Invalid
- Shared
- Modified

参考：[【并发编程】MESI--CPU缓存一致性协议](https://www.cnblogs.com/z00377750/p/9180644.html)

### 有序性

为什么会出现乱序执行呢？因为CPU为了提高效率，可能在执行某些指令的时候，不按顺序执行（指令前后没有依赖关系的时候）

乱序存在的条件是：不影响单线程的最终一致性(as - if - serial)

验证乱序执行的程序示例 DisOrder.java：

```java
public class DisOrder {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    // 以下程序可能会执行比较长的时间
    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        for (;;) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread one = new Thread(() -> {
                // 由于线程one先启动，下面这句话让它等一等线程two. 读着可根据自己电脑的实际性能适当调整等待时间.
                shortWait(100000);
                a = 1;
                x = b;
            });

            Thread other = new Thread(() -> {
                b = 1;
                y = a;
            });
            one.start();
            other.start();
            one.join();
            other.join();
            String result = "第" + i + "次 (" + x + "," + y + "）";
            if (x == 0 && y == 0) {
                // 出现这个分支，说明指令出现了重排
                // 否则不可能 x和y同时都为0
                System.err.println(result);
                break;
            } else {
                // System.out.println(result);
            }
        }
    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}
```

如上示例，如果指令不出现乱序，那么x和y不可能同时为0，通过执行这个程序可以验证出来，在我本机测试的结果是：

执行到第1425295次 出现了x和y同时为0的情况。



### 原子性

程序的原子性是指整个程序中的所有操作，要么全部完成，要么全部失败，不可能滞留在中间某个环节；在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程所打断。


一个示例：
```java
class T {
    m = 9;
}
```
对象T在创建过程中，背后其实是包含了多条执行语句的，由于有CPU乱序执行的情况，所以极有可能会在初始化过程中生成以一个半初始化对象t，这个t的m等于0（还没有来得及做赋值操作）

所以，不要在某个类的构造方法中启动一个线程，这样会导致this对象逸出，因为这个类的对象可能还来不及执行初始化操作，就启动了一个线程，导致了异常情况。


volatile一方面可以保证线程数据之间的可见性，另外一方面，也可以防止类似这样的指令重排，所以
所以，单例模式中，DCL方式的单例一定要加volatile修饰：

```java
public class Singleton6 {
    private volatile static Singleton6 INSTANCE;

    private Singleton6() {
    }

    public static Singleton6 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton6.class) {
                if (INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    INSTANCE = new Singleton6();
                }
            }
        }
        return INSTANCE;
    }
}
```

具体可以参考[设计模式学习笔记](https://www.cnblogs.com/greyzeng/p/14107751.html) 中单例模式的说明。

## CAS

比较与交换的意思

举个例子：

> 内存有个值是3，如果用Java通过多线程去访问这个数，每个线程都要把这个值+1。

之前是需要加锁，即synchronized关键字来控制。但是JUC的包出现后，有了CAS操作，可以不需要加锁来处理，流程是：

第一个线程：把3拿过来，线程本地区域做计算+1，然后把4写回去，
第二个线程：也把3这个数拿过来，线程本地区域做计算+3后，在回写回去的时候，会做一次比较，如果原来的值还是3，那么说明这个值之前没有被打扰过，就可以把4写回去，如果这个值变了，假设变为了4，那么说明这个值已经被其他线程修改过了，那么第二个线程需要重新执行一次，即把最新的4拿过来继续计算，回写回去的时候，继续做比较，如果内存中的值依然是4，说明没有其他线程处理过，第二个线程就可以把5回写回去了。

流程图如下：
![cas_case](https://img2020.cnblogs.com/blog/683206/202104/683206-20210407160938520-132921153.png)

### ABA问题

CAS会出现一个ABA的问题，即在一个线程回写值的时候，其他线程其实动过那个原始值，只不过其他线程操作后这个值依然是原始值。

如何来解决ABA问题呢？

我们可以通过版本号或者时间戳来控制，比如数据原始的版本是1.0，处理后，我们把这个数据的版本改成变成2.0版本, 时间戳来控制也一样，

以Java为例，AtomicStampedReference这个类，它内部不仅维护了对象值，还维护了一个时间戳。

当AtomicStampedReference对应的数值被修改时，除了更新数据本身外，还必须要更新时间戳。

当AtomicStampedReference设置对象值时，对象值以及时间戳都必须满足期望值，写入才会成功。

因此，即使对象值被反复读写，写回原值，只要时间戳发生变化，就能防止不恰当的写入。

### CAS的底层实现

> Unsafe.cpp-->Atom::cmpxchg-->Atomic_linux_x86_inline.hpp-->调用了汇编的LOCK_IF_MP方法
>
> Multiple_processor

```
lock cmpxchg
```

虽然cmpxchg指令不是原子的，但是加了lock指令后，则cmpxhg被上锁，不允许被打断。 在单核CPU中，无须加lock，在多核CPU中，必须加lock，可以参考stackoverflow上的这个回答:

[is-x86-cmpxchg-atomic-if-so-why-does-it-need-lock](https://stackoverflow.com/questions/27837731/is-x86-cmpxchg-atomic-if-so-why-does-it-need-lock/44273130#44273130)

使用CAS好处

jdk早期是重量级别锁 ，通过0x80中断 进行用户态和内核态转换，所以效率比较低，有了CAS操作，大大提升了效率。



## 思维导图

[processon](https://www.processon.com/view/5ec513425653bb6f2a1f7da8)

## 完整源码

[Github](https://github.com/GreyZeng/juc)


## 参考资料


[多线程与高并发-马士兵](https://ke.qq.com/course/3132461?tuin=b09cbb87)

[实战Java高并发程序设计(第2版)](https://book.douban.com/subject/30358019/)

[深入浅出Java多线程](http://concurrent.redspider.group/RedSpider.html)

[Java并发编程实战](https://book.douban.com/subject/10484692/)

[【并发编程】MESI--CPU缓存一致性协议](https://www.cnblogs.com/z00377750/p/9180644.html)

[【并发编程】细说并发编程的三大特性](https://zhuanlan.zhihu.com/p/274569273)

[设计模式学习笔记](https://www.cnblogs.com/greyzeng/p/14107751.html)