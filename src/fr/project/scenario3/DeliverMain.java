package fr.project.scenario3; // Package declaration for the code

import com.rabbitmq.client.BuiltinExchangeType; // Importing necessary RabbitMQ classes
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets; // Import for character encoding

import static fr.project.scenario3.Constants.*; // Importing constants from the Constants class

public class DeliverMain { // Class responsible for delivery operations

    public static void main(String[] args) throws InterruptedException { // Main method for the delivery process
        Thread thread1 = new Thread(() -> { // Creating a thread to handle delivery process for Deliver1
            try {
                create("Deliver1"); // Initiating the delivery process for 'Deliver1'
            } catch (Exception e) {
                throw new RuntimeException(e); // Throwing an exception in case of an error
            }
        });

        Thread thread2 = new Thread(() -> { // Creating a thread to handle delivery process for Deliver2
            try {
                create("Deliver2"); // Initiating the delivery process for 'Deliver2'
            } catch (Exception e) {
                throw new RuntimeException(e); // Throwing an exception in case of an error
            }
        });

        thread1.start(); // Starting the delivery process for Deliver1
        thread2.start(); // Starting the delivery process for Deliver2

        thread1.join(); // Waiting for the delivery process of Deliver1 to complete
        thread2.join(); // Waiting for the delivery process of Deliver2 to complete
    }

    private static void create(String name) throws Exception { // Method to initiate the delivery process
        Connection connection = RabbitMQConfig.getConnection(); // Establishing a connection to RabbitMQ
        Channel channel = connection.createChannel(); // Creating a channel for communication

        System.out.println("DELIVER [#" + name + "]"); // Displaying a message indicating the delivery process initiation
        channel.exchangeDeclare(EXCHANGE_DELIVER_NAME, BuiltinExchangeType.FANOUT); // Declaring an exchange for delivery
        channel.queueDeclare(QUEUE_ORDER_READY_FOR_DELIVERY_NAME, false, false, false, null); // Declaring a queue for orders ready for delivery
        channel.queueBind(QUEUE_ORDER_READY_FOR_DELIVERY_NAME, EXCHANGE_DELIVER_NAME, PIZZAIOLO_NAMESPACE + ".#"); // Binding the queue to the delivery exchange with the specified namespace

        channel.exchangeDeclare(EXCHANGE_ASSISTANT_NAME, BuiltinExchangeType.FANOUT); // Declaring an exchange for assistants
        channel.queueDeclare(QUEUE_DELIVER_NAME, false, false, false, null); // Declaring a queue for delivery
        channel.queueBind(QUEUE_DELIVER_NAME, EXCHANGE_ASSISTANT_NAME, DELIVER_NAMESPACE + "." + name); // Binding the queue to the assistant exchange with the specified name

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { // Handling incoming deliveries
            byte[] body = delivery.getBody(); // Extracting the message body
            String command = new String(body, StandardCharsets.UTF_8); // Converting the body to a readable string using UTF-8 encoding
            System.out.println(name + ": received command " + command); // Displaying the received command

            try {
                Thread.sleep(2000); // Simulating a process time by pausing for 2 seconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e); // Throwing an exception if interrupted
            }

            channel.basicPublish(EXCHANGE_ASSISTANT_NAME, DELIVER_NAMESPACE + "." + name, null, body); // Publishing the received command to the assistant exchange
        };

        channel.basicConsume(QUEUE_ORDER_READY_FOR_DELIVERY_NAME, true, deliverCallback, consumerTag -> {
            // Consuming messages from the queue, with a callback for handling the received message (empty for this scenario)
        });

        while(true){} // Infinite loop to keep the program running and handling messages continuously
    }
}
