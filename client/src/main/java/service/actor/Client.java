package service.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ActorSelection;

import service.core.*;
import service.messages.*;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import java.lang.Thread;

public class Client extends AbstractActor {

    public static HashMap<Integer, RestaurantInfo> restaurants;
    public static boolean menusReturned = false;
    public static HashMap<Integer, Dish> currentMenu;
    public static Order workingOrder;
    public static ArrayList<Integer> validResponses;
    public static int selectedRestaurantNumber;
    public static Dish selectedDish;
    public static HashMap<Dish, Integer> menuItemCount;
    public static double mealPrice;
    public static double brokerCommission;
    public static String userName = "";

    // ClientInfo fields:
    public static ClientInfo clientInfo = new ClientInfo("Sigma customer","EC001");
    // Needs a main method, a createReceive() method to handle responses, and probably additional methods for prettyprinting responses etc.

    public static void main(String[] args) { // Main method for the interactive client
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Client.class), "client");
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");

        if (args.length != 0) {
            for (String eachArgument : args) {
                userName = userName.concat(eachArgument);
                userName = userName.concat(" ");
            }
            clientInfo.setName(userName);
        }

        // 1. Request restaurants from broker (intention is that this will be handled before any client input arrives)
        requestMenus(selection, ref); // a separate method so we can create an option to repeat this action / refresh restaurants known to the client

        // 2. Print the main menu options, then open a scanner and a while (true) loop and wait for user input
        // This shouldn't happen until menus have been returned, so I'm introducing a loop to make sure the client waits.
        int timeOut = 0;
        while (! menusReturned) { // Loop will break once the broker has returned all known menus to the client
            try {
                Thread.sleep(1000);
                timeOut ++;
                if (timeOut > 15) {
                    System.out.println("No response from broker - shutting down");
                    System.exit(1);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
       printMainMenu();

        Scanner userInput = new Scanner(System.in);
        RestaurantInfo selectedRestaurant;
        int loopLevel = 1;
        int userSelection;

        while (loopLevel > 0) {
            validResponses = new ArrayList<Integer>();
            validResponses.add(0);
            for (int eachKey : restaurants.keySet()) {
                validResponses.add(eachKey);
            }
            userSelection = validateUserInput(userInput, validResponses);
            if (userSelection < 1) { // This conditional handles all 0 and invalid inputs
                if (userSelection == 0) {
                    loopLevel --;
                }
                continue;
            }
            selectedRestaurant = restaurants.get(userSelection);
            selectedRestaurantNumber = 0 + userSelection;
            currentMenu = selectedRestaurant.getMenu();
            workingOrder = new Order(selectedRestaurant, 0.0);
            menuItemCount = new HashMap<Dish, Integer>();
            for (Dish eachDish : currentMenu.values()) {
                menuItemCount.put(eachDish, 0);
            }
            loopLevel ++;

            while (loopLevel > 1) { // Refactored level 2 entrypoint
                printOrderModeSelectionOptions();
                validResponses.clear();
                for (int i=0; i<=3; i++) {
                    validResponses.add(i); // Options for order mode selection are integers 0-3
                }
                userSelection = validateUserInput(userInput, validResponses);
                // Handle level 2 valid responses below
                if (userSelection == 0) { // deincrement and continue to drop a "level"
                    loopLevel --;
                    System.out.println("\n\n");
                    printMainMenu();
                    continue;
                } else if (userSelection == 1) { // Proceed to payment
                    // There should really be a confirmation screen here, but for now submission just goes straight through
                    if (workingOrder.getOrderItems().size() == 0) {
                        System.out.println("Your order is not ready for submission - returning to mode selection menu");
                        continue;
                    }

                    // Print out the current price of the order as "Meal Price" and the broker's commission as "Sigma Service Charge"
                    // I actually want
                    mealPrice = Math.round(workingOrder.getPrice() * 100.0) / 100.0;
                    System.out.println("Total meal price: €" + String.format("%.2f", mealPrice));
                    brokerCommission = Math.round((mealPrice * 0.05) * 100.0) / 100.0;
                    System.out.println("Sigma service charge: €" + String.format("%.2f", brokerCommission));
                    System.out.println("Total payment: €" + String.format("%.2f", mealPrice+brokerCommission));

                    // Then show the confirmation screen: 0 to go back and 1 to proceed with the order
                    printConfirmationScreen();
                    validResponses.clear();
                    validResponses.add(0);
                    validResponses.add(1);
                    userSelection = validateUserInput(userInput, validResponses);

                    if (userSelection <= 0) {
                        System.out.println("\nYou have chosen not to submit your order - Returning to previous screen");
                        continue;
                    } else if (userSelection == 1) {
                        workingOrder.setPrice(mealPrice + brokerCommission);
                        submitOrder(selection, ref, workingOrder, brokerCommission);
                        System.out.println("\nYour order has been submitted - returning to main menu\n");
                        printMainMenu();
                        loopLevel --;
                        continue;
                    }
                } else if (userSelection == 2) { // Add items to order
                    loopLevel ++;
                    validResponses.clear();
                    validResponses.add(0);
                    for (int validMenuItem : currentMenu.keySet()) {
                        validResponses.add(validMenuItem+1);
                    }
                    printOrderAdditionOptions(selectedRestaurant);
                    while (loopLevel > 2) {
                        userSelection = validateUserInput(userInput, validResponses);
                        if (userSelection == 0) {
                            printWorkingOrder(workingOrder, currentMenu);
                            loopLevel --;
                            continue;
                        } else if (userSelection < 0) {
                            System.out.println("Not a menu item!!");
                        } else {
                            selectedDish = currentMenu.get(userSelection-1);
                            workingOrder.addToOrder(selectedDish);
                            menuItemCount.put(selectedDish, menuItemCount.get(selectedDish)+1);
                            System.out.println("1x \"" + currentMenu.get(userSelection-1).getName() + "\" added to order");
                            System.out.println("Current order subtotal: €" + String.format("%.2f", workingOrder.getPrice()));
                        }
                    }
                } else if (userSelection == 3) { // Remove items from order
                    if (workingOrder.getOrderItems().size() == 0) {
                        System.out.println("Your order is empty - returning to mode selection menu");
                        continue;
                    }
                    loopLevel ++;
                    validResponses.clear();
                    validResponses.add(0);
                    for (int validMenuItem : currentMenu.keySet()) {
                        validResponses.add(validMenuItem+1);
                    }
                    printOrderAdditionOptions(selectedRestaurant);
                    while (loopLevel > 2) {
                        
                        userSelection = validateUserInput(userInput, validResponses);
                        if (userSelection == 0) {
                            printWorkingOrder(workingOrder, currentMenu);
                            loopLevel --;
                            continue;
                        } else if (userSelection < 0) {
                            System.out.println("Not a menu item!!");
                        } else {
                            selectedDish = currentMenu.get(userSelection-1);
                            // System.out.println("The dish you have chosen to remove from your order is: " + selectedDish.getName());
                            if (workingOrder.getOrderItems().contains(selectedDish)) {
                                workingOrder.removeFromOrder(selectedDish);
                                menuItemCount.put(selectedDish, menuItemCount.get(selectedDish)-1);
                                System.out.println("\n1x \"" + currentMenu.get(userSelection-1).getName() + "\" removed from order");
                                printWorkingOrder(workingOrder, currentMenu);
                                if (workingOrder.getOrderItems().size() == 0) {
                                    loopLevel --;
                                    System.out.println("\nYour order is now empty - returning to mode selection menu");
                                    continue;
                                }
                            } else {
                                System.out.println("Your order does not contain \"" + selectedDish.getName() + "\"");
                                printWorkingOrder(workingOrder, currentMenu);
                                System.out.println("Please select a different item to remove from your order");
                            }
                        }
                    }
                } else if (userSelection == -1) {
                    System.out.println("Not a valid option!");
                }
            }
        }
        // Shut down the program once the outermost while loop is ended

 
        System.exit(0);

    }

    public static void requestMenus(ActorSelection selection, ActorRef ref) {
        // Create a MenuRequest object and send to the broker
        // Broker should respond with a MenuResponse message which will be handled by the createReceive() method below
        selection.tell(new MenuRequest(clientInfo), ref);
    }

    public static void submitOrder(ActorSelection selection, ActorRef ref, Order finishedOrder, double serviceCharge) {
        selection.tell(new OrderRequestCtoB(clientInfo, finishedOrder, serviceCharge), ref);
    }

    public static void printMenuItem(int menuNumber, Dish dish) {
        // System.out.println("|=================================================================================================================|");
        // System.out.println("|                                                       |                                                         |");
        System.out.println(
                menuNumber+1 + ": " +
                "| " + String.format("%1$-36s", dish.getName()) + 
                "| €" + String.format("%1$-6s", String.format("%.2f", dish.getPrice())) + " | ");
        // System.out.println("|                                                       |                                                         |");
        // System.out.println("|=================================================================================================================|");
    }


    public static void printMainMenu() {
        System.out.println("Enter the number associated with a restaurant in order to view its menu or place an order");
        System.out.println("0: Exit");
        for (int restaurantNumber : restaurants.keySet()) {
            System.out.println(restaurantNumber + ": " + restaurants.get(restaurantNumber).getName()); // Print out each restaurant along with its number
        }
    }

    public static void printOrderModeSelectionOptions() {
        // method containing the text to be printed when a restaurant is selected
        System.out.println("\nChoose your input mode from the options below:");
        System.out.println("0: Cancel order / Return to main menu");
        System.out.println("1: Finish order and proceed to payment");
        System.out.println("2: Add item to order");
        System.out.println("3: Remove item from order");
    }

    public static void printOrderAdditionOptions(RestaurantInfo selectedRest) {
        // I think for now this can copy the old code for printOrderOptions
        System.out.println("\nAdd items to your order one at a time by entering their menu number as displayed on the menu below");
        System.out.println("0: Complete order or return to main menu");
        for (Map.Entry<Integer, Dish> menuItem : selectedRest.getMenu().entrySet()) {
            printMenuItem(menuItem.getKey(), menuItem.getValue());
        }
    }

    public static void printOrderRemovalOptions(RestaurantInfo selectedRest) {
        // I think for now this can copy the old code for printOrderOptions
        System.out.println("\nRemove items to your order one at a time by entering their menu number as displayed on the menu below");
        System.out.println("0: Complete order or return to main menu");
        for (Map.Entry<Integer, Dish> menuItem : selectedRest.getMenu().entrySet()) {
            printMenuItem(menuItem.getKey(), menuItem.getValue());
        }
    }

    public static void printWorkingOrder(Order clientOrder, HashMap<Integer, Dish> restaurantMenu) {
        if (clientOrder.getOrderItems().size() == 0) {
            System.out.println("Your order currently contains no items");
            return;
        }
        System.out.println("Your order currently contains the following items:");

        // Instead of iterating over the client's order, I want to iterate over the menuItemCount
        // so for key in menuItemCount.keySet(), print menuItemCount.get(key)x key.getName()

        // OK, now I want to improve this
        // Ideally, we would count items in the same order they appear on the menu
        // Therefore, instead of selecting a dish as itemKey, I should iterate over the restaurantMenu and use the corresponding value in the menu to hash into menuItemCount




        for (Dish itemKey : menuItemCount.keySet()) {
            if (menuItemCount.get(itemKey) != 0) System.out.println(menuItemCount.get(itemKey) + "x " + itemKey.getName());
        } 
        System.out.println("Order subtotal: €" + String.format("%.2f", clientOrder.getPrice()));
    }

    public static void printConfirmationScreen() {
        System.out.println("\nAre you sure you would like to proceed with your order?");
        System.out.println("0: Do not submit order - return to mode selection menu");
        System.out.println("1: Proceed and submit order");
    }

    public static int validateUserInput(Scanner readFromConsole, ArrayList<Integer> validResponses) {
        boolean waitingForResponse = true;
        while (waitingForResponse) {
            if (readFromConsole.hasNextInt()) {
                // 3 options: valid, invalid, or 0
                int theInput = readFromConsole.nextInt();
                String remainingInput = readFromConsole.nextLine();
                if (validResponses.contains(theInput)) {
                    return theInput;
                } else {
                    System.out.println("Invalid input - Please select one of the valid options listed above");
                    return -1;
                }
            } else if (readFromConsole.hasNext()) {
                String invalidInput = readFromConsole.nextLine();
                System.out.println("Invalid input - Please enter a number according to the options above");
                return -1;
            } else {
                // Sleep for 1 second while awaiting input
                try {
                    System.out.println("You're caught in the response validation waiting loop");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return -1; // Default value to ensure compilation (although the loop shouldn't break without returning a valid value)
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(MenuResponse.class, msg -> {
                if (msg.getKeys().size() == msg.getValues().size()) {
                    restaurants = new HashMap<Integer, RestaurantInfo>();
                    LinkedList<Integer> msgKeys = msg.getKeys();
                    LinkedList<RestaurantInfo> msgValues = msg.getValues();
                    for (int i=0; i < msgKeys.size(); i++) {
                        restaurants.put(msgKeys.get(i) + 1, msgValues.get(i));
                    }
                    if(restaurants != null) menusReturned = true;
                } else {
                    System.out.println("Error: invalid information returned from broker");
                }
            })
            .match(String.class, msg -> {
                System.out.println(msg.toString());
            })
            .build();
    }
}
