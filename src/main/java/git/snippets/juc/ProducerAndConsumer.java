package git.snippets.juc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 写一个固定容量的同步容器，拥有put和get方法，以及getCount方法，能够支持2个生产者线程以及10个消费者线程的阻塞调用。
public class ProducerAndConsumer {
	public static void main(String[] args) {

	}
}

class MyContainer {
	List<Object> list = null;
	int limit;

	MyContainer(int limit) {
		this.limit = limit;
		list = Collections.synchronizedList(new ArrayList<>());
	}

	synchronized int getCount() {
		return list.size();
	}

	// index 从0开始计数
	synchronized Object get(int index) {
		if (index >= list.size() || index <= 0) {
			System.out.println("can not get out of range, error index:" + index);
			return null;
		}
		Object removed = list.remove(index);
		System.out.println("removed the " + index + " object");
		return removed;
	}

	synchronized void put(Object data) {
		if (list.size() >= limit) {
			System.out.println("container is full , do not add any more");
			return;
		}
		list.add(data);
		System.out.println("an object added, list size is" + getCount());
	}
}
