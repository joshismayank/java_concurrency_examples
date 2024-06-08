import java.util.Random;

class RunnableDemo implements Runnable {
	public void run() {
		Random rand = new Random();
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

class RunnableTest {
	public static void main(String args[]) {
		RunnableDemo runnableDemo1 = new RunnableDemo();
		new Thread(runnableDemo1).start();
		RunnableDemo runnableDemo2 = new RunnableDemo();
		new Thread(runnableDemo2).start();
	}
}
