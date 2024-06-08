class OrderedPrinting {
	int n;
	int threadCt;
	int a;
	Object lock;

	public OrderedPrinting(int n, int tCt) {
		this.n = n;
		this.threadCt = tCt;
		this.a = 0;
		lock = new Object();
	}

	class Print implements Runnable {
		public void run() {
			while (true) {
				try {
					synchronized (lock) {
						if (a<n) {
							System.out.println(Thread.currentThread().getName()+" - "+a++);
						} else {
							return;
						}
					}
					Thread.sleep(100);
				} catch (Exception ex) {}
			}
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
