package fr.project.scenario3; // Package declaration for the code

import com.rabbitmq.client.Connection; // Importing necessary RabbitMQ connection classes
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets; // Import for character encoding

public class RabbitMQConfig { // Configuration class for RabbitMQ

    public static Connection getConnection() throws Exception { // Method to establish a RabbitMQ connection
        ConnectionFactory factory = new ConnectionFactory(); // Creating a connection factory instance
        factory.setHost("localhost"); // Setting the RabbitMQ server host address (Replace with your RabbitMQ server address)
        return factory.newConnection(); // Returning the connection to RabbitMQ
    }

    public static byte[] msg(String msg) { // Method to convert a string message to a byte array
        return msg.getBytes(StandardCharsets.UTF_8); // Converting the string message to bytes using UTF-8 encoding
    }
}
