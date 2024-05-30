import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import static java.lang.System.out;

public class SantaClaus {
    private int count = 0;
    private static Random generator = new Random();

    private final static int NUMBER_OF_REINDEER = 9;
    private final static int NUMBER_OF_ELVES = 10;
    private final static int ELVES_NEEDED_TO_WAKE_SANTA = 3;

    private final Semaphore queueElves;
    private final CyclicBarrier threeElves;
    private final CyclicBarrier allReindeers;
    private final Semaphore santasAttention;
    private final static int LAST_REINDEER = 0;    // compares to CyclicBarrier.await()
    private final static int THIRD_ELF = 0;        // compares to CyclicBarrier.await()

    class Reindeer implements Runnable {
        int id;
        Reindeer(int id) { this.id = id; }

        public void run() {
            while (count < 3) {
                try {
                    System.out.println("Reindeer " + id + " is on vacation");
                    Thread.sleep(1900 + generator.nextInt(200));

                    // only all reindeers together can wake Santa
                    int reindeer = allReindeers.await();
                    // the last reindeer to return to North Pole must wake Santa
                    if (reindeer == LAST_REINDEER) {
                        santasAttention.acquire();
                        out.println("=== Delivery for Christmas " + count + " ===");
						/* int Nextyear = year.incrementAndGet();
						System.out.println("-----------------------------------Next year is " + Nextyear); */
						synchronized(this) {
                        	count++;
						}
                    }
                    santasAttention.release();
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
        Elf(int id) { this.id = id; }

        public void run() {
            try {
				System.out.println("Elf " + id + " builds a toy");
                Thread.sleep(generator.nextInt(3000));

                while (count < 3) {
                    queueElves.acquire();
                    out.println("Elf " + id + " has a problem");

                    int elf = threeElves.await();	// wait until three elves have a problem

                    // the third elf wake Santa
                    if (elf == THIRD_ELF)
                        santasAttention.acquire();
					out.println("Santa is helping Elf " + id);

                    if (elf == THIRD_ELF)
                        santasAttention.release();
                    queueElves.release();
                }
            } catch (InterruptedException e) {
                // thread interrupted for program cleanup
				System.out.println("Elf " + id + " is interrupted 1");
            } catch (BrokenBarrierException e) {
                // another thread in the barrier was interrupted
				System.out.println("Elf " + id + " is interrupted 2");
            }
            out.println("Elf " + id + " retires");
        }
    }

    class BarrierMessage implements Runnable {
        String msg;
        BarrierMessage(String msg) { this.msg = msg; }
        public void run() {
            out.println(msg);
        }
    }

    public SantaClaus() {
        // use a fair semaphore for Santa to prevent that a second group of elves might get Santas attention first if the reindeer are
        // waiting and Santa is consulting with a first group of elves.
        santasAttention = new Semaphore(1, true);

        queueElves = new Semaphore(ELVES_NEEDED_TO_WAKE_SANTA, true);    // use a fair semaphore
        threeElves = new CyclicBarrier(ELVES_NEEDED_TO_WAKE_SANTA, new BarrierMessage("--- " + ELVES_NEEDED_TO_WAKE_SANTA + " elves are knocking ---"));
        allReindeers = new CyclicBarrier(NUMBER_OF_REINDEER, new Runnable() {
            public void run() {
                out.println("=== Reindeer reunion for Christmas " + count +" ===");
            }});

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < NUMBER_OF_ELVES; ++i)
            threads.add(new Thread(new Elf(i)));
        for (int i = 0; i < NUMBER_OF_REINDEER; ++i)
            threads.add(new Thread(new Reindeer(i)));
        //out.println("Once upon in the year " + year + " :");
        for (Thread t : threads)
            t.start();

        try {
			//mutex.acquire();
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
