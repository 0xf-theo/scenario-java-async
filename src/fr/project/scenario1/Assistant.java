package fr.project.scenario1;

import java.util.concurrent.CompletableFuture;

public record Assistant(String name) {
    public synchronized void handle(Client client, Pizzaiolo pizzaiolo) throws InterruptedException {
        System.out.println("CALL CENTER: Réception appel du client " + client.name());
        Thread.sleep(1000);
        System.out.println(name + ": décroche l'appel de  " + client.name());
        System.out.println(name + ": demande le choix au client " + client.name());
        this.menus(client, pizzaiolo);
    }

    public void menus(Client client, Pizzaiolo pizzaiolo) throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            if(client.waitForMenus()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.print(client.pizzas() + ": ma commande : " + client.pizzas());
            System.out.println();
            System.out.println(name + ": commande de  " + client.name() + " validée");
            try {
                pizzaiolo.prepare(client, client.pizzas());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
