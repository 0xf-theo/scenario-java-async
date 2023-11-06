package fr.project.scenario3; // Déclaration du package pour le code

import com.rabbitmq.client.*; // Importation des classes de RabbitMQ

import java.io.IOException; // Importation de classes Java standards pour la gestion des exceptions
import java.nio.charset.StandardCharsets; // Importation pour le codage des caractères

public class ClientMain { // Définition de la classe principale

    public static void main(String[] args) throws Exception { // Méthode principale du programme
        Connection connection = RabbitMQConfig.getConnection(); // Établissement d'une connexion à RabbitMQ
        Channel channel = connection.createChannel(); // Création d'un canal de communication

        channel.exchangeDeclare(Constants.EXCHANGE_CLIENT_NAME, BuiltinExchangeType.FANOUT); // Déclaration d'un échange pour les clients
        channel.queueDeclare(Constants.QUEUE_HANDLE_CLIENT_NAME, false, false, false, null); // Déclaration d'une file d'attente pour les clients
        channel.queueBind(Constants.QUEUE_HANDLE_CLIENT_NAME, Constants.EXCHANGE_CLIENT_NAME, Constants.COMMAND_NAMESPACE + ".#"); // Liaison de la file d'attente à l'échange pour les commandes des clients

        channel.exchangeDeclare(Constants.EXCHANGE_ASSISTANT_NAME, BuiltinExchangeType.FANOUT); // Déclaration d'un échange pour les assistants
        channel.queueDeclare(Constants.QUEUE_DELIVER_NAME, false, false, false, null); // Déclaration d'une file d'attente pour les livraisons
        channel.queueBind(Constants.QUEUE_DELIVER_NAME, Constants.EXCHANGE_ASSISTANT_NAME, Constants.DELIVER_NAMESPACE + ".#"); // Liaison de la file d'attente à l'échange pour les livraisons des assistants

        new ClientBuilder() // Création d'un objet de type ClientBuilder pour créer des clients
                .repeat(4) // Définir le nombre de répétitions pour créer les clients
                .build() // Construction des clients
                .forEach(client -> { // Pour chaque client créé
                    var msg = client.name() + "#" + String.join(",", client.pizzas().stream().map(p -> p.name() + "<>" + p.preparationTime()).toList()); // Construction d'un message à envoyer
                    try {
                        channel.basicPublish(Constants.EXCHANGE_CLIENT_NAME, Constants.COMMAND_NAMESPACE + "." + client.name(), null, RabbitMQConfig.msg(msg)); // Publication du message pour chaque client vers l'échange client
                    } catch (IOException e) {
                        throw new RuntimeException(e); // Gestion de l'exception en cas d'erreur lors de la publication
                    }
                });

        channel.basicConsume(Constants.QUEUE_DELIVER_NAME, true, Constants.QUEUE_DELIVER_NAME, new DefaultConsumer(channel) { // Consommation des messages provenant de la file d'attente de livraisons
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) { // Méthode pour gérer la réception des messages de livraison
                String command = new String(body, StandardCharsets.UTF_8); // Conversion du message reçu en chaîne de caractères
                System.out.println("Command " + command + " delivered !"); // Affichage du message de livraison
            }
        });

        System.in.read(); // Attente de l'entrée utilisateur pour maintenir le programme en cours d'exécution
    }
}
