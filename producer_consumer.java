import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class ProducerConsumer {
	Queue<Integer> buffer;
	int size;
	ReentrantLock lock;
	Random rand;

	public ProducerConsumer(int size) {
		buffer = new LinkedList<>();
		this.size = size;
		lock = new ReentrantLock();
		rand = new Random();
	}

	class Producer implements Runnable {
		public void run() {
			while (true) {
				try {
					lock.lock();
					if (buffer.size()<size) {
						int p = rand.nextInt(1000);
						buffer.add(p);
						System.out.println("produced = "+p);
					}
				} finally {
					lock.unlock();
				}
				try {
					Thread.sleep(rand.nextInt(10)*100);
				} catch (Exception ex) {}
			}
		}
	}

	class Consumer implements Runnable {
		public void run() {
			while (true) {
				try {
					lock.lock();
					if (!buffer.isEmpty()) {
						System.out.println("consumed - "+buffer.remove());
					}
				} finally {
					lock.unlock();
				}
				try {
					Thread.sleep(rand.nextInt(10)*100);
				} catch (Exception ex) {}
			}
		}
	}

	public void test() {
		Consumer cons = new Consumer();
		new Thread(cons).start();
		Producer prod = new Producer();
		new Thread(prod).start();
	}
}
class ProducerConsumerTest {
	public static void main(String[] args) {
		ProducerConsumer prodCons = new ProducerConsumer(5);
		prodCons.test();
	}
}
