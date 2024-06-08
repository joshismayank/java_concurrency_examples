class ThreadDemo extends Thread {
	public ThreadDemo(String name) {
		super(name);
	}
	public void run() {
		System.out.println("starting run of "+Thread.currentThread().getName());
		System.out.println("exiting run of"+Thread.currentThread().getName());
	}
}

class ThreadTest {
	public static void main(String args[]) {
		ThreadDemo threadDemo = new ThreadDemo("demo");
		threadDemo.start();
	}
}
