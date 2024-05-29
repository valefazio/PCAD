import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Nuotatore {
    private static final int Ns = 2;
	private static final int Nc = 2;
	private static final Semaphore sem_spogliatoi = new Semaphore(Ns);
	private static final Semaphore sem_armadietti = new Semaphore(Nc);

	public void entraSpogliatoio() throws InterruptedException {
		sem_spogliatoi.acquire();
		//System.out.println("Nuotatore " + Thread.currentThread().getId() + " è entrato nello spogliatoio");
	}

	public void esceSpogliatoio() {
		System.out.println("( ) Nuotatore " + Thread.currentThread().getId() + " è uscito dallo spogliatoio");
		sem_spogliatoi.release();
	}

	public void entraArmadietto() throws InterruptedException {
		sem_armadietti.acquire();
		//System.out.println("Nuotatore " + Thread.currentThread().getId() + " ha preso un armadietto");
	}

	public void esceArmadietto() {
		System.out.println("( ) Nuotatore " + Thread.currentThread().getId() + " ha liberato l'armadietto");
		sem_armadietti.release();
	}

    public void start() {
		new Thread(() -> {
			try {
				entraSpogliatoio();
				System.out.println("(a) Nuotatore " + Thread.currentThread().getId() + " prende la chiave dello spogliatoio");

				/* entraArmadietto();
				System.out.println("(b) Nuotatore " + Thread.currentThread().getId() + " prende la chiave dell'armadietto");
				System.out.println("(e) Nuotatore " + Thread.currentThread().getId() + " mette i suoi vestiti nell'armadietto");
				Thread.sleep(1000); */

				System.out.println("(c) Nuotatore " + Thread.currentThread().getId() + " si cambia nello spogliatoio");
				Thread.sleep(1000);
				System.out.println("(d) Nuotatore " + Thread.currentThread().getId() + " libera lo spogliatoio");
				Thread.sleep(1000);

				esceSpogliatoio();

				//Risoluzione del deadlock
				entraArmadietto();
				System.out.println("(b) Nuotatore " + Thread.currentThread().getId() + " prende la chiave dell'armadietto");
				System.out.println("(e) Nuotatore " + Thread.currentThread().getId() + " mette i suoi vestiti nell'armadietto");
				Thread.sleep(1000);

				System.out.println("(g) Nuotatore " + Thread.currentThread().getId() + " nuota");
				Thread.sleep(1000);
//DEADLOCK QUI
				entraSpogliatoio();
				System.out.println("(h) Nuotatore " + Thread.currentThread().getId() + " prende la chiave dello spogliatoio");

				System.out.println("(i) Nuotatore " + Thread.currentThread().getId() + " recupera i suoi vestiti dall'armadietto");
				Thread.sleep(1000);
				System.out.println("(j) Nuotatore " + Thread.currentThread().getId() + " si cambia nello spogliatoio");
				Thread.sleep(1000);

				esceSpogliatoio();
				esceArmadietto();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		Nuotatore nuotatore = new Nuotatore();
		for (int i = 0; i < 6; i++) {
			nuotatore.start();
		}
	}
}
