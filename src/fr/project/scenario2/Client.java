package fr.project.scenario2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Client {
    public static void start() throws Exception {
        Connection connection = RabbitMQConfig.getConnection();
        Channel channel = connection.createChannel();

        sendOrder(channel, "Client1", List.of("Margarita"));
        sendOrder(channel, "Client2", List.of("Margarita"));
        sendOrder(channel, "Client3", List.of("Buffalo"));
        sendOrder(channel, "Client4", List.of("Calzone"));

        channel.close();
        connection.close();
    }

    private static void sendOrder(Channel channel, String clientName, List<String> articles) throws IOException {
        var message = "NEW_ORDER:" + clientName + "|" + String.join(",", articles);
        channel.basicPublish("demande_de_prise_en_charge", "", null, message.getBytes(StandardCharsets.UTF_8));
    }
}