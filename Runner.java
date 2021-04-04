/**
 * @author  Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

// package edu.ucalgary.ensf409;

import java.io.*;
import java.util.*;

public class Runner {
    private final static String outputFileName = "OrderForm.txt";
    private final static String USERNAME = "joeya1";
    private final static String PASSWORD = "Deanjo_19";
    private final static String URL = "jdbc:mysql://localhost/inventory";

    public static void main(String[] args) {
        CombinationFinder solver = null;
        MySQLService db = null;
        File outputFile = null;
        BufferedWriter writer = null;
        Scanner input = null;

        try {
            db = new MySQLService(URL, USERNAME, PASSWORD);
            outputFile = new File(outputFileName);
            writer = new BufferedWriter(new FileWriter(outputFile, false));
            input = new Scanner(System.in);
            String category = null;
            String type = null;
            String[] types = null;
            int amount = 0;

            //get furniture category
            while(true) {
                System.out.println("Enter one of the following furniture categories or \"exit\" to end program: ");
                System.out.println("1. chair");
                System.out.println("2. desk");
                System.out.println("3. filing");
                System.out.println("4. lamp");
                category = input.nextLine().toLowerCase().trim();  //store as lowercase for comparisons

                if(category.equals("exit")) {
                    return;
                }
                else if(isValidCategory(category)) {
                    break;
                }
                else {
                    System.out.print("Invalid furniture Category! ");
                }
            }

            types = getCategoryTypes(category);

            //get furniture type
            while(true) {
                System.out.println("Enter one of the following "+ category +" types or \"exit\" to end program: ");

                for (int i = 0; i < types.length; i++) {                    
                    System.out.println(String.valueOf(i+1) + ". " + types[i]);
                }
                type = input.nextLine().toLowerCase().trim();

                if(type.equals("exit")) {
                    return;
                }
                else if(Arrays.asList(types).contains(type)) {
                    break;
                }
                else {
                    System.out.print("Invalid furniture type! ");
                }
            }
            
            //get furniture amount
            while(true) {
                System.out.println("Enter the desired amount of " + type + " " + category + "(s) or \"exit\" to end program: ");
                String placeholder = input.nextLine().trim();
                
                if(placeholder.equals("exit")) {
                    return;
                }
                else if(isValidAmount(placeholder)) {
                    amount = Integer.parseInt(placeholder);
                    break;
                }
                else {
                    System.out.print("Invalid furniture type! ");
                }
            }

            //process
            solver = new CombinationFinder(db.getData(category), type, amount);
            solver.solve();
            InventoryEntity[] results = solver.getRemovedItems();

            if(results == null) {
                String[] manu = null;
                int i = 0;
                if(category.equals("chair")) {
                    manu = db.getManu()[0];
                }
                else if(category.equals("desk")){
                    manu = db.getManu()[1];
                }
                else if(category.equals("lamp")) {
                    manu = db.getManu()[2];
                }
                else {
                    manu = db.getManu()[3];
                }
                System.out.print("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ");
                for (i = 0; i < manu.length-1; i++) {
                    System.out.print(manu[i] + ", ");
                }
                System.out.println(manu[i] + ".");
                return;
            }
            db.updateDatabases(category, Arrays.asList(results));
            
            //output
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
            writer.write("Total Price: $" + solver.getBestPrice());
        }
        catch(IllegalArgumentException e) { //TODO
            System.out.println("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            //close all objects quietly
            try{
                input.close();
                writer.close();
                db.close();
            }
            catch(Exception e) {}
        }
    }

    /**
     * Verifies if the supplied category is one of the four valid categories
     * @param category a string containing the category to verify in lowercase
     * @return true if category is a valid furniture category, else returns false
     */
    public static boolean isValidCategory(String category) {
        if(category.equals("chair") || category.equals("desk")
        || category.equals("filing") || category.equals("lamp")){
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
            return null;
        }
    }
}