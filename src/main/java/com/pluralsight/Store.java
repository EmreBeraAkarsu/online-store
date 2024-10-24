package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Store {

    public static void main(String[] args) {
        // Initialize variables
        ArrayList<Product> inventory = new ArrayList<Product>();
        ArrayList<Product> cart = new ArrayList<Product>();
        double totalAmount = 0.0;

        // Load inventory from CSV file
        loadInventory("products.csv", inventory);

        // Create scanner to read user input
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        // Display menu and get user choice until they choose to exit
        while (choice != 3) {
            System.out.println("Welcome to the Online com.pluralsight.Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            // Call the appropriate method based on user choice
            switch (choice) {
                case 1:
                    //To display the inventory
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    //To display the items in the cart
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    //To exit
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    //default is an invalid option selected
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    //Load the .csv file contents into an Arraylist for the code to use
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {

//Read the .csv file
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String input;
            //Continue reading each line
            while ((input = bufferedReader.readLine()) != null) {

                //Split the line into separate section by the "|"
                String[] strings = input.split("\\|");

                //Use the split strings for creating a new Product object and add it to the inventory ArrayList
                inventory.add(new Product(strings[0], strings[1], Double.parseDouble(strings[2]), strings[3]));

            }
        } catch (IOException e) {
            //Display error message if there were any issues reading the file
            System.err.println("Error reading the file.");
        }
    }

    //Displays the inventory items
    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {

        //Display header
        System.out.println("The list of all products: ");

        //Iterate through the inventory ArrayList and print out the product objects one by one
        for (Product product : inventory) {
            System.out.println(product);
        }

        boolean display = true;
        //Menu for displaying the products
        while (display) {

            System.out.println("\nSearch a product(1)\nAdd a product to your cart with id(2)\nGo Back to the home page(3)");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    //To search a product with a specific value
                    searchProduct(scanner, inventory);
                    break;

                case "2":
                    //To add an item to the cart

                    //Prompt and store the id of the product the user wants to add to the cart
                    System.out.println("Enter the id of the product you want to add: ");
                    String idAddProduct = scanner.nextLine();

                    //Iterate through the inventory ArrayList
                    for (Product product : inventory) {
                        //Add to the cart Arraylist if there is a match between the user's input id and an item from the inventory ArrayList
                        if (product.getId().equalsIgnoreCase(idAddProduct)) {
                            cart.add(product);

                        }
                    }
                    break;

                case "3":
                    //To return to the main menu
                    display = false;
                    break;
            }
        }
    }


    //Search a product for a specific item value
    public static void searchProduct(Scanner scanner, ArrayList<Product> inventory) {

        //Get the option the user wants to search for
        System.out.println("Search by:\nId(1)\nName(2)\nPrice(3)\nDepartment(4)");
        String input = scanner.nextLine();

        //Menu for searching for an item with the selected value option
        switch (input) {

            case "1":
                //Search with the id

                //Ask the user to provide the id to search
                System.out.println("Enter the id to search for the product: ");
                String idSearch = scanner.nextLine();

                //Call the findProductById() method with the id as an argument to search a product and display the result
                System.out.println(findProductById(idSearch, inventory));
                break;

            case "2":
                //Search with the name

                //Ask the user to provide the name to search
                System.out.println("Enter the name to search for the product: ");
                String nameSearch = scanner.nextLine();

                //Call the searchProductByName() method to search a product by the name and display the result
                System.out.println(searchProductByName(nameSearch, inventory));
                break;

            case "3":
                //Search with the price

                //Ask the user to provide the price to search
                System.out.println("Enter the price to search for the product: ");
                double priceSearch = scanner.nextDouble();

                //Call the searchProductByPrice() method the search for a product with id and display the result
                System.out.println(searchProductByPrice(priceSearch, inventory));
                break;

            case "4":
                //Search with the department

                //Ask the user for the department to search
                System.out.println("Enter the department to search for the product: ");
                String department = scanner.nextLine();

                //Call the searchProductByDepartment() method to search a product by the department and display the result
                System.out.println(searchProductByDepartment(department, inventory));
        }
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {

        //Menu for displaying the cart and other cart related actions
        while (true) {
            //Ask the user to pick a cart related option and store the input
            System.out.println("Enter one of the following options:\nView the contents of the cart(1)\nRemove and item from the cart(2)\nCheckOut(3)\nGo back to the home screen(4)");
            String input = scanner.nextLine();

            switch (input) {

                case "1":
                    //Option to display the contents of the cart
                    cartContentDisplay(cart);
                    System.out.println("Total price: " + cartContentCost(cart));
                    break;

                case "2":
                    System.out.println("Enter the id of the product you want to remove:");
                    String idRemove = scanner.nextLine();

                    Iterator<Product> iterator = cart.iterator();

                    // Iterate through the ArrayList
                    while (iterator.hasNext()) {
                        Product element = iterator.next();

                        if (element.getId().equalsIgnoreCase(idRemove)) {
                            iterator.remove();
                        }
                    }
                    break;

                case "3":
                    checkOut(cart, scanner);
                case "4":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    public static void cartContentDisplay(ArrayList<Product> cart) {
        for (Product product : cart) {
            System.out.println(product);
        }
    }


    public static double cartContentCost(ArrayList<Product> cart) {
        double totalCost = 0;

        for (Product product : cart) {
            totalCost += product.getPrice();
        }

        return totalCost;
    }


    public static void checkOut(ArrayList<Product> cart, Scanner scanner) {
        // This method should calculate the total cost of all items in the cart,
        // and display a summary of the purchase to the user. The method should
        // prompt the user to confirm the purchase, and deduct the total cost
        // from their account if they confirm.

        StringBuilder stringBuilder = new StringBuilder();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = localDateTime.format(formatter);


        System.out.println("Contents of the cart: \n");
        cartContentDisplay(cart);

        System.out.println("The total cost: $" + cartContentCost(cart));

        System.out.println("Do you want to confirm the pruchase? yes/no");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("yes")) {
            System.out.println("How much are you paying?");
            double paid = scanner.nextDouble();
            scanner.nextLine();

            cartContentDisplay(cart);
            double totalDue = cartContentCost(cart);

            if (totalDue <= paid) {
                System.out.println("Receipt: ");
                System.out.println(localDateTime.toLocalDate());
                System.out.println("Your change is $" + (paid - totalDue));
                cartContentDisplay(cart);
                System.out.println("Total: " + cartContentCost(cart));
                System.out.println("Amount Paid: " + paid);


                stringBuilder.append("Receipt: ");
                stringBuilder.append("\n" + localDateTime);
                stringBuilder.append("\nYour change is $" + (paid - totalDue));
                stringBuilder.append("\n" + getContentsOfTheCart(cart));
                stringBuilder.append("\nTotal: " + cartContentCost(cart));
                stringBuilder.append("\nAmount Paid: " + paid);

                cart.clear();


                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(formattedDateTime + ".txt"));

                    bufferedWriter.write(stringBuilder.toString());

                    bufferedWriter.close();
                } catch (Exception e) {
                    System.err.println("Error writing to the file!");
                }

            } else {
                System.out.println("Amount paid is not enough!");
            }

        } else if (input.equalsIgnoreCase("no")) {

        } else {
            System.out.println("Invalid option");
        }




    }


    public static StringBuilder getContentsOfTheCart(ArrayList<Product> cart) {

        StringBuilder stringBuilder = new StringBuilder();

        for (Product product : cart) {
            stringBuilder.append(product);

        }
        return stringBuilder;
    }


    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // This method should search the inventory ArrayList for a product with
        // the specified ID, and return the corresponding com.pluralsight.Product object. If
        // no product with the specified ID is found, the method should return
        // null.
        Product productFound = null;

        for (Product product : inventory) {
            if (product.getId().equalsIgnoreCase(id)) {
                productFound = product;
            }
        }
        if (productFound == null) {

            System.out.println("Couldn't find the product");
        }
        return productFound;
    }

    public static Product searchProductByName(String name, ArrayList<Product> inventory) {


        Product productFound = null;

        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                productFound = product;
            }
        }
        if (productFound == null) {

            System.out.println("Couldn't find the product");
        }
        return productFound;
    }


    public static Product searchProductByPrice(double price, ArrayList<Product> inventory) {


        Product productFound = null;

        for (Product product : inventory) {
            if (product.getPrice() == price) {
                productFound = product;
            }
        }
        if (productFound == null) {

            System.out.println("Couldn't find the product");
        }
        return productFound;
    }

    public static ArrayList<Product> searchProductByDepartment(String department, ArrayList<Product> inventory) {


        ArrayList<Product> productsFound = new ArrayList<>();

        for (Product product : inventory) {
            if (product.getDepartment().equalsIgnoreCase(department)) {
                productsFound.add(product);
            }
        }
        if (productsFound.isEmpty()) {

            System.out.println("Couldn't find the product");
        }
        return productsFound;
    }
}
