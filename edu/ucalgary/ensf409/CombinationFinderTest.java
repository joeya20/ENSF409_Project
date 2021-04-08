/**
 * @author Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class CombinationFinderTest {

    /*
     * Test if the CombinationFinder solve method returns null if no solutions are possible.
     */
    @Test
    public void CombinationFinderNoSolutionsTest() {

        /* This can make a single mesh chair, but not 2. So the result from solve should new null */
        ArrayList<InventoryEntity> testData = new ArrayList<InventoryEntity>();
        testData.add(new InventoryEntity("C001", "mesh", new boolean[]{true, true, false}, 50, "M001"));
        testData.add(new InventoryEntity("C002", "mesh", new boolean[]{true, true, false}, 30, "M001"));
        testData.add(new InventoryEntity("C003", "mesh", new boolean[]{true, false, true}, 25, "M001"));
        testData.add(new InventoryEntity("C004", "mesh", new boolean[]{false, true, false}, 10, "M001"));
        
        var testObj = new CombinationFinder(testData, "mesh", 2);
        testObj.solve();

        assertNull("CombinationFinder.solve() should return null when no solutions exist", testObj.getRemovedItems());

    }

    /*
     * Test that combination finder filters input data to correct object type. Test will include "exec" and "kneel" type
     * inventory items and the constructor will solve
     */
    @Test
    public void CombinationFinderFilterInputTest() {

        ArrayList<InventoryEntity> testData = new ArrayList<InventoryEntity>();
        testData.add(new InventoryEntity("C002", "kneel", new boolean[]{true, false, false, true}, 30, "M001"));
        testData.add(new InventoryEntity("C001", "exec", new boolean[]{true, true, true, true}, 7, "M001"));
        testData.add(new InventoryEntity("C003", "kneel", new boolean[]{false, false, true, false}, 15, "M001"));
        testData.add(new InventoryEntity("C004", "kneel", new boolean[]{false, true, false, false}, 15, "M001"));

        var testObj = new CombinationFinder(testData, "kneel", 1);
        testObj.solve();
        /* The "exec" item should not be included by the solver as the specified type to solve for is "kneel" */
        assertEquals("Combination finder should be able to filter selected type in constructor", 30 + 15 + 15, testObj.getBestPrice());

        var itemsToRemove = testObj.getRemovedItems();

        /* C002, C003, C004 should be present in .getRemovedItems(), but C001 should not */
        assertFalse("CombinationFinder.getRemovedItems() should filter non-pertinent objects before solving",
                idIsPresent(itemsToRemove, testData.get(1).getId()));

        /* @FIXME: add more assertions */
    }

    /*
     * Test that the CombinationFinder finds an optimal solution for multiple simultaneous items at once.
     *
     * REASON: If CombinationFinder only successively considers the weighted set cover problem, it will find a solution that is non-optimal.
     * To find the correct solution, Combination finder must solve for build all items at once.
     */
    @Test
    public void CombinationFinderTestOptimality() {
        /* The optimal solution to building 2 items of type large is select Z001, Z002, Z004 (no Z003)*/
        ArrayList<InventoryEntity> testData = new ArrayList<InventoryEntity>();        
        testData.add(new InventoryEntity("Z001", "large", new boolean[]{true, true, false}, 50, "M001"));
        testData.add(new InventoryEntity("Z002", "large", new boolean[]{true, false, true}, 50, "M001"));
        testData.add(new InventoryEntity("Z003", "large", new boolean[]{true, true, true}, 100, "M001"));
        testData.add(new InventoryEntity("Z004", "large", new boolean[]{false, true, true}, 50, "M001"));

        var testObj = new CombinationFinder(testData, "large", 2);
        testObj.solve();

        var itemsToRemove = testObj.getRemovedItems();

        assertEquals("CombinationFinder should find the optimal solution when multiple solutions exist for making multiple items",
                50 + 50 + 50, testObj.getBestPrice());

        /* This test is partially redundant, but is included to make it clear that Z004 should not be in the returned solution */
        assertFalse("CombinationFinder should find the optimal solution when multiple solutions exist for making multiple items",
                idIsPresent(itemsToRemove, "Z003"));
    }

    /*
     * Test the example data from inventory.sql based on the project outline. Gives a good sanity check.
     */
    @Test
    public void CombinationFinderTestProjectExample() {
        /* This is the default chair table data. Pulled straight from the .sql file */
        ArrayList<InventoryEntity> testData = new ArrayList<InventoryEntity>();
        testData.add(new InventoryEntity("C1320", "Kneeling", new boolean[]{true, false, false, false}, 50, "002"));
        testData.add(new InventoryEntity("C3405", "Task", new boolean[]{true, true, false, false}, 100, "003"));
        testData.add(new InventoryEntity("C9890", "Mesh", new boolean[]{false, true, false, true}, 50, "003"));
        testData.add(new InventoryEntity("C7268", "Executive", new boolean[]{false, false, true, false}, 75, "004"));
        testData.add(new InventoryEntity("C0942", "Mesh", new boolean[]{true, false, true, true}, 175, "005"));
        testData.add(new InventoryEntity("C4839", "Ergonomic", new boolean[]{false, false, false, true}, 50, "002"));
        testData.add(new InventoryEntity("C2483", "Executive", new boolean[]{true, true, false, false}, 175, "002"));
        testData.add(new InventoryEntity("C5789", "Ergonomic", new boolean[]{true, false, false, true}, 125, "003"));
        testData.add(new InventoryEntity("C3819", "Kneeling", new boolean[]{false, false, true, false}, 75, "005"));
        testData.add(new InventoryEntity("C5784", "Executive", new boolean[]{true, false, false, true}, 150, "004"));
        testData.add(new InventoryEntity("C6748", "Mesh", new boolean[]{true, false, false, false}, 75, "003"));
        testData.add(new InventoryEntity("C0914", "Task", new boolean[]{false, false, true, true}, 50, "002"));
        testData.add(new InventoryEntity("C1148", "Task", new boolean[]{true, false, true, true}, 125, "003"));
        testData.add(new InventoryEntity("C5409", "Ergonomic", new boolean[]{true, true, true, false}, 200, "003"));
        testData.add(new InventoryEntity("C8138", "Mesh", new boolean[]{false, false, true, false}, 75, "005"));

        var testObj = new CombinationFinder(testData, "mesh", 1);
        testObj.solve();

        /* We are told that there should be 3 items */
        assertEquals("CombinationFinder should return the same output as described in the project outline",
                3, testObj.getRemovedItems().length);
        assertEquals("CombinationFinder should return the same price as the inventory.sql file example does",
                200, testObj.getBestPrice());

    }

    /**
     * Helper function that determines if an array of InventoryEntities,
     *
     * @param inventory InventoryEntity array to check.
     * @param id        id string to check for in inventory.
     * @return true if inventory contains an item with id of id, false otherwise.
     */
    private boolean idIsPresent(InventoryEntity[] inventory, String id) {
        for (var item : inventory) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
