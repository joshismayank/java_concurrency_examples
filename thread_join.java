class ThreadJoin {
	static void printThreadAndMsg (String msg) {
		System.out.println(Thread.currentThread().getName()+" - "+msg);
	}
	private static class Loop implements Runnable {
		public void run() {
			long counter = 0;
			while (true) {
				try {
					printThreadAndMsg("in loop "+counter);
					counter++;
					Thread.sleep(100);
				} catch (Exception ex) {
					printThreadAndMsg("ex - "+ex);
					return;
				}
			}
		}
	}
	public static void main(String args[]) throws InterruptedException {
		int maxWait = 10000;
		long startTime = System.currentTimeMillis();
		printThreadAndMsg("starting");
		Thread t = new Thread(new Loop());
		t.start();
		while (t.isAlive()) {
			System.out.println("trying join");
			t.join(1000);
			System.out.println(System.currentTimeMillis()-startTime);
			if (System.currentTimeMillis()-startTime>maxWait) {
				printThreadAndMsg("interrupting");
				t.interrupt();
			}
		}
		printThreadAndMsg("ending");
	}
}
