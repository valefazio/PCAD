public class Elfi extends Natale implements Runnable{
	public void run(){
		while(true) {
			try {
				System.out.println("Elfo " + Thread.currentThread().getId() + " costruisce un giocattolo");
				Thread.sleep(1000);

				System.out.println("Elfo " + Thread.currentThread().getId() + " aspetta Babbo Natale");
				codaElfi.acquire();
				int n_elfo = elfiXBabboNatale.await();

				if (n_elfo == 0) {	// ultimo elfo
					synchronized (svegliaBabboNatale) {
						attenzioneBabboNatale.acquire();
						svegliaBabboNatale.notify();
					}
				}
				synchronized (babboPronto) {
					babboPronto.wait();
				}

				// Elfo sta imparando
				Thread.sleep(3000);
				if(n_elfo == 0)
					attenzioneBabboNatale.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
