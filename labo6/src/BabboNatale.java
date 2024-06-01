public class BabboNatale extends Natale implements Runnable {
    public void run() {
        while (true) {
            lock.lock();
            try {
                System.out.println("===== Babbo Natale sta dormendo =====");
                svegliaBabboNatale.await();
                if (rennePronte) {
                    System.out.println("===== Babbo Natale parte con le renne");
                    Thread.sleep(2000);
                    babboPronto.signalAll();
					rennePronte = false;
                    System.out.println("===== Babbo Natale ha consegnato i regali");
                } else {
                    System.out.println("===== Babbo Natale apre a tre elfi");
                    Thread.sleep(2000);
                    babboPronto.signalAll();
                    System.out.println("===== Babbo Natale ha aiutato tre elfi");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
