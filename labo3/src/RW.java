public class RW extends RWbasic {
    boolean readyToWrite = true;
	boolean readyToRead = true;

    public  void write() throws InterruptedException {
        while (readyToWrite == false) {}
		readyToWrite = false;
		readyToRead = false;
        int tmp = data;
        tmp++;
        Thread.sleep(1000);
        data = tmp;
        readyToWrite = true;
		readyToRead = true;
        
        /* try {
            while (readyToWrite == false) {
                wait();
            }
            readyToWrite = false;
            notifyAll();
            int tmp = data;
            tmp++;
            Thread.sleep(1000);
            data = tmp;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } */

    }

    public  int read() {
        while(readyToRead == false) {}
		readyToWrite = false;
        int tmp = data;
        readyToWrite=true;
        return tmp;

        /* try {
            while (readyToWrite == true) {
				System.out.println("Reader waiting");
                wait();
            }
            readyToWrite = true;
            notifyAll();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return data; */
    }
}
