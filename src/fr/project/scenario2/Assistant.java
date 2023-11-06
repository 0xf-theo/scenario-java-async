package fr.project.scenario2;

import com.rabbitmq.client.*;

public class Assistant implements Runnable {
    @Override
    public void run() {
        try {
            Connection connection = RabbitMQConfig.getConnection();
            Channel channel = connection.createChannel();

            // Écoute la demande de prise en charge
            channel.queueDeclare("demande_de_prise_en_charge", false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Assistant a reçu : " + message);

                // Traite la commande et l'envoie à la file "commande créée"
                channel.basicPublish("commande_creee", "", null, message.getBytes("UTF-8"));
                System.out.println("Commande créée envoyée : " + message);
            };

            // Démarre la consommation
            channel.basicConsume("demande_de_prise_en_charge", true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}