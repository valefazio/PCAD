import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
	private final int vinit;
	private int val;
	private final Lock lock = new ReentrantLock();
	private final Condition varcond = lock.newCondition();

	public Barrier(int vinit) {
		this.vinit = vinit;
		this.val = 0;
	}

	public void await() throws InterruptedException {
		lock.lock();
		try {
			val++;
			if (val == vinit) {
				val = 0;
				varcond.signalAll();
				notifyAll();
			} else {
				varcond.await();
			}
		} finally {
			lock.unlock();
		}
	}

	public void awaitInList(LinkedList<Thread> l) throws InterruptedException {
		lock.lock();
		try {
			val++;
			if (val == vinit) {
				val = 0;
				lock.unlock();
				for (int i = 0; i < vinit; i++)
					l.removeFirst().notify();

			} else {
				l.add(Thread.currentThread()); // metto l'elfo in list d'attesa
				lock.unlock();
				wait();
			}
		} finally {
			lock.unlock();
		}
	}
}
