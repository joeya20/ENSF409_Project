// package edu.ucalgary.ensf409;

import java.util.Comparator;

/**
 * Comparator Class to sort lists of InventoryEntity by IDs.
 */
class SortInventoryEntitybyID implements Comparator<InventoryEntity> {
    //Compare using the InventoryEntity's ID
    public int compare(InventoryEntity a, InventoryEntity b) {
        return a.getId().compareTo(b.getId());
    }
}