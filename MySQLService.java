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
    private String[][] manu = { //list of manufacturers per furniture type
        {"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"},       //chair
        {"Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies"},    //desk
        {"Office Furnishings", "Furniture Goods", "Fine Office Supplies"},                      //lamp
        {"Office Furnishings", "Furniture Goods", "Fine Office Supplies"}                       //filing
    };

    /**
     * class constructor to set values fields and initialize database connection
     * @param URL string of url
     * @param USER string of username
     * @param PASS string of password
     */
    public MySQLService (String URL, String USER, String PASS) {
        this.URL = URL;
        this.USERNAME = USER;
        this.PASSWORD = PASS;

        //initialize connection to the SQL database, try and catch method to catch SQL exception
        try{
            this.databaseConnection = DriverManager.getConnection(this.URL, this.USERNAME, this.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL expception in initializeConnection");
        }
        
    }

    /**
     * Getter method for manu
     * @return two-dimensional string array of manu containing all the manufacturers per furniture type
     */
    public String[][] getManu() {
        return manu;
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
            Statement myStmt = databaseConnection.createStatement(); //statement to use database
            results = myStmt.executeQuery("SELECT * FROM " + tableName); //selecting all rows from tableName
            
            //declaring boolean arrays of length corresponding to their table properties
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

                int i = 0;
                //assigning boolean values in array according to table values
                while (i < properties.length) {
                    if (results.getNString(i + 3).equals("Y")) { //"Y" for true
                        properties[i] = true;
                    }
                    else {
                        properties[i] = false; //"N" for false
                    }
                    i++;
                }
                //initializing InventoryEntity object
                InventoryEntity nextElement = new InventoryEntity(results.getString("ID"), results.getString("Type"), properties, results.getInt("Price"), results.getString("ManuID"));
                returnList.add(nextElement); //adding object to returnList
            }
            
            myStmt.close(); //closing statement
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL expception in getData");
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
            String query = "DELETE FROM " + table + " WHERE ID = ?";
            PreparedStatement myStmt = databaseConnection.prepareStatement(query); 

            //for loop going through each item to be deleted
            for (int i = 0; i < removeItems.size(); i++) {
                //setting the value of the prepared statement to the ID we want to delete
                myStmt.setString(1, removeItems.get(i).getId());
                myStmt.executeUpdate(); //executing the deletion of such items in inventory in database
            }
            
            myStmt.close(); //releasing the open statement

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL expception in updateDatabases");
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
            System.out.println("SQL expception in close");
        }
    }
}

