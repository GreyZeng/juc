package git.snippets.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 写时复制容器 copy on write
 * 多线程环境下，写时效率低，读时效率高
 * 适合写少读多的环境
 * ！！！！ 好像和Vector执行的时间差不多
 *
 * @author zenghui
 * @date 2020/3/24
 */
public class CopyOnWriteListVSVector {
    static final Random random = new Random();
    static List<Integer> list = null;

    private enum TYPE {
        VECTOR, COPYONWRITELIST
    }

    public static List<Integer> choose(TYPE type, ArrayList<Integer> list) {
        switch (type) {
            case VECTOR:
                return new Vector<>(list);
            case COPYONWRITELIST:
                return new CopyOnWriteArrayList<>(list);
            default:
                return new CopyOnWriteArrayList<>(list);
        }
    }

    public static void main(String[] args) {
        useVector();
        useCopyOnWriteList();
    }

    public static void useCopyOnWriteList() {
        System.out.println("use CopyOnWriteList...");
        List<Integer> list = initData(TYPE.COPYONWRITELIST);
        getData(list);
    }

    public static List<Integer> initData(TYPE type) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < (100 * 100000); i++) {
            arrayList.add(i);
        }
        return choose(type, arrayList);
    }

    public static void useVector() {
        System.out.println("use vector...");
        List<Integer> list = initData(TYPE.VECTOR);
        getData(list);
    }

    public static void getData(List<Integer> list) {
        //----------------读数据--------------
        long start2 = System.currentTimeMillis();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    list.get(random.nextInt(9999));
                }
            });
        }
        Arrays.asList(threads).forEach(t -> t.start());
        Arrays.asList(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end2 = System.currentTimeMillis();
        System.out.println("time is :" + (end2 - start2));
    }


}
