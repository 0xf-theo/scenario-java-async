package fr.project.scenario2;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class Cuisinier implements Runnable {
    @Override
    public void run() {
        try {
            Connection connection = RabbitMQConfig.getConnection();
            Channel channel = connection.createChannel();

            // Écoute la commande créée
            channel.queueDeclare("commande_creee", false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Cuisinier a reçu : " + message);

                // Prépare la commande et l'envoie à la file "commande prête pour livraison"
                channel.basicPublish("commande_pour_livraison", "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("Commande prête pour livraison : " + message);
            };

            // Démarre la consommation
            channel.basicConsume("commande_creee", true, deliverCallback, consumerTag -> {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}