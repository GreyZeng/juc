package git.snippets.juc;

import org.openjdk.jol.info.ClassLayout;

/**
 * 对象的内存布局
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class ObjectModel {
    public static void main(String[] args) {
        T o = new T();
        String s = ClassLayout.parseInstance(o).toPrintable();
        System.out.println(s);
    }
}
class  T{
    public int a = 3;
    public long b = 3l;
}
