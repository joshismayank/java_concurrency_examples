import java.util.Random;

class ThreadDemo extends Thread {
	Random rand;
	public ThreadDemo(String name) {
		super(name);
		rand = new Random();
	}
	public void run() {
		System.out.println("starting run of "+Thread.currentThread().getName());
		try {
			for (int i=0;i<5;i++) {
				System.out.println(i+" - sleeping "+Thread.currentThread().getName());
				Thread.sleep(rand.nextInt(500));
				System.out.println(i+" - awake "+Thread.currentThread().getName());
			}
		} catch (Exception ex) {
			System.out.println("exception");
		}
		System.out.println("exiting run of"+Thread.currentThread().getName());
	}
}

class ThreadTest {
	public static void main(String args[]) {
		ThreadDemo threadDemo1 = new ThreadDemo("demo1");
		threadDemo1.start();
		ThreadDemo threadDemo2 = new ThreadDemo("demo2");
		threadDemo2.start();
	}
}
