class ThreadDemo extends Thread {
	public ThreadDemo(String name) {
		super(name);
	}
	public void run() {
		System.out.println("starting run of "+Thread.currentThread().getName());
		try {
			for (int i=0;i<5;i++) {
				System.out.println(i+" - sleeping");
				Thread.sleep(50);
				System.out.println(i+" - awake");
			}
		} catch (Exception ex) {
			System.out.println("exception");
		}
		System.out.println("exiting run of"+Thread.currentThread().getName());
	}
}

class ThreadTest {
	public static void main(String args[]) {
		ThreadDemo threadDemo = new ThreadDemo("demo");
		threadDemo.start();
	}
}
