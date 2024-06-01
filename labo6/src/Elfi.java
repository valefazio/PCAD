public class Elfi extends Natale implements Runnable{
	public void run(){
		while(true) {
			try {
				System.out.println("\t\t\t\t\t\t\t\t\t\tElfo " + Thread.currentThread().getId() + " costruisce un giocattolo");
				Thread.sleep(1000);

				System.out.println("\t\t\t\t\t\t\t\t\t\tElfo " + Thread.currentThread().getId() + " aspetta Babbo Natale");
				codaElfi.acquire();
				int n_elfo = elfiXBabboNatale.await();

				if (n_elfo == 0) {	// ultimo elfo
					lock.lock();
					try {
						attenzioneBabboNatale.acquire();
						svegliaBabboNatale.signal();
					} finally {
						lock.unlock();
					}
				}

				// Elfo sta imparando
				Thread.sleep(3000);
				if(n_elfo == 0)
					attenzioneBabboNatale.release();
				codaElfi.release();
				System.out.println("\t\t\t\t\t\t\t\t\t\t----------Elfo " + Thread.currentThread().getId() + " torna in laboratorio il giocattolo");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
