package fr.project.scenario3; // Déclaration du package pour le code

import com.rabbitmq.client.BuiltinExchangeType; // Importation de classes spécifiques de RabbitMQ
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException; // Importation de classes Java standards
import java.nio.charset.StandardCharsets;

import static fr.project.scenario3.Constants.*; // Importation des constantes du même package

public class AssistantMain { // Définition de la classe principale

    public static void main(String[] args) throws Exception { // Définition de la méthode principale
        Thread thread1 = new Thread(() -> { // Création d'un thread 1 pour exécuter une tâche
            try {
                create("Assistant1"); // Appel de la méthode create avec un argument spécifique
            } catch (Exception e) {
                throw new RuntimeException(e); // Lancement d'une exception en cas d'erreur
            }
        });

        Thread thread2 = new Thread(() -> { // Création d'un thread 2 pour exécuter une autre tâche
            try {
                create("Assistant2"); // Appel de la méthode create avec un autre argument spécifique
            } catch (Exception e) {
                throw new RuntimeException(e); // Lancement d'une exception en cas d'erreur
            }
        });

        thread1.start(); // Démarrage de l'exécution du thread 1
        thread2.start(); // Démarrage de l'exécution du thread 2

        thread1.join(); // Attend que le thread 1 se termine
        thread2.join(); // Attend que le thread 2 se termine
    }

    private static void create(String name) throws Exception { // Définition de la méthode create
        Connection connection = RabbitMQConfig.getConnection(); // Établissement d'une connexion à RabbitMQ
        Channel channel = connection.createChannel(); // Création d'un canal de communication

        System.out.println("ASSISTANT [#" + name + "]"); // Affichage d'un message dans la console
        channel.exchangeDeclare(Constants.EXCHANGE_CLIENT_NAME, BuiltinExchangeType.FANOUT); // Déclaration d'un échange RabbitMQ
        channel.queueDeclare(Constants.QUEUE_HANDLE_CLIENT_NAME, false, false, false, null); // Déclaration d'une file d'attente dans le canal
        channel.queueBind(Constants.QUEUE_HANDLE_CLIENT_NAME, Constants.EXCHANGE_CLIENT_NAME, Constants.COMMAND_NAMESPACE + ".#"); // Liaison de la file d'attente à un échange

        channel.exchangeDeclare(EXCHANGE_PIZZAIOLO_NAME, BuiltinExchangeType.FANOUT); // Déclaration d'un autre échange
        channel.queueDeclare(QUEUE_CLIENT_HANDLED_NAME, false, false, false, null); // Déclaration d'une autre file d'attente
        channel.queueBind(QUEUE_CLIENT_HANDLED_NAME, EXCHANGE_PIZZAIOLO_NAME, ASSISTANT_NAMESPACE + "." + name); // Liaison de la file d'attente à un autre échange avec un nom spécifique

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { // Définition de l'action en cas de réception de message
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8); // Conversion du message reçu en chaîne de caractères
            System.out.println(name + ": received command: '" + message); // Affichage du message reçu dans la console

            try {
                Thread.sleep(5000); // Mise en pause du thread pendant 5000 millisecondes (5 secondes)
            } catch (InterruptedException e) {
                e.printStackTrace(); // Gestion de l'exception
            }

            try {
                channel.basicPublish(EXCHANGE_PIZZAIOLO_NAME, ASSISTANT_NAMESPACE + "." + name, null, RabbitMQConfig.msg(message)); // Publication d'un message vers un échange
            } catch (IOException e) {
                e.printStackTrace(); // Gestion de l'exception
            }
        };

        channel.basicConsume(Constants.QUEUE_HANDLE_CLIENT_NAME, true, deliverCallback, consumerTag -> {}); // Consommation des messages provenant d'une file d'attente

        while(true){} // Boucle infinie pour maintenir le programme en cours d'exécution
    }
}
