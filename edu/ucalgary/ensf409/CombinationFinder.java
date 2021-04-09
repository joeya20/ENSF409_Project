/**
 * @author Joey Ah-kiow, Jordan Lonneberg, Juan Villarreal, Mustakim Rahman
 * @version 1.0
 */

package edu.ucalgary.ensf409;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Class used to solve satifiably problem of if an item can be made, and for
 * what price.
 */
public class CombinationFinder {
    /*
     * Saved selections. Used to returning best selection and culling bad branches
     */
    private int bestPrice = -1; /* java has no sum types. Use -1 to indicate infinite price (ie no solution) */
    private InventoryEntity[] bestSelection;

    /* Number of items to solve form */
    private final int number;

    /* selection stacks for solver (current selection and removed selection) */
    private final ArrayDeque<InventoryEntity> cSelection = new ArrayDeque<InventoryEntity>();
    private final ArrayDeque<InventoryEntity> rSelection = new ArrayDeque<InventoryEntity>();

    /* Current inventory item list*/
    private final InventoryEntity[] inventory;

    /**
     * Constructor to generate solver class
     *
     * @param items    List of InventoryEntity's to solve for
     * @param itemType Desired Inventory type to solve for
     * @param number   Number of items to solve for
     */
    public CombinationFinder(List<InventoryEntity> items, String itemType, int number) {

        /*
         * Filter items to only get desired types and then sort by best choices
         * per dollar, finally store as array. Creates shallow copy which is
         * useful for direct comparison to remove items after solving --- which
         * is good for extensibility.
         */
        this.inventory =
                items
                        .stream()
                        .filter(s -> s.getType().equalsIgnoreCase(itemType))
                        .sorted(Comparator.comparingInt(s -> s.getPrice() / s.getProperties().length))
                        .toArray(InventoryEntity[]::new);

        this.number = number;
    }

    public int getBestPrice() {
        return this.bestPrice;
    }

    public InventoryEntity[] getRemovedItems() {
        return this.bestSelection;
    }

    public void solve() {

        assert (inventory.length > 0);

        int[] cs = new int[inventory[0].getProperties().length];
        this.solve(cs, 0, 0, this.number);
    }

    /**
     * Used to solve integer programming problems. Based on solving the
     * problem:
     * <p>
     * let X = zero-or-one vector of selected items
     * let P = price vector of items
     * let Z = properties matrix of items
     * let n = number of items to provide
     * <p>
     * minimize(P.X) constrained by:
     * (Z*X)_i >= n for all i
     *
     * <p>
     * This results in a naive recursive algorithm that for every item in the
     * array checks what the price would be if we use the item, or remove the
     * item. Although the new inventory.sql file has been updated to make all
     * prices per component equal, this is capable of solving a more general
     * case where the per component prices are arbitrary.
     *
     * @param constraintSum integer array of current constraints that are satisfied (equivalent to Z*X).
     * @param cPrice        price of current selection (equivalent to P.X)
     * @param n             index of the current item to check adding inside of this.inventory (ith element of X)
     * @param number        number of items to attempt to create (equivalent to n)
     */

    private void solve(int[] constraintSum, int cPrice, int n, int number) {

        /* cull nodes that we know are bad (current price worse than best known price) */
        if (cPrice > bestPrice && bestPrice != -1) {
            return;
        }

        /* End of tree: have found a solution to constraints with best known
         * price. No need to test adding any more items as that would only
         * increase costs. Saves our current selection as best know price and
         * combination */
        if (isSolution(constraintSum, number)) {
            this.bestSelection = this.cSelection.toArray(InventoryEntity[]::new);
            this.bestPrice = cPrice;
            return;
        }

        /* End of tree: no more items are left to check in inventory */
        if (n == inventory.length) {
            return;
        }

        /* We need a copy of the current constraints to test adding the next
         * item */
        int[] constraintSumCopy = constraintSum.clone();
        boolean[] nElementConstraints = this.inventory[n].getProperties();
        for (int i = 0; i < constraintSumCopy.length; i++) {
            if (nElementConstraints[i]) {
                constraintSumCopy[i] += 1;
            }
        }

        /* Only check the nodes from adding the next item if it moves us towards
         * satisfying constraints */
        if (!Arrays.equals(constraintSumCopy, constraintSum)) {
            cSelection.push(inventory[n]);
            /* check the right-hand-side: best price if we add the next item */
            solve(constraintSumCopy, cPrice + this.inventory[n].getPrice(), n + 1, number);
            cSelection.pop();
        }

        /* check the left-hand-side: best price if we do not add the next item */
        rSelection.push(inventory[n]);
        solve(constraintSum, cPrice, n + 1, number);
        rSelection.pop();

    }

    /* Checks if the current selection satisfies constraints (ie test if
     * (Z*X)_i >= n for all i) */
    private boolean isSolution(int[] constraints, int number) {
        for (var c : constraints) {
            if (c < number) {
                return false;
            }
        }
        return true;
    }
}
