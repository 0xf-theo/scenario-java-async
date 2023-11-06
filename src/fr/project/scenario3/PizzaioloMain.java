package fr.project.scenario3; // Package declaration for the code

import com.rabbitmq.client.BuiltinExchangeType; // Importing necessary RabbitMQ classes
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import fr.project.scenario1.Pizza; // Importing the Pizza class from another package

import java.nio.charset.StandardCharsets; // Import for character encoding
import java.util.ArrayList;
import java.util.Arrays;
import static fr.project.scenario3.Constants.*; // Importing constants from the Constants class

public class PizzaioloMain { // Class responsible for the pizza maker (Pizzaiolo)

    public static void main(String[] args) throws Exception { // Main method for pizza maker operations
        Thread thread1 = new Thread(() -> { // Creating a thread for the first pizza maker
            try {
                create("Cuisinier1"); // Initiating the pizza making process for 'Cuisinier1'
            } catch (Exception e) {
                throw new RuntimeException(e); // Throwing an exception in case of an error
            }
        });

        Thread thread2 = new Thread(() -> { // Creating a thread for the second pizza maker
            try {
                create("Cuisinier2"); // Initiating the pizza making process for 'Cuisinier2'
            } catch (Exception e) {
                throw new RuntimeException(e); // Throwing an exception in case of an error
            }
        });

        Thread thread3 = new Thread(() -> { // Creating a thread for the third pizza maker
            try {
                create("Cuisinier3"); // Initiating the pizza making process for 'Cuisinier3'
            } catch (Exception e) {
                throw new RuntimeException(e); // Throwing an exception in case of an error
            }
        });

        thread1.start(); // Starting the pizza making process for 'Cuisinier1'
        thread2.start(); // Starting the pizza making process for 'Cuisinier2'
        thread3.start(); // Starting the pizza making process for 'Cuisinier3'

        thread1.join(); // Waiting for 'Cuisinier1' to complete pizza making
        thread2.join(); // Waiting for 'Cuisinier2' to complete pizza making
        thread3.join(); // Waiting for 'Cuisinier3' to complete pizza making
    }

    private static void create(String name) throws Exception { // Method to initiate the pizza making process
        Connection connection = RabbitMQConfig.getConnection(); // Establishing a connection to RabbitMQ
        Channel channel = connection.createChannel(); // Creating a channel for communication

        System.out.println("PIZZAIOLO [#" + name + "]"); // Displaying a message indicating the pizza maker's initiation
        channel.exchangeDeclare(EXCHANGE_PIZZAIOLO_NAME, BuiltinExchangeType.FANOUT); // Declaring an exchange for pizza makers
        channel.queueDeclare(QUEUE_HANDLE_CLIENT_NAME, false, false, false, null); // Declaring a queue to handle client requests
        channel.queueBind(QUEUE_HANDLE_CLIENT_NAME, EXCHANGE_PIZZAIOLO_NAME, ASSISTANT_NAMESPACE + ".#"); // Binding the queue to the pizza maker exchange

        channel.exchangeDeclare(EXCHANGE_DELIVER_NAME, BuiltinExchangeType.FANOUT); // Declaring an exchange for deliveries
        channel.queueDeclare(QUEUE_ORDER_READY_NAME, false, false, false, null); // Declaring a queue for orders ready for delivery
        channel.queueBind(QUEUE_ORDER_READY_NAME, EXCHANGE_DELIVER_NAME, PIZZAIOLO_NAMESPACE + "." + name); // Binding the queue to the delivery exchange with the specified name

        DeliverCallback deliverCallback = (consumerTag, delivery) -> { // Handling incoming orders for pizza making
            byte[] body = delivery.getBody(); // Extracting the message body
            String command = new String(body, StandardCharsets.UTF_8); // Converting the body to a readable string using UTF-8 encoding

            var clientName = command.split("#")[0]; // Extracting the client's name from the received command
            var pizzaStr = command.split("#")[1].split(","); // Extracting pizza details from the command
            var pizzas = Arrays.stream(pizzaStr)
                    .map(str -> new Pizza(str.split("<>")[0], Integer.parseInt(str.split("<>")[1]))) // Creating Pizza objects from the received details
                    .toList(); // Collecting pizzas into a list

            System.out.println(name + ": received command for client " + clientName + ", " + pizzas); // Displaying the received command and its details

            var threads = new ArrayList<Thread>(); // Creating a list to store pizza-making threads

            for (Pizza pizza : pizzas) { // Processing each pizza in the received order
                Thread thread = new Thread(() -> { // Creating a thread to make each pizza
                    try {
                        System.out.println(name + ": Je vais preparer la pizza " + pizza.name()); // Displaying the pizza being prepared
                        Thread.sleep(pizza.preparationTime()); // Simulating pizza preparation time by pausing the thread
                        System.out.println(name + ": La pizza " + pizza.name() + " est prête"); // Displaying the completion of making a pizza
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Handling interruption exceptions
                    }
                });
                threads.add(thread); // Adding each pizza-making thread to the list
                thread.start(); // Starting the pizza-making thread
            }

            for (Thread thread : threads) { // Waiting for each pizza-making thread to complete
                try {
                    thread.join(); // Joining each thread to ensure completion before proceeding
                } catch (InterruptedException e) {
                    throw new RuntimeException(e); // Throwing an exception if interrupted
                }
            }

            System.out.println(name + ": J'ai terminé la commande de " + clientName); // Displaying completion of the client's order
            channel.basicPublish(EXCHANGE_DELIVER_NAME, PIZZAIOLO_NAMESPACE + "." + name, null, body); // Publishing the completed order to the delivery exchange
        };

        channel.basicConsume(QUEUE_HANDLE_CLIENT_NAME, true, deliverCallback, consumerTag -> {
            // Consuming messages from the queue, with a callback for handling the received message (empty for this scenario)
        });

        while(true){} // Infinite loop to keep the program running and handling orders continuously
    }
}
