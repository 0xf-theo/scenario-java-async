package fr.project.scenario2;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {

    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // Remplacez par l'adresse du serveur RabbitMQ
        return factory.newConnection();
    }

}
