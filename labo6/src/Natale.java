import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Natale{
    protected final static int N_RENNE = 9;
	protected final static int N_ELFI = 10;
	protected final static int N_ELFI_RICHIESTI = 3;

	protected static boolean rennePronte = false;

	protected final static Lock lock = new ReentrantLock();
	protected final static Condition babboPronto = lock.newCondition();
	protected final static Condition svegliaBabboNatale = lock.newCondition();

	protected final static Semaphore codaElfi = new Semaphore(N_ELFI_RICHIESTI, true);
	protected final static CyclicBarrier elfiXBabboNatale = new CyclicBarrier(N_ELFI_RICHIESTI, new Runnable() {public void run() {}});
	protected final static CyclicBarrier renneXBabboNatale = new CyclicBarrier(N_RENNE, new Runnable() {public void run() {}});
	protected final static Semaphore attenzioneBabboNatale = new Semaphore(1, true);

	public static void main(String[] args) {
		Thread[] elfi = new Thread[N_ELFI];
		Thread[] renne = new Thread[N_RENNE];
		Thread babboNatale = new Thread(new BabboNatale());

		for (int i = 0; i < N_ELFI; i++) {
			elfi[i] = new Thread(new Elfi());
			elfi[i].start();
		}

		for (int i = 0; i < N_RENNE; i++) {
			renne[i] = new Thread(new Renne());
			renne[i].start();
		}

		babboNatale.start();
	}
}
