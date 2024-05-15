public class Main {
    public static void main(String[] args) {
        ServerMain.main(args);
        
        //CASE 1: consumer aspetta che ci sia qualcosa nel buffer
        ClientConsumer.main(args);
        ClientProducer.main(args);

        //CASE 2: producer riempie il buffer (2 elementi) e consumer prende il primo
        ClientProducer.main(args);
        ClientProducer.main(args);
        ClientConsumer.main(args);

        //CASE 3: [case])
    }
}
