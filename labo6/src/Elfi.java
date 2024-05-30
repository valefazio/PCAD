import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Elfi {
    private final static int nElfi = 9;
	private final static int elfiPerVolta = 3;
	protected static Barrier elfiXBabboNatale = new Barrier(elfiPerVolta);
	protected static LinkedList<Thread> elfiInAttesa = new LinkedList<Thread>();
	
	public void run() throws InterruptedException{
		while(true) {
			System.out.println("Elfo " + Thread.currentThread().getId() + " costruisce un giocattolo");
			Thread.sleep(1000);
			System.out.println("Elfo " + Thread.currentThread().getId() + " ha trovato un problema e va da Babbo Natale");
			//prendere un ticket

			elfiXBabboNatale.awaitInList(elfiInAttesa);
			System.out.println("Elfo " + Thread.currentThread().getId() + " ha svegliato Babbo Natale");
			//Babbo Natale aggiusta il giocattolo
			System.out.println("Elfo " + Thread.currentThread().getId() + " torna a costruire giocattoli");
			Thread.sleep(1000);
		}
	}
}
