/* Solving the Santa Claus Problem (John Trono, 1994) with Java J2SE 5.0
 *
 * (c) 2006 Peter Steiner <peter.steiner@schlau.ch> http://pesche.schlau.ch
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.System.out;

public class SantaClaus {
	private volatile boolean canContinue = true;
	private final Semaphore lock = new Semaphore(0);
	private final static int CURRENT_YEAR = 2024;
	private AtomicInteger year = new AtomicInteger(2020);
	private static Random generator = new Random();

	private final static int NUMBER_OF_REINDEER = 9;
	private final static int NUMBER_OF_ELVES = 10;
	private final static int ELVES_NEEDED_TO_WAKE_SANTA = 3;

	private final Semaphore queueElves;
	private final CyclicBarrier threeElves;
	private final CyclicBarrier allReindeers;
	private final Semaphore santasAttention;
	private final static int LAST_REINDEER = 0; // compares to CyclicBarrier.await()
	private final static int THIRD_ELF = 0; // compares to CyclicBarrier.await()

	class Reindeer implements Runnable {
		int id;

		Reindeer(int id) {
			this.id = id;
		}

		public void run() {
			while (canContinue) {
				try {
					System.out.println("Reindeer " + id + " is on vacation");
					Thread.sleep(900 + generator.nextInt(200));	// wait until christmas comes

					int reindeer = allReindeers.await();	// only all reindeers together can wake Santa
					
					if (reindeer == LAST_REINDEER) {	// the last reindeer to return to North Pole must wake Santa
						santasAttention.acquire();
						out.println("=== Delivery for Christmas " + year + " ===");
						if (year.incrementAndGet() == CURRENT_YEAR) {
							canContinue = false;
							lock.release();
						}
					}

					Thread.sleep(generator.nextInt(20)); // delivering is almost immediate

					if (reindeer == LAST_REINDEER) {
						santasAttention.release();
					}
				} catch (InterruptedException e) {
					// thread interrupted for program cleanup
				} catch (BrokenBarrierException e) {
					// another thread in the barrier was interrupted
				}
			}
			out.println("Reindeer " + id + " retires");
		}
	}

	class Elf implements Runnable {
		int id;

		Elf(int id) {
			this.id = id;
		}

		public void run() {
			try {
				System.out.println("Elf " + id + " is building a toy");
				Thread.sleep(generator.nextInt(2000));

				while (canContinue) {
					System.out.println("Elf " + id + " found a problem");	// no more than three elves fit into Santa's office
					Thread.sleep(1000);
					queueElves.acquire();
					
					int elf = threeElves.await();	// wait until three elves have a problem

					if(elf == THIRD_ELF)	santasAttention.acquire();	// only one elf calls Santa
					
					System.out.println("Elf " + id + " can now continue working");

					if (elf == THIRD_ELF)	santasAttention.release();

					// other elves that ran out of ideas in the meantime
					// may now gather and wake santa again
					queueElves.release();

					// manufacture toys until the inspiration is used up
					Thread.sleep(generator.nextInt(2000));
				}
			} catch (InterruptedException e) {
				// thread interrupted for program cleanup
			} catch (BrokenBarrierException e) {
				// another thread in the barrier was interrupted
			}
			out.println("Elf " + id + " retires");
		}
	}

	class BarrierMessage implements Runnable {
		String msg;

		BarrierMessage(String msg) {
			this.msg = msg;
		}

		public void run() {
			out.println(msg);
		}
	}

	public SantaClaus() {
		santasAttention = new Semaphore(1, true);
		queueElves = new Semaphore(ELVES_NEEDED_TO_WAKE_SANTA, true); // use a fair semaphore
		threeElves = new CyclicBarrier(ELVES_NEEDED_TO_WAKE_SANTA, new BarrierMessage("--- " + ELVES_NEEDED_TO_WAKE_SANTA + " elves are knocking ---"));
		allReindeers = new CyclicBarrier(NUMBER_OF_REINDEER, new Runnable() {
			public void run() {
				//out.println("=== Reindeer reunion for Christmas " + year + " ===");
			}
		});

		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < NUMBER_OF_ELVES; ++i)
			threads.add(new Thread(new Elf(i)));
		for (int i = 0; i < NUMBER_OF_REINDEER; ++i)
			threads.add(new Thread(new Reindeer(i)));
		for (Thread t : threads)
			t.start();

		try {
			// wait until !canContinue
			lock.acquire();
			out.println("Faith has vanished from the world");
			for (Thread t : threads)
				t.interrupt();
			for (Thread t : threads)
				t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		out.println("The End of Santa Claus");
	}

	public static void main(String[] args) {
		new SantaClaus();
	}
}
