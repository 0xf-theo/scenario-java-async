package fr.project.scenario2;

public class Main {
    public static void main(String[] args) throws Exception {
        new Thread(new Assistant()).start();
        new Thread(new Assistant()).start();
        new Thread(new Cuisinier()).start();
        new Thread(new Cuisinier()).start();
        new Thread(new Cuisinier()).start();
        new Thread(new Livreur()).start();
        new Thread(new Livreur()).start();

        Client.start();
    }
}
