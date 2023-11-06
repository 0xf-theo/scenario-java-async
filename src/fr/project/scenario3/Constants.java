package fr.project.scenario3; // Déclaration du package pour le code

public class Constants { // Définition de la classe Constants pour les valeurs constantes

    // Définition des noms des échanges
    public final static String EXCHANGE_CLIENT_NAME = "EXCHANGE_CLIENT_NAME";
    public final static String EXCHANGE_ASSISTANT_NAME = "EXCHANGE_ASSISTANT_NAME";
    public final static String EXCHANGE_PIZZAIOLO_NAME = "EXCHANGE_PIZZAIOLO_NAME";
    public final static String EXCHANGE_DELIVER_NAME = "EXCHANGE_DELIVER_NAME";

    // Définition des noms des files d'attente
    public final static String QUEUE_HANDLE_CLIENT_NAME = "QUEUE_HANDLE_CLIENT_NAME";
    public final static String QUEUE_DELIVER_NAME = "QUEUE_DELIVER_NAME";
    public final static String QUEUE_CLIENT_HANDLED_NAME = "QUEUE_CLIENT_HANDLED_NAME";
    public final static String QUEUE_ORDER_READY_NAME = "QUEUE_ORDER_READY_NAME";
    public final static String QUEUE_ORDER_READY_FOR_DELIVERY_NAME = "QUEUE_ORDER_READY_FOR_DELIVERY_NAME";

    // Définition des namespaces pour les commandes, les livraisons, les assistants et les pizzaïolos
    public final static String COMMAND_NAMESPACE = "COMMAND_NAMESPACE";
    public final static String DELIVER_NAMESPACE = "DELIVER_NAMESPACE";
    public final static String ASSISTANT_NAMESPACE = "ASSISTANT_NAMESPACE";
    public final static String PIZZAIOLO_NAMESPACE = "PIZZAIOLO_NAMESPACE";
}
