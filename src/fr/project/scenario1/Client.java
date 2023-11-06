package fr.project.scenario1;

import java.util.List;

public record Client(String name, List<Pizza> pizzas, boolean waitForMenus) {


}
