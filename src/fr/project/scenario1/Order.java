package fr.project.scenario1;

import java.util.List;

public record Order(Client client, List<Pizza> pizzas) {
}
