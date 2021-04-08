/**
 * @author Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

package edu.ucalgary.ensf409;

import java.io.*;
import java.util.*;

public class Runner {
    private final static String OUTPUT_FILENAME = "OrderForm.txt";
    private final static String USERNAME = "scm";
    private final static String PASSWORD = "ensf409";
    private final static String URL = "jdbc:mysql://localhost/inventory";
    private final static File outputFile = new File(OUTPUT_FILENAME);

    /**
     * Responsible for getting the user input and calling helper function processInput
     */
    public static void main(String[] args) {

        try (Scanner input = new Scanner(System.in)) {
            String category;
            String type;
            String[] validFurnitureTypes;
            int amount;

            //get furniture category
            while (true) {
                System.out.println("Enter one of the following furniture categories or \"exit\" to end program: ");
                System.out.println("1. chair");
                System.out.println("2. desk");
                System.out.println("3. filing");
                System.out.println("4. lamp");
                category = input.nextLine().toLowerCase().trim();  //store as lowercase for comparisons

                if (category.equals("exit")) {   //end program if exit was inputted
                    return;
                } else if (isValidCategory(category)) {    //break if category is valid
                    break;
                } else {  //else print invalid category and re-prompt an input
                    System.out.print("Invalid furniture category! ");
                }
            }

            //get all valid types of the input category
            validFurnitureTypes = getCategoryTypes(category);

            //get furniture type
            while (true) {
                System.out.println("Enter one of the following " + category + " types or \"exit\" to end program: ");

                //print all valid types of the category as possibilities
                for (int i = 0; i < validFurnitureTypes.length; i++) {
                    System.out.println((i + 1) + ". " + validFurnitureTypes[i]);
                }
                type = input.nextLine().toLowerCase().trim();

                if (type.equals("exit")) {   //end program if exit was inputted
                    return;
                } else if (Arrays.asList(validFurnitureTypes).contains(type)) {  //break if the input was one of the valid furniture types
                    break;
                } else {  //print invalid type and re-prompt an input
                    System.out.print("Invalid furniture type! ");
                }
            }

            //get furniture amount
            while (true) {
                System.out.println("Enter the desired amount of " + type + " " + category + "(s) or \"exit\" to end program: ");
                String placeholder = input.nextLine().trim();

                if (placeholder.equals("exit")) {    //end program if exit was inputted
                    return;
                } else if (isValidAmount(placeholder)) {   //break if the input was a valid amount
                    amount = Integer.parseInt(placeholder);
                    break;
                } else {  //print invalid amount and re-prompt an input
                    System.out.print("Invalid furniture amount! ");
                }
            }
            //process the user input
            processInput(category, type, amount);
        }
    }

    /**
     * Processes the user input and outputs the appropriate behaviour.
     * Outputs "cannot fulfill order" with the valid Manufacturers if order cannot be fulfilled.
     * Outputs parts to order if order can be fulfilled and calls generateOrderForm to create OrderForm.txt.
     * Utilizes the MySQLService Class to access the database and the CombinationFinder Class to determine a valid combination.
     *
     * @param category Furniture category user input
     * @param type     Furniture type user input
     * @param amount   Furniture amount user input
     */
    public static void processInput(String category, String type, int amount) {
        //check if all arguments are valid
        if ((!isValidCategory(category.toLowerCase())) ||
                (!Arrays.asList(getCategoryTypes(category.toLowerCase())).contains(type.toLowerCase())) ||
                (!isValidAmount(Integer.toString(amount)))) {

            throw new IllegalArgumentException();
        }

        // establish connection to database
        MySQLService db = new MySQLService(URL, USERNAME, PASSWORD);
        //instantiate a CombinationFinder object using data from the database and user input
        CombinationFinder solver = new CombinationFinder(db.getData(category), type, amount);
        solver.solve(); //find a valid combination
        InventoryEntity[] results = solver.getRemovedItems();   //get the Items that fulfill the order, returns null for no solution

        if(results == null) {   //if no solution was found
            String[] manufacturers = MySQLService.getManu(category);

            // output error message and end program
            System.out.print("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ");
            for (int i = 0; i < manufacturers.length; i++) {

                if (i != manufacturers.length - 1) {
                    System.out.print(manufacturers[i] + ", ");
                } else {
                    System.out.println(manufacturers[i] + ".");
                }

            }

        } else {
            //remove the rows from the database if a solution was found
            db.updateDatabases(category, Arrays.asList(results));

            //output to command line
            System.out.print("Purchase ");
            int i;
            for (i = 0; i < results.length - 1; i++) {
                System.out.print(results[i].getId() + ", ");
            }
            System.out.println("and " + results[i].getId() + " for $" + solver.getBestPrice());

            //Generate OrderForm.txt
            generateOrderForm(outputFile, category, type, amount, results, solver.getBestPrice());
        }

        //close db connection
        if (db != null) {
            db.close();
        }
    }

    /**
     * Generates the OrderForm.txt file using the arguments provided in the standard format provided in the handout.
     *
     * @param outputFile File to write order form to
     * @param category   Furniture category to order
     * @param type       Furniture type to order
     * @param amount     Amount of furniture to order
     * @param results    Furniture pieces to order
     * @param bestPrice  Cost of order
     */
    public static void generateOrderForm(File outputFile, String category,
                                         String type, int amount, InventoryEntity[] results, int bestPrice) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));

            writer.write("Furniture Order Form");
            writer.newLine();
            writer.newLine();
            writer.write("Faculty: ");
            writer.newLine();
            writer.write("Contact: ");
            writer.newLine();
            writer.write("Date: ");
            writer.newLine();
            writer.newLine();
            writer.write("Request: " + type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + " " +
                    category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase() + ", " + amount);
            writer.newLine();
            writer.newLine();
            writer.write("Item(s) Ordered:");
            writer.newLine();

            for (InventoryEntity result : results) {
                writer.write(result.getId());
                writer.newLine();
            }

            writer.newLine();
            writer.write("Total Price: $" + bestPrice);
        } catch (IOException e) {
            System.out.println("Error generating order form!");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to close file " + outputFile.getAbsolutePath());
            }
        }
    }

    /**
     * Verifies if the supplied category is one of the four valid categories
     *
     * @param category a string containing the category to verify in lowercase
     * @return true if category is a valid furniture category, else returns false
     */
    public static boolean isValidCategory(String category) {
        return category.equals("chair") || category.equals("desk")
                || category.equals("filing") || category.equals("lamp");
    }

    /**
     * Verifies if the supplied String amount represents a valid furniture amount.
     * Amount must be a natural number, defined as an integer with a value greater than 1.
     *
     * @param amount the String representation of a furniture amount
     * @return true if amount is a valid furniture amount, else returns false
     */
    public static boolean isValidAmount(String amount) {
        try {
            int test = Integer.parseInt(amount);    //throws exception if not an integer
            if (test < 1) {
                throw new IllegalArgumentException();   //throw exception if amount <= 0
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the valid types of furniture for the supplied furniture category.
     * Returns null if the supplied furniture category is invalid
     *
     * @param category a string containing the category in lowercase
     * @return a string array containing all types of the category in lowercase, or null for invalid argument
     * @throws IllegalArgumentException if category is none of the four valid categories
     */
    public static String[] getCategoryTypes(String category) {

        String[] result;

        switch (category.toLowerCase()) {
            case "chair":
                result = new String[]{"task", "mesh", "kneeling", "executive", "ergonomic"};
                break;
            case "desk":
                result = new String[]{"adjustable", "standing", "traditional"};
                break;
            case "filing":
                result = new String[]{"small", "medium", "large"};
                break;
            case "lamp":
                result = new String[]{"desk", "study", "swing arm"};
                break;
            default:
                throw new IllegalArgumentException("Invalid category supplied to getCategoryTypes");
        }

        return result;
    }
}