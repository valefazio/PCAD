public class Main {
    public static void main(String[] args) throws InterruptedException {
        //RWbasic rw = new RWbasic();
		RWexclusive rw = new RWexclusive();
		//RW rw = new RW();

        Reader reader = new Reader(rw);
        Writer writer = new Writer(rw);
        Thread[] threads_reader = new Thread[50];
        Thread[] threads_writer = new Thread[50];

        for(int i=0; i<50; i++) {
            threads_writer[i] = new Thread(writer);
            threads_reader[i] = new Thread(reader);
        }
        for(int i=0; i<50; i++) {
            threads_writer[i].start();
            /* threads_writer[i].join();*/
            Thread.sleep(100);
            threads_reader[i].start();
			threads_reader[i].join();
        }        
    }
}
