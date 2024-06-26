public class RW extends RWbasic {
    boolean readyToWrite = true;

    public synchronized void write() throws InterruptedException {
        try {
            while (readyToWrite == false) {
                wait();
            }
            readyToWrite = false;
            notifyAll();
            int tmp = data;
            tmp++;
            data = tmp;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
		System.out.println("  " + Thread.currentThread().getName() + " ha scritto " + data);
    }

    public synchronized int read() {
        try {
            while (readyToWrite == true) {
				System.out.println("    Reader " + Thread.currentThread().getName() + " aspetta");
                wait();
            }
            readyToWrite = true;
            notifyAll();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
		System.out.println("  " + Thread.currentThread().getName() + " ha letto ");
        return data;
    }
}
