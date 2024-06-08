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
				} catch (Exception ex) {
					printThreadAndMsg("ex - "+ex);
					return;
				}
			}
		}
	}
	public static void main(String args[]) throws InterruptedException {
		int maxWait = 10;
		long startTime = System.currentTimeMillis();
		printThreadAndMsg("starting");
		Thread t = new Thread(new Loop());
		t.start();
		while (t.isAlive()) {
			System.out.println("trying join");
			t.join(1);
			System.out.println(System.currentTimeMillis()-startTime);
			if (System.currentTimeMillis()-startTime>maxWait) {
				printThreadAndMsg("interrupting");
				t.interrupt();
			}
		}
		printThreadAndMsg("ending");
	}
}
