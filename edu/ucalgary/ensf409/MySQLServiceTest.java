/**
 * @author Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MySQLServiceTest {
    private final static String USERNAME = "scm";
    private final static String PASSWORD = "ensf409";
    private final static String URL = "jdbc:mysql://localhost/inventory";

    public MySQLServiceTest() {        
    }
    /**
     * Test to verify that the data obtained from MySQLService is valid when using a valid Table name.
     */
    @Test
    public void testGetChairData() {
        System.out.println("getChairData");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("C0914", "Task", new boolean[]{false, false, true, true} , 50 ,"002"));
        expResult.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false} , 50, "002"));
        expResult.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false} , 100 ,"003"));
        expResult.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true} , 50 ,"003"));
        expResult.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false} , 75 ,"004"));
        expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 175 ,"005"));
        expResult.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true} , 50 ,"002"));
        expResult.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false} , 175 ,"002"));
        expResult.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false} , 75 ,"005"));
        expResult.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false} , 75 ,"003"));
        expResult.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false} , 200 ,"003"));
        expResult.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false} , 75 ,"005"));
        
        //getting result list from method getData()
        var result = db.getData("chair");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

    /**
     * Another test to verify that the data obtained from MySQLService is 
     * valid when using a valid Table name (different table used).
     */
    @Test
    public void testGetDeskData() {
        System.out.println("getDeskData");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("D0890", "Traditional", new boolean[]{false, false, true} , 25 ,"002"));
        expResult.add(new InventoryEntity("D1030", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        expResult.add(new InventoryEntity("D1927", "Standing", new boolean[]{true, false, true} , 200 ,"005"));
        expResult.add(new InventoryEntity("D2341", "Standing", new boolean[]{false, true, false} , 100 ,"001"));
        expResult.add(new InventoryEntity("D2746", "Adjustable", new boolean[]{true, false, true} , 250 ,"004"));
        expResult.add(new InventoryEntity("D3682", "Adjustable", new boolean[]{false, false, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D3820", "Standing", new boolean[]{true, false, false} , 150 ,"001"));
        expResult.add(new InventoryEntity("D4231", "Traditional", new boolean[]{false, true, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        expResult.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 200 ,"001"));
        expResult.add(new InventoryEntity("D7373", "Adjustable", new boolean[]{true, true, false} , 350 ,"005"));
        expResult.add(new InventoryEntity("D8675", "Traditional", new boolean[]{true, true, false} , 75 ,"001"));
        expResult.add(new InventoryEntity("D9352", "Traditional", new boolean[]{true, false, true} , 75 ,"002"));
        expResult.add(new InventoryEntity("D9387", "Standing", new boolean[]{true, true, false} , 250 ,"004"));
        
        //getting result list from method getData()
        var result = db.getData("DESK");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

    /**
     * Test to verify that the data obtained from MySQLService is valid when using a valid case insensitive Table name.
     */
    @Test
    public void testGetChairDataCaseInsensitive() {
        System.out.println("getChairDataCaseInsensitive");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false} , 100 ,"003"));
        expResult.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true} , 50 ,"003"));
        expResult.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false} , 75 ,"004"));
        expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 175 ,"005"));
        expResult.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true} , 50 ,"002"));
        expResult.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false} , 175 ,"002"));
        expResult.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false} , 75 ,"005"));
        expResult.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false} , 75 ,"003"));
        expResult.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false} , 200 ,"003"));
        expResult.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false} , 75 ,"005"));
        
        //getting result list from method getData()
        var result = db.getData("ChAiR");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

    /**
     * Test to verify that MySQLService returns an empty List<InventoryEntity> when 
     * an invalid table name (chair type in this case) is supplied.
     */
    @Test
    public void testGetDataInvalidTable() {
        System.out.println("getDataInvalidTable");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list, empty list
        var expResult = new ArrayList<InventoryEntity>();

        //getting result list from method getData()
        var result = db.getData("mesh");

        //compare the two lists, using overwritten equals() method
        assertTrue("Expected returned list is not empty", Arrays.equals(expResult.toArray(), result.toArray()));
    }

    /**
     * Test to verify that MySQLService returns an empty List<InventoryEntity> when an empty string is supplied.
     */
    @Test
    public void testGetDataEmptyInput() {
        System.out.println("getDataEmptyInput");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list, empty list
        var expResult = new ArrayList<InventoryEntity>();

        //getting result list from method getData()
        var result = db.getData("");

        //compare the two lists, using overwritten equals() method
        assertTrue("Expected returned list is not empty", Arrays.equals(expResult.toArray(), result.toArray()));
    }

    /**
     * Test to verify that MySQLService returns an empty List<InventoryEntity> when a 
     * whitespace is added at the end of the table name supplied.
     */
    @Test
    public void testGetDataSpaceAfterTableName() {
        System.out.println("getDataSpaceAfterTableName");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list, empty list
        var expResult = new ArrayList<InventoryEntity>();

        //getting result list from method getData()
        var result = db.getData("chair ");

        //compare the two lists, using overwritten equals() method
        assertTrue("Expected returned list is not empty", Arrays.equals(expResult.toArray(), result.toArray()));
    }

    /**
     * Test to verify that the data list given from a valid table is removed from the inventory database.
     */
    @Test
    public void testUpdateDatabasesTwoItems() {
        System.out.println("UpdateDatabasesTwoItems");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        dataToRemove.add(new InventoryEntity("C0914", "Task", new boolean[]{false, false, true, true} , 50 ,"002"));
        dataToRemove.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false} , 50, "002"));
        String type = "chair";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false} , 100 ,"003"));
        expResult.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true} , 50 ,"003"));
        expResult.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false} , 75 ,"004"));
        expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 175 ,"005"));
        expResult.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true} , 50 ,"002"));
        expResult.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false} , 175 ,"002"));
        expResult.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false} , 75 ,"005"));
        expResult.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false} , 75 ,"003"));
        expResult.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false} , 200 ,"003"));
        expResult.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false} , 75 ,"005"));
        
        //getting result list of table from updated inventory
        var result = db.getData("chair");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

     /**
     * Test to verify nothing happens if already removed items are trying to be removed.
     */
    @Test
    public void testUpdateDatabasesSameItems() {
        System.out.println("UpdateDatabasesSameItems");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        dataToRemove.add(new InventoryEntity("C0914", "Task", new boolean[]{false, false, true, true} , 50 ,"002"));
        dataToRemove.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false} , 50, "002"));
        String type = "chair";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false} , 100 ,"003"));
        expResult.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true} , 50 ,"003"));
        expResult.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false} , 75 ,"004"));
        expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 175 ,"005"));
        expResult.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true} , 50 ,"002"));
        expResult.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false} , 175 ,"002"));
        expResult.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false} , 75 ,"005"));
        expResult.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false} , 75 ,"003"));
        expResult.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true} , 125 ,"003"));
        expResult.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false} , 200 ,"003"));
        expResult.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false} , 75 ,"005"));
        
        //getting result list of table from updated inventory
        var result = db.getData("chair");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

     /**
     * Test to verify nothing happens if invalid items are trying to be removed. Note: only the ID is important
     */
    @Test
    public void testUpdateDatabasesInvalidItems() {
        System.out.println("UpdateDatabasesInvalidItems");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        dataToRemove.add(new InventoryEntity("D0000", "Traditional", new boolean[]{false, false, true} , 25 ,"002"));
        dataToRemove.add(new InventoryEntity("D9999", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        String type = "desk";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("D0890", "Traditional", new boolean[]{false, false, true} , 25 ,"002"));
        expResult.add(new InventoryEntity("D1030", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        expResult.add(new InventoryEntity("D1927", "Standing", new boolean[]{true, false, true} , 200 ,"005"));
        expResult.add(new InventoryEntity("D2341", "Standing", new boolean[]{false, true, false} , 100 ,"001"));
        expResult.add(new InventoryEntity("D2746", "Adjustable", new boolean[]{true, false, true} , 250 ,"004"));
        expResult.add(new InventoryEntity("D3682", "Adjustable", new boolean[]{false, false, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D3820", "Standing", new boolean[]{true, false, false} , 150 ,"001"));
        expResult.add(new InventoryEntity("D4231", "Traditional", new boolean[]{false, true, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        expResult.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 200 ,"001"));
        expResult.add(new InventoryEntity("D7373", "Adjustable", new boolean[]{true, true, false} , 350 ,"005"));
        expResult.add(new InventoryEntity("D8675", "Traditional", new boolean[]{true, true, false} , 75 ,"001"));
        expResult.add(new InventoryEntity("D9352", "Traditional", new boolean[]{true, false, true} , 75 ,"002"));
        expResult.add(new InventoryEntity("D9387", "Standing", new boolean[]{true, true, false} , 250 ,"004"));
        
        //getting result list of table from updated inventory
        var result = db.getData("desk");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

     /**
     * Test to verify only the valid item(s) from the list are removed. Note: only the ID is important
     */
    @Test
    public void testUpdateDatabasesInvalidItemsAndValidItems() {
        System.out.println("UpdateDatabasesInvalidItemsAndValidItems");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        dataToRemove.add(new InventoryEntity("D0890", "Traditional", new boolean[]{false, false, true} , 25 ,"002"));
        dataToRemove.add(new InventoryEntity("D9999", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        dataToRemove.add(new InventoryEntity("D2746", "Adjustable", new boolean[]{true, false, true} , 250 ,"004"));
        dataToRemove.add(new InventoryEntity("D4000", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        String type = "desk";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("D1030", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        expResult.add(new InventoryEntity("D1927", "Standing", new boolean[]{true, false, true} , 200 ,"005"));
        expResult.add(new InventoryEntity("D2341", "Standing", new boolean[]{false, true, false} , 100 ,"001"));
        expResult.add(new InventoryEntity("D3682", "Adjustable", new boolean[]{false, false, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D3820", "Standing", new boolean[]{true, false, false} , 150 ,"001"));
        expResult.add(new InventoryEntity("D4231", "Traditional", new boolean[]{false, true, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        expResult.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 200 ,"001"));
        expResult.add(new InventoryEntity("D7373", "Adjustable", new boolean[]{true, true, false} , 350 ,"005"));
        expResult.add(new InventoryEntity("D8675", "Traditional", new boolean[]{true, true, false} , 75 ,"001"));
        expResult.add(new InventoryEntity("D9352", "Traditional", new boolean[]{true, false, true} , 75 ,"002"));
        expResult.add(new InventoryEntity("D9387", "Standing", new boolean[]{true, true, false} , 250 ,"004"));
        
        //getting result list of table from updated inventory
        var result = db.getData("desk");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

     /**
     * Test to verify nothing happens when empty list is given.
     */
    @Test
    public void testUpdateDatabasesEmptyList() {
        System.out.println("UpdateDatabasesEmptyList");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        String type = "desk";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("D0890", "Traditional", new boolean[]{false, false, true} , 25 ,"002"));
        expResult.add(new InventoryEntity("D1030", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        expResult.add(new InventoryEntity("D1927", "Standing", new boolean[]{true, false, true} , 200 ,"005"));
        expResult.add(new InventoryEntity("D2341", "Standing", new boolean[]{false, true, false} , 100 ,"001"));
        expResult.add(new InventoryEntity("D2746", "Adjustable", new boolean[]{true, false, true} , 250 ,"004"));
        expResult.add(new InventoryEntity("D3682", "Adjustable", new boolean[]{false, false, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D3820", "Standing", new boolean[]{true, false, false} , 150 ,"001"));
        expResult.add(new InventoryEntity("D4231", "Traditional", new boolean[]{false, true, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        expResult.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 200 ,"001"));
        expResult.add(new InventoryEntity("D7373", "Adjustable", new boolean[]{true, true, false} , 350 ,"005"));
        expResult.add(new InventoryEntity("D8675", "Traditional", new boolean[]{true, true, false} , 75 ,"001"));
        expResult.add(new InventoryEntity("D9352", "Traditional", new boolean[]{true, false, true} , 75 ,"002"));
        expResult.add(new InventoryEntity("D9387", "Standing", new boolean[]{true, true, false} , 250 ,"004"));
        
        //getting result list of table from updated inventory
        var result = db.getData("desk");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }

    /**
     * Test to verify nothing happens when invalid type is given.
     */
    @Test
    public void testUpdateDatabasesInvalidType() {
        System.out.println("UpdateDatabasesInvalidType");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating data to be removed and its table name
        var dataToRemove = new ArrayList<InventoryEntity>();
        dataToRemove.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        dataToRemove.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        dataToRemove.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 50 ,"001"));
        String type = "desk ";

        //updating database to remove data rows
        db.updateDatabases(type, dataToRemove);

        //instantiating expected result list
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("D1030", "Adjustable", new boolean[]{false, true, false} , 150, "002"));
        expResult.add(new InventoryEntity("D1927", "Standing", new boolean[]{true, false, true} , 200 ,"005"));
        expResult.add(new InventoryEntity("D2341", "Standing", new boolean[]{false, true, false} , 100 ,"001"));
        expResult.add(new InventoryEntity("D3682", "Adjustable", new boolean[]{false, false, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D3820", "Standing", new boolean[]{true, false, false} , 150 ,"001"));
        expResult.add(new InventoryEntity("D4231", "Traditional", new boolean[]{false, true, true} , 50 ,"005"));
        expResult.add(new InventoryEntity("D4438", "Standing", new boolean[]{false, true, true} , 150 ,"004"));
        expResult.add(new InventoryEntity("D4475", "Adjustable", new boolean[]{false, true, true} , 200 ,"002"));
        expResult.add(new InventoryEntity("D5437", "Adjustable", new boolean[]{true, false, false} , 200 ,"001"));
        expResult.add(new InventoryEntity("D7373", "Adjustable", new boolean[]{true, true, false} , 350 ,"005"));
        expResult.add(new InventoryEntity("D8675", "Traditional", new boolean[]{true, true, false} , 75 ,"001"));
        expResult.add(new InventoryEntity("D9352", "Traditional", new boolean[]{true, false, true} , 75 ,"002"));
        expResult.add(new InventoryEntity("D9387", "Standing", new boolean[]{true, true, false} , 250 ,"004"));
        
        //getting result list of table from updated inventory
        var result = db.getData("desk");

        //sort both lists by IDs
        expResult.sort(new SortInventoryEntitybyID());
        result.sort(new SortInventoryEntitybyID());

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    }
}