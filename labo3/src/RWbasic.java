public class RWbasic {
    protected int data = 0;

	public void write() throws InterruptedException {
		int tmp = data;
		tmp++;
        Thread.sleep(1000);
		data = tmp;
		System.out.println("  " + Thread.currentThread().getName() + " ha scritto " + data);
	}

	public int read() {
		System.out.println("  " + Thread.currentThread().getName() + " ha letto ");
		return data;
	}
}
