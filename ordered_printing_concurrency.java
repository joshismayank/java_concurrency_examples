import java.util.concurrent.atomic.AtomicInteger;

class OrderedPrinting {
	int n;
	int threadCt;
	AtomicInteger i;
	int a;

	public OrderedPrinting(int n, int tCt) {
		this.n = n;
		this.threadCt = tCt;
		this.i = new AtomicInteger(0);
		this.a = 0;
	}

	class Print implements Runnable {
		public void run() {
			try {
				while (i.get()<n) {
					System.out.println(Thread.currentThread().getName()+" - "+i.incrementAndGet());
					Thread.sleep(100);
				}
			} catch (Exception ex) {}
		}
	}

	public void print() {
		for (int i=0;i<threadCt;i++) {
			Print print = new Print();
			new Thread(print).start();
		}
	}
}

class OrderedPrintingTest {
	public static void main(String[] args) {
		OrderedPrinting ordPrint = new OrderedPrinting(10,5);
		ordPrint.print();
	}
}
