// package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RunnerTest {
    private final static String USERNAME = "joeya1";
    private final static String PASSWORD = "Deanjo_19";
    private final static String URL = "jdbc:mysql://localhost/inventory";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream systemOut = System.out;

    public RunnerTest() {
    }

    /**
     * Tests that the program removes the rows from the database when a valid input is supplied
     * Must run .sql script to restore database first.
     */
    @Test
    public void testProcessInputUpdateDatabase() {

        //call processInput, expected to remove chairs with ID C0914 and C3405 from database
        Runner.processInput("Chair", "task", 1);

        //creating connection to database
        var db = new MySQLService(URL, USERNAME, PASSWORD);

        //instantiating expected result list, all chairs other than chairs with ID C0914 and C3405
        var expResult = new ArrayList<InventoryEntity>();
        expResult.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false} , 50, "002"));
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

        //compare expResult and result
        assertArrayEquals("Database was not updated", expResult.toArray(), result.toArray());
        //verify that the IDs of outputted rows match the IDs of the removed rows
        assertEquals("Incorrect output", "Purchase C3405, and C0914 for $150", outputStream.toString().trim());
    }

    /**
     * Test to verify a valid order form, with the proper format 
     * and information is created if valid inputs are supplied.
     * Does not affect the database.
     * @throws IOException
     */
    @Test
    public void testGenerateOrderForm() throws IOException {
        File outputFile = new File("testOrderForm.txt");

        //create test inventoryEntitys to output to file
        InventoryEntity[] test = new InventoryEntity[] {
            new InventoryEntity("ID1", "type", null, 50, "MANUID"), 
            new InventoryEntity("ID2", "test type", null, 50, "MANUID")
        };
        
        //create "test.txt"
        Runner.generateOrderForm(outputFile, "test category", "test type", 10, test, 100);

        //get all lines from output file "test.txt"
        List<String> result = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));

        //expected result
        List<String> expResult = new ArrayList<String>();
        expResult.add("Furniture Order Form");
        expResult.add("");
        expResult.add("Faculty: ");
        expResult.add("Contact: ");
        expResult.add("Date: ");
        expResult.add("");
        expResult.add("Request: Test type Test category, 10");
        expResult.add("");
        expResult.add("Item(s) Ordered:");
        expResult.add("ID1");
        expResult.add("ID2");
        expResult.add("");
        expResult.add("Total Price: $100");

        //compare expResult and result
        assertArrayEquals("Generated Form does not contain correct information/formatting", expResult.toArray(), result.toArray());
    }

    
    /**
     * Tests the program output when supplied with valid inputs.
     * Must run .sql script to restore database first.
     */
    @Test
    public void testProcessInputFulfillableOrder() {
        //call processInput to output message to outputStream
        Runner.processInput("desk", "adjustable", 1);

        //expected result
        String expString = "Purchase D5437, and D4475 for $400";

        //compare string printed to outputStream to expected result
        assertEquals("Output message is incorrect", expString, outputStream.toString().trim());
    }

    /**
     * Tests the program output when supplied with an unfulfillable order.
     * Does not affect database since the order cannot be fulfilled.
     */
    @Test
    public void testProcessInputUnfulfillableOrder() {
        //call processInput to output message to outputStream
        Runner.processInput("chair", "task", 10);

        //expected result
        String expString = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are Office Furnishings, Chairs R Us, Furniture Goods, Fine Office Supplies.";

        //compare string printed to outputStream to expected result
        assertEquals("Output message is incorrect", expString, outputStream.toString().trim());
    }

    /**
     * Tests the program output when supplied with an invalid category.
     * Does not affect database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInputInvalidCategory() {
        //call processInput to output message to outputStream
        Runner.processInput("invalid", "task", 10);
    }

    /**
     * Tests the program output when supplied with an invalid type.
     * Does not affect database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInputInvalidType() {
        //call processInput to output message to outputStream
        Runner.processInput("chair", "invalid", 10);
    }

    /**
     * Tests the program output when supplied with an invalid amount.
     * Does not affect database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProcessInputInvalidAmount() {
        //call processInput to output message to outputStream
        Runner.processInput("chair", "task", 0);
    }

    /**
     * Tests the program output when supplied with non-lowercase arguments.
     * Must run .sql script to restore database first.
     */
    @Test
    public void testProcessInputEdgeCase0() {
        //call processInput
        Runner.processInput("ChaIr", "mESh", 1);

        //expected result
        String expString = "Purchase C8138, C6748, and C9890 for $200";

        //compare string printed to outputStream to expected result
        assertEquals("Output message is incorrect", expString, outputStream.toString().trim());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testProcessInputEdgeCase1() {
        //call processInput
        Runner.processInput("  ChaIr ", "  mESh  ", 1);
    }

    // /**
    //  * Tests the program output when supplied with valid inputs.
    //  * Must run .sql script to restore database first.
    //  */
    // @Test
    // public void testProcessInputEdgeCase1() {
    //     // set output stream to ByteArrayStream
    //     System.setOut(new PrintStream(outputStream));

    //     //call processInput
    //     Runner.processInput("ChaIr", "mESh", 1);

    //     //expected result
    //     String expString = "Purchase C8138, C6748, and C9890 for $200";

    //     //compare string printed to outputStream to expected result
    //     assertEquals("Output message is incorrect", expString, outputStream.toString().trim());

    //     //restore output stream to System.Out
    //     System.setOut(systemOut);
    // }

    /**
     * set output stream to ByteArrayStream
    */
    @Before
    public void setUpOutputStream() {
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * restore output stream to System.Out
    */
    @After
    public void restoreOutputStream() {
        System.setOut(systemOut);
    }
    
    // /**
    //  * Test to verify that the data obtained from MySQLService is valid when using a valid Table name.
    //  */
    // @Test
    // public void testGetChairData() {
    //     System.out.println("getChairData");

    //     //creating connection to database
    //     var db = new MySQLService(URL, USERNAME, PASSWORD);

    //     //instantiating expected result list
    //     var expResult = new ArrayList<InventoryEntity>();
    //     expResult.add(new InventoryEntity("C0914", "Task", new boolean[]{false, false, true, true} , 50 ,"002"));
    //     expResult.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false} , 50, "002"));
    //     expResult.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false} , 100 ,"003"));
    //     expResult.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true} , 50 ,"003"));
    //     expResult.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false} , 75 ,"004"));
    //     expResult.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true} , 100 ,"005"));
    //     expResult.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true} , 50 ,"002"));
    //     expResult.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false} , 175 ,"002"));
    //     expResult.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true} , 125 ,"003"));
    //     expResult.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false} , 75 ,"005"));
    //     expResult.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true} , 150 ,"004"));
    //     expResult.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false} , 75 ,"003"));
    //     expResult.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true} , 125 ,"003"));
    //     expResult.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false} , 200 ,"003"));
    //     expResult.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false} , 75 ,"005"));
        
    //     //getting result list from method getData()
    //     var result = db.getData("chair");

    //     //sort both lists by IDs
    //     expResult.sort(new SortInventoryEntitybyID());
    //     result.sort(new SortInventoryEntitybyID());

    //     //compare the two lists, using overwritten equals() method
    //     assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());   //expResult[i].tostring.equals(result[i].toString())
    // }

    // /**
    //  * Test to verify that MySQLService returns an empty List<InventoryEntity> when an invalid table name is supplied.
    //  */
    // @Test
    // public void testGetDataInvalidTable() {
    //     System.out.println("getDataInvalidTable");

    //     //creating connection to database
    //     var db = new MySQLService(URL, USERNAME, PASSWORD);

    //     //instantiating expected result list, empty list
    //     var expResult = new ArrayList<InventoryEntity>();

    //     //getting result list from method getData()
    //     var result = db.getData("invalid");

    //     //compare the two lists, using overwritten equals() method
    //     assertArrayEquals("Expected and obtained lists are not identical", expResult.toArray(), result.toArray());
    // }
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