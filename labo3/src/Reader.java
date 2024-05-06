public class Reader implements Runnable {
    //private RWbasic rw;
	private RWexclusive rw;
	//private RW rw;

    Reader(/* RWbasic */ RWexclusive /* RW */ rw) {
        this.rw = rw;
    }

    public void run() {
        System.out.println(rw.read());
    }
}
