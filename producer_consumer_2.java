import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.*;

class Producer implements Runnable {
	int id;
	Queue<String> buffer;
	int buffSize;
	ReentrantLock lock;
	Condition inserted;
	Condition popped;
	Random rand;

	public Producer(int i, Queue<String> buf, int bSize, ReentrantLock l, Condition ins, Condition pop) {
		id = i;
		buffer = buf;
		buffSize = bSize;
		lock = l;
		inserted = ins;
		popped = pop;
		rand = new Random();
	}

	public void run() {
		while (true) {
		try {
			lock.lock();
			if (buffer.size()==buffSize) {
				System.out.println("wait in produce "+id);
				popped.await();
				System.out.println("wait complete in produce "+id);
			}
			produce();
			inserted.signal();
		} catch(Exception ex) {
			System.out.println("error in produce (A) "+id+" - "+ex);
		} finally {
			lock.unlock();
		}
		try {
			Thread.sleep(rand.nextInt(10)*100);
		} catch (Exception ex) {
			System.out.println("error in produce (B) "+id+" - "+ex);
		}
		}
	}

	private void produce() {
		String t = "";
		for (int i=rand.nextInt(10);i>=0;i--) {
			t += i;
		}
		buffer.add(t);
		System.out.println("produced in "+id+" - "+t);
	}
}
class Consumer implements Runnable {
	int id;
	Queue<String> buffer;
	int buffSize;
	ReentrantLock lock;
	Condition inserted;
	Condition popped;
	Random rand;

	public Consumer(int i, Queue<String> buf, int bSize, ReentrantLock l, Condition ins, Condition pop) {
		id = i;
		buffer = buf;
		buffSize = bSize;
		lock = l;
		inserted = ins;
		popped = pop;
		rand = new Random();
	}

	public void run() {
		while (true) {
		try {
			lock.lock();
			if (buffer.isEmpty()) {
				System.out.println("wait in consume "+id);
				inserted.await();
				System.out.println("wait done in consume "+id);
			}
			consume();
			popped.signal();
		} catch(Exception ex) {
			System.out.println("error in consume (A) "+id+" - "+ex);
		} finally {
			lock.unlock();
		}
		try {
			Thread.sleep(rand.nextInt(10)*100);
		} catch (Exception ex) {
			System.out.println("error in consume (B) "+id+" - "+ex);
		}
		}
	}

	private void consume() {
		System.out.println("consumed in "+id+" - "+buffer.remove());
	}
}

class ProducerConsumer {
	List<Producer> producers;
	List<Consumer> consumers;

	public ProducerConsumer (int pCt, int cCt, int buffCt) {
		ReentrantLock lock = new ReentrantLock();
		Condition inserted = lock.newCondition();
		Condition popped = lock.newCondition();
		Queue<String> buffer;
		buffer = new LinkedList<>();
		producers = new ArrayList<>();
		consumers = new ArrayList<>();
		
		for (int i=0;i<pCt;i++) {
			Producer p = new Producer(i, buffer, buffCt, lock, inserted, popped);
			producers.add(p);
		}
		for (int i=0;i<cCt;i++) {
			Consumer c = new Consumer(i, buffer, buffCt, lock, inserted, popped);
			consumers.add(c);
		}
	}

	public void test() {
		for (Producer p: producers) {
			new Thread(p).start();
		}
		for (Consumer c: consumers) {
			new Thread(c).start();
		}
	}
}

class ProducerConsumerTest {
	public static void main(String[] args) {
		ProducerConsumer pc = new ProducerConsumer(7, 3, 5);
		pc.test();
	}
}
