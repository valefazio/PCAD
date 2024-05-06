public class Writer implements Runnable{
    //private RWbasic rw;
    private RWexclusive rw;
    //private RW rw;

    Writer(/* RWbasic */ RWexclusive /* RW */ rw) {
        this.rw = rw;
    }

    public void run() {
        try {
            rw.write();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }    
}