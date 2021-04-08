/**
 * @author  Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

// package edu.ucalgary.ensf409;

import java.util.*;
import java.sql.*;

/**
 * class MySQLService uses inventory SQL database and will read or delete elements of the database.
 */
public class MySQLService {
    //class fields
    private final String URL; 
    private final String USERNAME; 
    private final String PASSWORD; 
    private Connection databaseConnection; //connection to SQL
    private ResultSet results; //result set from statement execution

    /**
     * class constructor to set values fields and initialize database connection
     * @param url string of url
     * @param username string of username
     * @param password string of password
     */
    public MySQLService (String url, String username, String password) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;

        //initialize connection to the SQL database, try and catch method to catch SQL exception
        try{
            this.databaseConnection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception in initializeConnection");
        }
        
    }

    /**
     * Get manufactures that create items in category
     * @return string array of manufacturers that make items in category
     */
    public static String[] getManu(String category) {

        String[] result;

        switch (category.toLowerCase()) {
            case "chair":
                result = new String[]{"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"};
                break;
            case "desk":
                result = new String[]{"Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies"};
                break;
            case "filing":
                result = new String[]{"Office Furnishings", "Furniture Goods", "Fine Office Supplies"};
                break;
            case "lamp":
                result = new String[]{"Office Furnishings", "Furniture Goods", "Fine Office Supplies"};
                break;
            default:
                throw new IllegalArgumentException("Invalid category supplied to getManufactures");
        }

        return result;
    }

    /**
     * method gets data specified by input table from the inventory database
     * @param tableName string of the furniture table to be extracted
     * @return a list of all the items in the extracted table, each row as an InventoryEntity object in the list
     */
    public List<InventoryEntity> getData(String tableName){
        List<InventoryEntity> returnList = new ArrayList<InventoryEntity>();
        
        //try and catch method to catch SQL exception
        try {

            /*
             * Select all entries from the desired table
             */
            String query = "SELECT * FROM ?";
            PreparedStatement myStmt  = databaseConnection.prepareStatement(query);
            myStmt.setString(1, tableName);
            results = myStmt.executeQuery("SELECT * FROM " + tableName);
            
            /* Declaring boolean arrays of length corresponding to their table properties */
            while (results.next()){               
                boolean[] properties;
                if (tableName.equalsIgnoreCase("chair")){
                    properties = new boolean[4]; //4 chair properties
                }
                else if (tableName.equalsIgnoreCase("desk") || tableName.equalsIgnoreCase("filing")) {
                    properties = new boolean[3]; //3 desk or filing properties
                }
                else {
                    properties = new boolean[2]; //2 lamp properties
                }

                //assigning boolean values in array according to table values
                for (int i = 0; i < properties.length; i++) {
                    properties[i] = results.getNString(i + 3).equals("Y");
                }

                //initializing InventoryEntity object
                InventoryEntity nextElement = new InventoryEntity(results.getString("ID"), results.getString("Type"), properties, results.getInt("Price"), results.getString("ManuID"));
                returnList.add(nextElement); //adding object to returnList
            }
            
            myStmt.close(); //closing statement
        } catch (SQLException ex) {
            // ex.printStackTrace();
            System.out.println("SQL exception in getData");
        }

        //returning the list of the item inventory
        return returnList;
    }

    
    /**
     * method to delete items of given table from the inventory database 
     * @param table string of the furniture table to be extracted
     * @param removeItems list of all the items to be removed from the database, each element in the list 
     * is an InventoryEntity object corresponding to a row from the specified table of the database
     */
    public void updateDatabases(String table, List<InventoryEntity> removeItems){
        //try and catch method to catch SQL exception                  
        try {
            //string for prepared statement to delete the items of given ID from the table given
            String query = "DELETE FROM " + table + "  WHERE ID = ?";
            PreparedStatement myStmt = databaseConnection.prepareStatement(query);

            /* Set desired table to delete from */

            //for loop going through each item to be deleted
            for (InventoryEntity removeItem : removeItems) {
                //setting the value of the prepared statement to the ID we want to delete
                myStmt.setString(1, removeItem.getId());
                myStmt.executeUpdate(); //executing the deletion of such items in inventory in database
            }
            
            myStmt.close(); //releasing the open statement

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL exception in updateDatabases");
        }

    }    
    

    /**
     * method to release open results and connections that have been open 
     */
    public void close() {
        //try and catch method to catch SQL exception
        try {
            results.close();
            databaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL exception in close");
        }
    }
}

