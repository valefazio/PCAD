public class RWext extends RWbasic {
    boolean readyToWrite = true;
	boolean read = true;

    public synchronized void write() throws InterruptedException {
        try {
            while (readyToWrite == false || !read) {
                wait();
            }
            readyToWrite = false;
            notifyAll();
            int tmp = data;
            tmp++;
            data = tmp;
			read = false;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
		System.out.println("  " + Thread.currentThread().getName() + " ha scritto " + data);
    }

    public synchronized int read() {
        try {
            while (readyToWrite == true || read) {
				System.out.println("    Reader " + Thread.currentThread().getName() + " aspetta");
                wait();
            }
            readyToWrite = true;
			read = true;
            notifyAll();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
		System.out.println("  " + Thread.currentThread().getName() + " ha letto ");
        return data;
    }
}
