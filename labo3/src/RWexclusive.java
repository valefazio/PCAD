public class RWexclusive extends RWbasic{
    public synchronized void write() throws InterruptedException {
		int tmp = data;
		tmp++;
		data = tmp;
		System.out.println("-" + Thread.currentThread().getName() + " ha scritto " + data);
	}

	public synchronized int read() {
		System.out.println("-" + Thread.currentThread().getName() + " ha letto ");
		return data;
	}
}
