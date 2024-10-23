package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                    displayProducts(inventory, cart, scanner);
                    break;
                case 2:
                    displayCart(cart, scanner, totalAmount);
                    break;
                case 3:
                    System.out.println("Thank you for shopping with us!");
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // This method should read a CSV file with product information and
        // populate the inventory ArrayList with com.pluralsight.Product objects. Each line
        // of the CSV file contains product information in the following format:
        //
        // id,name,price
        //
        // where id is a unique string identifier, name is the product name,
        // price is a double value representing the price of the product

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String input;
            while ((input = bufferedReader.readLine()) != null) {

                String[] strings = input.split("\\|");

                inventory.add(new Product(strings[0], strings[1], Double.parseDouble(strings[2]), strings[3]));

            }
        } catch (IOException e) {
            System.err.println("Error reading the file.");
        }
    }

    public static void displayProducts(ArrayList<Product> inventory, ArrayList<Product> cart, Scanner scanner) {
        // This method should display a list of products from the inventory,
        // and prompt the user to add items to their cart. The method should
        // prompt the user to enter the ID of the product they want to add to
        // their cart. The method should
        // add the selected product to the cart ArrayList.

        System.out.println("The list of all products: ");

        for (Product product : inventory) {
            System.out.println(product);
        }

        boolean display = true;
        while (display) {

            System.out.println("Search a product(1)\nAdd a product to your cart with id(2)\nGo Back to the home page(3)");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    searchProduct(scanner, inventory);
                    break;

                case "2":
                    System.out.println("Enter the id of the product you want to add: ");
                    String idAddProduct = scanner.nextLine();
                    for (Product product : inventory) {
                        if (product.getId().equalsIgnoreCase(idAddProduct)) {
                            cart.add(product);

                        }
                    }
                    break;

                case "3":
                    display = false;
                    break;
            }
        }
    }


    public static void searchProduct(Scanner scanner, ArrayList<Product> inventory) {

        System.out.println("Search by:\nId(1)\nName(2)\nPrice(3)\nDepartment(4)");
        String input = scanner.nextLine();

        switch (input) {

            case "1":
                System.out.println("Enter the id to search for the product: ");
                String idSearch = scanner.nextLine();

                System.out.println(findProductById(idSearch, inventory));
                break;

            case "2":
                System.out.println("Enter the name to search for the product: ");
                String nameSearch = scanner.nextLine();

                System.out.println(searchProductByName(nameSearch, inventory));
                break;

            case "3":
                System.out.println("Enter the price to search for the product: ");
                double priceSearch = scanner.nextDouble();

                System.out.println(searchProductByPrice(priceSearch, inventory));
                break;

            case "4":
                System.out.println("Enter the department to search for the product: ");
                String department = scanner.nextLine();

                System.out.println(searchProductByDepartment(department, inventory));
        }
    }

    public static void displayCart(ArrayList<Product> cart, Scanner scanner, double totalAmount) {
        // This method should display the items in the cart ArrayList, along
        // with the total cost of all items in the cart. The method should
        // prompt the user to remove items from their cart by entering the ID
        // of the product they want to remove. The method should update the cart ArrayList and totalAmount
        // variable accordingly.




        while (true) {
            System.out.println("Enter one of the following options:\nView the contents of the cart(1)\nRemove and item from the cart(2)\nCheckOut(3)\nGo back to the home screen(4)");
            String input = scanner.nextLine();

            switch (input) {

                case "1":
                    cartContentDisplay(cart);
                    System.out.println("Total price: " + cartContentCost(cart));
                    break;

                case "2":
                    System.out.println("Enter the id of the product you want to remove:");
                    String idRemove = scanner.nextLine();

                    for (Product product : cart) {
                        if (product.getId().equalsIgnoreCase(idRemove)) {
                            cart.remove(product);
                        }
                    }

                    Iterator<Product> iterator = cart.iterator();

                    // Iterate through the ArrayList
                    while (iterator.hasNext()) {
                        String element = iterator.next();
                        System.out.println(element);
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
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.valueOf(localDateTime) + ".txt"));

                    bufferedWriter.write(String.valueOf(stringBuilder));
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
