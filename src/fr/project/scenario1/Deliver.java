package fr.project.scenario1;

public record Deliver(String name) {

    public void deliver(Client client) throws InterruptedException {
        System.out.println(name + ": Je vais livrer " + client.name());
        Thread.sleep(1000);
        System.out.println(name + ": J'ai livré " + client.name());
    }

}
