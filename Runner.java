/**
 * @author  Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

// package edu.ucalgary.ensf409;

import java.io.*;
import java.util.*;

public class Runner {
    private final static String OUTPUTFILENAME = "OrderForm.txt";
    private final static String USERNAME = "joeya1";
    private final static String PASSWORD = "Deanjo_19";
    private final static String URL = "jdbc:mysql://localhost/inventory";
    private static File outputFile = new File(OUTPUTFILENAME);

    /**
     * Responsible for getting the user input and calling helper function processInput
     */
    public static void main(String[] args) {
        Scanner input = null;

        try {
            input = new Scanner(System.in);
            String category = null;
            String type = null;
            String[] validFurnitureTypes = null;
            int amount = 0;

            //get furniture category
            while(true) {
                System.out.println("Enter one of the following furniture categories or \"exit\" to end program: ");
                System.out.println("1. chair");
                System.out.println("2. desk");
                System.out.println("3. filing");
                System.out.println("4. lamp");
                category = input.nextLine().toLowerCase().trim();  //store as lowercase for comparisons

                if(category.equals("exit")) {   //end program if exit was inputted
                    return;
                }
                else if(isValidCategory(category)) {    //break if category is valid
                    break;
                }
                else {  //else print invalid category and reprompt an input
                    System.out.print("Invalid furniture Category! ");
                }
            }

            //get all valid types of the input category
            validFurnitureTypes = getCategoryTypes(category);

            //get furniture type
            while(true) {
                System.out.println("Enter one of the following "+ category +" types or \"exit\" to end program: ");

                //print all valid types of the category as possibilites
                for (int i = 0; i < validFurnitureTypes.length; i++) {                    
                    System.out.println(String.valueOf(i+1) + ". " + validFurnitureTypes[i]);
                }
                type = input.nextLine().toLowerCase().trim();

                if(type.equals("exit")) {   //end program if exit was inputted
                    return;
                }
                else if(Arrays.asList(validFurnitureTypes).contains(type)) {  //break if the input was one of the valid furniture types
                    break;
                }
                else {  //print invalid type and reprompt an input
                    System.out.print("Invalid furniture type! ");
                }
            }
            
            //get furniture amount
            while(true) {
                System.out.println("Enter the desired amount of " + type + " " + category + "(s) or \"exit\" to end program: ");
                String placeholder = input.nextLine().trim();
                
                if(placeholder.equals("exit")) {    //end program if exit was inputted
                    return;
                }
                else if(isValidAmount(placeholder)) {   //break if the input was a valid amount
                    amount = Integer.parseInt(placeholder);
                    break;
                }
                else {  //print invalid amount and reprompt an input
                    System.out.print("Invalid furniture type! ");
                }
            }

            //process the user input
            processInput(category, type, amount);
        }
        catch(IllegalArgumentException e) { //TODO
            System.out.println("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //close scanner quietly
            try{ input.close(); } catch(Exception e) {}
        }
    }

    /**
     * Processes the user input and outputs the appropriate behaviour. 
     * Outputs "cannot fulfill order" with the valid Manufacturers if order cannot be fulfilled.
     * Outputs parts to order if order can be fulfilled and calls generateOrderForm to create OrderForm.txt.
     * Utilizes the MySQLService Class to access the database and the CombinationFinder Class to determine a valid combination.
     * @param category Furniture category user input
     * @param type Furniture type user input
     * @param amount Furniture amount user input
     */
    public static void processInput(String category, String type, int amount) {
        //instantiate a CombinationFinder object using data from the database and user input
       
        MySQLService db = new MySQLService(URL, USERNAME, PASSWORD);
        CombinationFinder solver = new CombinationFinder(db.getData(category), type, amount); 
        solver.solve(); //find a valid combination
        InventoryEntity[] results = solver.getRemovedItems();   //get the Items that fulfill the order, returns null for no solution

        if(results == null) {   //if no solution was found
            String[] manu = null;
            int i = 0;

            if(category.equals("chair")) {  //get valid chair manufacturers
                manu = db.getManu()[0]; 
            }
            else if(category.equals("desk")){ //get valid desk manufacturers
                manu = db.getManu()[1];
            }
            else if(category.equals("lamp")) {  //get valid lamp manufacturers
                manu = db.getManu()[2];
            }
            else if(category.equals("filing")){  //get valid filing manufacturers
                manu = db.getManu()[3];
            }
            else {
                throw new IllegalArgumentException();
            }

            // output error message and end program
            System.out.print("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ");
            for (i = 0; i < manu.length-1; i++) {
                System.out.print(manu[i] + ", ");
            }
            System.out.println(manu[i] + ".");
        }
        else {
            //remove the rows from the database if a solution was found
            db.updateDatabases(category, Arrays.asList(results));
            
            //output to command line
            System.out.print("Purchase ");
            int i = 0;
            for (i = 0; i < results.length-1; i++) {
                System.out.print(results[i].getId() + ", ");
            }
            System.out.println("and " + results[i].getId() + " for $" + solver.getBestPrice());
    
            //Generate OrderForm.txt
            generateOrderForm(outputFile, category, type, amount, results, solver.getBestPrice());
    
            System.out.println("Order form created!");
        }
        
        //close db connection
        db.close();
    }

    /**
     * Generates the OrderForm.txt file using the arguments provided in the standard format provided in the handout.
     * @param outputFile File to write order form to
     * @param category Furniture category to order
     * @param type Furniture type to order
     * @param amount Amount of furniture to order
     * @param results Furniture pieces to order
     * @param bestPrice Cost of order
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
            writer.write("Request: " + type + " " + category + ", " + amount);
            writer.newLine();
            writer.newLine();
            writer.write("Item(s) Ordered:");
            writer.newLine();
    
            for (int i = 0; i < results.length; i++) {
                writer.write(results[i].getId());
                writer.newLine();
            }
    
            writer.newLine();
            writer.write("Total Price: $" + bestPrice);    
        }
        catch(IOException e) {
            System.out.println("Error generating order form!");
        }
        finally {           
            try { writer.close(); } catch(Exception e) {}
        }
    }

    /**
     * Verifies if the supplied category is one of the four valid categories
     * @param category a string containing the category to verify in lowercase
     * @return true if category is a valid furniture category, else returns false
     */
    public static boolean isValidCategory(String category) {
        if(category.equals("chair") || category.equals("desk")
        || category.equals("filing") || category.equals("lamp")) {
            return true;
        }
        return false;
    }

    /**
     * Verifies if the supplied String amount represents a valid furniture amount.
     * Amount must be a natural number, defined as an integer with a value greater than 1.
     * @param amount the String representation of a furniture amount
     * @return true if amount is a valid furniture amount, else returns false
     */
    public static boolean isValidAmount(String amount) {
        try {
            int test = Integer.parseInt(amount);    //throws exception if not an integer
            if(test < 1) {
                throw new IllegalArgumentException();   //throw exception if amount <= 0
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    /**
     * Returns the valid types of furniture for the supplied furniture category. 
     * Returns null if the supplied furniture category is invalid
     * @param category a string containing the category in lowercase
     * @return a string array containing all types of the category in lowercase, or null for invalid argument
     * @throws IllegalArgumentException if category is none of the four valid categories
     */
    public static String[] getCategoryTypes(String category) {

        if(category.equals("chair")) {
            return new String[] {"task", "mesh", "kneeling", "executive", "ergonomic"};
        }
        else if(category.equals("desk")) {
            return new String[] {"adjustable", "standing", "traditional"};
        }
        else if(category.equals("filing")) {
            return new String[] {"small", "medium", "large"};
        }
        else if(category.equals("lamp")) {
            return new String[] {"desk", "study", "swing arm"};
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}