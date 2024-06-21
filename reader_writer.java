import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.*;

class Reader implements Runnable {
	List<String> s;
	Lock lock;
	int id;
	Random rand;
	public Reader (int i, List<String> strs, Lock l) {
		s = strs;
		lock = l;
		id = i;
		rand = new Random();
	}
	private void read(int pos) {
		System.out.println("read in "+id+" "+s.get(pos));
	}
	public void run() {
		while (true) {
			try {
				lock.lock();
				if (s.size()>0) {
					read(rand.nextInt(s.size()));
				}
			} finally {
				lock.unlock();
			}
			try {
				Thread.sleep(rand.nextInt(10)*100);
			} catch (Exception ex) {
				System.out.println("exception in read - "+id+" "+ex);
			}
		}
	}
}
class Writer implements Runnable {
	int id;
	List<String> s;
	Lock lock;
	Random rand;
	public Writer (int i, List<String> strs, Lock l) {
		id = i;
		s = strs;
		lock = l;
		rand = new Random();
	}
	private void write() {
		String t = "";
		for (int i=rand.nextInt(10);i>=0;i--) {
			t += i;
		}
		s.add(t);
		System.out.println("write in "+id+" "+t);
	}
	public void run() {
		rand = new Random();
		while (true) {
			try {
				lock.lock();
				write();
			} finally {
				lock.unlock();
			}
			try {
				Thread.sleep(rand.nextInt(10)*100);
			} catch (Exception ex) {
				System.out.println("exception in write - "+id+" "+ex);
			}
		}
	}
}
class ReaderWriter {
	List<Reader> readers;
	List<Writer> writers;
	List<String> s;
	ReentrantReadWriteLock l;
	Lock rL, wL;

	public ReaderWriter(int rCt, int wCt) {
		s = new ArrayList<>();
		l = new ReentrantReadWriteLock();
		rL = l.readLock();
		wL = l.writeLock();
		readers = new ArrayList<>();
		writers = new ArrayList<>();
		for (int i=0;i<rCt;i++) {
			Reader r = new Reader(i, s, rL);
			readers.add(r);
		}
		for (int i=0;i<wCt;i++) {
			Writer w = new Writer(i, s, wL);
			writers.add(w);
		}
	}

	public void test() {
		for (Writer w: writers) {
			new Thread(w).start();
		}
		for (Reader r: readers) {
			new Thread(r).start();
		}
	}
}
class ReaderWriterTest {
	public static void main(String[] args) {
		ReaderWriter rw = new ReaderWriter(5, 2);
		rw.test();
	}
}
