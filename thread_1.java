class ThreadDemo extends Thread {
	public ThreadDemo() {
		super("ThreadDemo");
	}
	public void run() {
		System.out.println("starting run");
		System.out.println("exiting run");
	}
}

class ThreadTest {
	public static void main(String args[]) {
		ThreadDemo threadDemo = new ThreadDemo();
		threadDemo.start();
	}
}
