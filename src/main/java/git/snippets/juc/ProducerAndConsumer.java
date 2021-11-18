package git.snippets.juc;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// 写一个固定容量的同步容器，拥有put和get方法，以及getCount方法，能够支持2个生产者线程以及10个消费者线程的阻塞调用。
public class ProducerAndConsumer {
	public static void main(String[] args) {
		MyContainerByCondition container = new MyContainerByCondition(100);
		for (int i = 0; i < 25; i++) {
			new Thread(container::get).start();
		}
		for (int i = 0; i < 20; i++) {
			new Thread(() -> container.put(new Object())).start();
		}
	}
}

// 使用ReentrantLock的Condition
class MyContainerByCondition {
	static ReentrantLock lock = new ReentrantLock();
	final int MAX;
	private final LinkedList<Object> list = new LinkedList<>();
	Condition consumer = lock.newCondition();
	Condition producer = lock.newCondition();

	public MyContainerByCondition(int limit) {
		this.MAX = limit;
	}

	public void put(Object object) {
		lock.lock();
		try {
			while (getCount() == MAX) {
				System.out.println("container is full");
				try {
					producer.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.add(object);
			consumer.signalAll();
			System.out.println("contain add a object, current size " + getCount());

		} finally {
			lock.unlock();
		}

	}

	public Object get() {
		lock.lock();
		try {
			while (getCount() == 0) {
				try {
					System.out.println("container is empty");
					consumer.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Object object = list.removeFirst();
			producer.signalAll();

			System.out.println("contain get a object, current size " + getCount());
			return object;
		} finally {
			lock.unlock();
		}

	}

	public synchronized int getCount() {
		return list.size();
	}
}

// 使用synchronized的wait和notifyAll
class MyContainerByNotifyAndWait {
	LinkedList<Object> list = null;
	final int limit;

	MyContainerByNotifyAndWait(int limit) {
		this.limit = limit;
		list = new LinkedList<>();
	}

	synchronized int getCount() {
		return list.size();
	}

	// index 从0开始计数
	synchronized Object get() {
		while (list.size() == 0) {
			System.out.println("container is empty");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Object o = list.removeFirst();
		
		System.out.println("get a data");
		this.notifyAll();
		return o;
	}

	synchronized void put(Object data) {
		while (list.size() > limit) {
			System.out.println("container is full , do not add any more");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		list.add(data);
		
		System.out.println("add a data");
		this.notifyAll();
	}
}
