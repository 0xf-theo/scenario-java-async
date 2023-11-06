package fr.project.scenario3; // Déclaration du package pour le code

import fr.project.scenario1.Client; // Importation de la classe Client d'un autre package
import fr.project.scenario1.Pizza; // Importation de la classe Pizza d'un autre package

import java.util.ArrayList; // Importation de classes Java pour les collections
import java.util.Arrays; // Importation de classes Java pour la manipulation de tableaux
import java.util.List; // Importation de la classe List pour une collection de clients
import java.util.Scanner; // Importation de la classe Scanner pour la saisie utilisateur

public class ClientBuilder { // Définition de la classe ClientBuilder

    private Integer repeat = 1; // Initialisation d'une variable pour le nombre de répétitions

    public ClientBuilder repeat(Integer repeat) { // Méthode permettant de définir le nombre de répétitions
        this.repeat = repeat; // Affectation de la valeur du nombre de répétitions
        return this; // Retourne l'instance actuelle de ClientBuilder
    }

    public List<Client> build() { // Méthode pour construire une liste de clients
        var clients = new ArrayList<Client>(); // Création d'une liste pour stocker les clients

        for (int i = 0; i < repeat; i++) { // Boucle pour créer les clients en fonction du nombre de répétitions défini
            System.out.println("Client [#" + i + " ]"); // Affichage du numéro du client
            System.out.println("Please specify the name for that client:"); // Demande de saisie du nom du client
            System.out.flush(); // Vidage du flux de sortie

            var scanner = new Scanner(System.in); // Création d'une instance de Scanner pour lire l'entrée utilisateur
            var name = scanner.nextLine(); // Lecture du nom saisi par l'utilisateur

            System.out.println("Please specify the menus with pizzas separated with ',':"); // Demande de saisie des pizzas du menu
            var pizzas = Arrays.stream(scanner.nextLine().split(",")).map(str -> { // Lecture des pizzas séparées par des virgules
                System.out.println("For pizza " + str + " please specify preparation time:"); // Demande du temps de préparation pour chaque pizza
                System.out.flush(); // Vidage du flux de sortie

                var time = scanner.nextInt(); // Lecture du temps de préparation saisi par l'utilisateur
                System.out.println("Saving " + str + " with " + time); // Affichage de la sauvegarde de la pizza avec son temps de préparation
                return new Pizza(str, time); // Création d'une instance de Pizza avec le nom et le temps de préparation
            }).toList(); // Collecte des pizzas dans une liste

            clients.add(new Client(name, pizzas, false)); // Ajout d'un nouveau client à la liste de clients avec ses pizzas
            System.out.println("Saving " + name); // Affichage de la sauvegarde du nom du client
        }

        return clients; // Retourne la liste de clients construite
    }
}
