package fr.project.scenario2;

import com.rabbitmq.client.*;

public class Livreur implements Runnable {
    @Override
    public void run() {
        try {
            Connection connection = RabbitMQConfig.getConnection();
            Channel channel = connection.createChannel();

            // Écoute la commande prête pour livraison
            channel.queueDeclare("commande_pour_livraison", false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Livreur a reçu : " + message);
                System.out.println("Livraison effectuée pour : " + message);
            };

            // Démarre la consommation
            channel.basicConsume("commande_pour_livraison", true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}