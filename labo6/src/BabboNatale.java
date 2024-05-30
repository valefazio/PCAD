public class BabboNatale implements Runnable{
    public void run(){
        while (true) {
            System.out.println("Babbo Natale dorme");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Babbo Natale parte per la consegna dei regali con le renne");
        }
    }
}
