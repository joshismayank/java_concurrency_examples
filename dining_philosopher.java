import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class Philosopher implements Runnable {
	int id;
	Semaphore leftStick;
	Semaphore rightStick;
	Random rand;

	public Philosopher(int i, Semaphore l, Semaphore r) {
		id = i;
		leftStick = l;
		rightStick = r;
		rand = new Random();
	}

	private void eat() {
		System.out.println("philosopher "+id+" eating start");
		try {
			Thread.sleep(rand.nextInt(10)*1000);
		} catch (Exception ex) {
			System.out.println("exception in eat for "+id+" "+ex);
		}
		System.out.println("philosopher "+id+" eating end");
	}
	private void think() {
		//System.out.println("philosopher "+id+" thinking start");
		try {
			Thread.sleep(rand.nextInt(10)*10);
		} catch (Exception ex) {
			System.out.println("exception in think for "+id+" "+ex);
		}
		//System.out.println("philosopher "+id+" thinking end");
	}

	public void run () {
		while (true) {
			if (rand.nextInt(2)==1) {
				think();
			} else {
				if (id==0) {
					if (rightStick.tryAcquire()) {
						if (leftStick.tryAcquire()) {
							eat();
							leftStick.release();
						}
						rightStick.release();
					}
				} else {
					if (leftStick.tryAcquire()) {
						if (rightStick.tryAcquire()) {
							eat();
							rightStick.release();
						}
						leftStick.release();
					}
				}
			}
		}
	}
}
class DiningPhilosopher {

	List<Philosopher> philosophers;
	List<Semaphore> sticks;

	public DiningPhilosopher(int ct) {
		philosophers = new ArrayList<>();
		sticks = new ArrayList<>();
		for (int i=0;i<ct;i++) {
			Semaphore s = new Semaphore(1);
			sticks.add(s);
		}
		for (int i=0;i<ct-1;i++) {
			Philosopher p = new Philosopher(i, sticks.get(i), sticks.get(i+1));
			philosophers.add(p);
		}
		Philosopher p = new Philosopher(ct-1, sticks.get(ct-1), sticks.get(0));
		philosophers.add(p);
	}

	public void start() {
		for (Philosopher p: philosophers) {
			new Thread(p).start();
		}
	}

	
}
class DiningPhilosopherTest {
	
	public static void main(String[] args) {
		DiningPhilosopher dp = new DiningPhilosopher(5);
		dp.start();
	}
}
