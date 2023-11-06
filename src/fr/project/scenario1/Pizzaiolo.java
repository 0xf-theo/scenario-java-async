package fr.project.scenario1;

import java.util.ArrayList;
import java.util.List;

public record Pizzaiolo(String name, Deliver deliver) {

    public void prepare(Client client, List<Pizza> pizzas) throws InterruptedException {
        var threads = new ArrayList<Thread>();

        for (Pizza pizza : pizzas) {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(name + ": Je vais preparer  la pizza " + pizza.name());
                    Thread.sleep(pizza.preparationTime());
                    System.out.println(name + ": La pizza  " + pizza.name() + " est prête");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(name + ": J'ai terminé la commande de " + client.name());
        deliver.deliver(client);
    }

}
