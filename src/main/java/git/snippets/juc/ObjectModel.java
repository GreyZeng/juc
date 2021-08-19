package git.snippets.juc;

import org.openjdk.jol.info.ClassLayout;

/**
 * 对象的内存布局
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
// 配置VM参数 -XX:+UseCompressedClassPointers
public class ObjectModel {
    public static void main(String[] args) {
        System.out.println("======T1=======");
        T1 o = new T1();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);
        System.out.println("======T1=======");
        System.out.println("======T2=======");
        T2 o1 = new T2();
        String s1 = ClassLayout.parseInstance(o1).toPrintable();
        System.out.println(s1);
        System.out.println("======T2=======");
    }
    static class  T1{
        public int a = 3;
    }
    static class T2{
        public int a = 3;
        public long b = 3L;
    }
}

