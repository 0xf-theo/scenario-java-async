package fr.project.scenario1;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        var client1 = new Client("Client1", List.of(
                new Pizza("Margarita", 1000),
                new Pizza("Calzone", 2000)
        ), true);

        var client2 = new Client("Client2", List.of(
                new Pizza("Calzone", 2000)
        ), false);

        var assistant = new Assistant("Assistant1");
        var deliver = new Deliver("Livreur1");
        var pizzaiolo = new Pizzaiolo("Cuisinier", deliver);

        Thread thread1 = new Thread(() -> {
            try {
                assistant.handle(client1, pizzaiolo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                assistant.handle(client2, pizzaiolo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.in.read();
    }

}
