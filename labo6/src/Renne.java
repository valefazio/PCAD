import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Renne extends Natale implements Runnable {
    int n_renna;
	private static Random generator = new Random();

    public void run() {
        while (true) {
            try {
                System.out.println("Renna " + Thread.currentThread().getId() + " va in vacanza");
                Thread.sleep(1000 + generator.nextInt(2000));

                System.out.println("Renna " + Thread.currentThread().getId() + " è tornata e aspetta");
                n_renna = renneXBabboNatale.await();

                if (n_renna == 0) { // ultima renna
                    lock.lock();
                    try {
                        rennePronte = true;
                        attenzioneBabboNatale.acquire();
                        svegliaBabboNatale.signal();
                    } finally {
                        lock.unlock();
                    }
                }
				
                /* lock.lock();
                try {
                    babboPronto.await();
                } finally {
                    lock.unlock();
                } */

                // Renne in distribuzione pacchi
                Thread.sleep(3000);
                if(n_renna == 0) {
                    attenzioneBabboNatale.release();
					System.out.println("Renne hanno consegnato i regali");
				}
	        } catch (InterruptedException | BrokenBarrierException e) {
            	e.printStackTrace();
            }

        }
    }
}
