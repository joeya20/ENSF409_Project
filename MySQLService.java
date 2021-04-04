
package edu.ucalgary.ensf409;
import java.util.*;
import java.sql.*;

//class MySQLService uses inventory SQL database and will read or delete elements of the database.
public class MySQLService {
    //class fields
    private final String URL; 
    private final String USERNAME; 
    private final String PASSWORD; 
    private Connection databaseConnection; //connection to SQL
    private ResultSet results; //result set from statement execution
    private List<Manufacturer> manufacturerData; //all manufacturer data
    
    //class constructor
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
        
        //intializes manufacturerData
        this.manufacturerData = getManufacturers();
    }

    //getting all manufacturer data
    public List<Manufacturer> getManufacturers() {
        List<Manufacturer> returnList = new ArrayList<Manufacturer>();

        //try and catch method to catch SQL exception
        try {                    
            Statement myStmt = databaseConnection.createStatement(); //statement to use database
            results = myStmt.executeQuery("SELECT * FROM manufacturer"); //selecting all rows from tableName
            
            //declaring boolean arrays of length corresponding to their type
            while (results.next()){               
                //System.out.println(results.getString("ManuID") + "  " + results.getString("Name") + "  " + results.getString("Phone") + results.getString("Province"));
                //initializing Manufacturer object
                Manufacturer nextManufacturer = new Manufacturer(results.getString("ManuID"), results.getString("Name"), results.getString("Phone"), results.getString("Province"));
                returnList.add(nextManufacturer); //adding object to returnList
            }
            
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL expception in getData");
        }

        //returns list of manufacturer objects
        return returnList;
    }

    //getter methods for URL, USERNAME and PASSWORD
    /*
    public String getUrl() {
        return this.URL;
    }
    public String getUsername() {
        return this.USERNAME;
    }
    public String getPassword() {
        return this.PASSWORD;
    }*/

    //method returns a list of Inventory Entitys from the specified input table fo the inventory database
    //public void getData(String tableName){
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
                //System.out.println(results.getString("ID") + "  " + results.getString("Type") + "  " + properties[0] + " " + properties[1] + " " + results.getInt("Price") + "  " + results.getString("ManuID"));
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

    
    //method to delete items of given table from the inventory database
    //public void updateDatabases(String table, String removeItems){
    public void updateDatabases(String table, List<InventoryEntity> removeItems){
        //try and catch method to catch SQL exception                  
        try {
            //string for prepared statement to delete the items of given ID from the table given
            String query = "DELETE FROM " + table + " WHERE ID = ?";
            PreparedStatement myStmt = databaseConnection.prepareStatement(query); 

            //for loop going through each item to be deleted
            for (int i = 0; i < removeItems.size(); i++) {
                //setting the value of the prepared statement to the ID we want to delete
                myStmt.setString(1, removeItems.get(i).id);
                //myStmt.setString(1, removeItems);
                myStmt.executeUpdate(); //executing the deletion of such items in inventory in database
            }
            
            myStmt.close(); //releasing the open statement

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQL expception in updateDatabases");
        }

    }    
    

    //method to release open results and connections that have been open
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
    
    //main method used to test our program
    /*public static void main(String[] args) {

        MySQLService myJDBC = new MySQLService("jdbc:mysql://localhost/inventory","root","Ju14154830");
        //myJDBC.initializeConnection();
        
        //myJDBC.getData("desk");
       
        //myJDBC.updateDatabases("Desk", "D0890");
        
        myJDBC.close();
    }*/
}

