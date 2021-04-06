// package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class RunnerTest {
    private final static String USERNAME = "joeya1";
    private final static String PASSWORD = "Deanjo_19";
    private final static String URL = "jdbc:mysql://localhost/inventory";

    public RunnerTest() {
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
        expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 100 ,"005"));
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
     * Test to verify that MySQLService returns an empty List<InventoryEntity> when an invalid table name is supplied.
     */
    @Test
    public void testGetDataInvalidTable() {
        System.out.println("getDataInvalidTable");

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list, empty list
        var expResult = new ArrayList<InventoryEntity>();

        //getting result list from method getData()
        var result = db.getData("invalid");

        //compare the two lists, using overwritten equals() method
        assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());
    }
}

/**
 * Comparator Class to sort lists of InventoryEntity by IDs.
 */
class SortInventoryEntitybyID implements Comparator<InventoryEntity> {
    //Compare using the InventoryEntity's ID
    public int compare(InventoryEntity a, InventoryEntity b) {
        return a.getId().compareTo(b.getId());
    }
}