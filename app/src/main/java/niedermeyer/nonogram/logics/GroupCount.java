package niedermeyer.nonogram.logics;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
public class GroupCount implements Serializable {

    /**
     * The counts list.
     */
    private final ArrayList<ArrayList<GroupCountCell>> counts;

    /**
     * Constructor.
     * Initializes {@link #counts} with a new empty list.
     */
    public GroupCount() {
        counts = new ArrayList<>();
    }

    public GroupCount(ArrayList<ArrayList<GroupCountCell>> counts) {
        this.counts = counts;
    }

    /**
     * Getter for {@link #counts}.
     *
     * @return {@link #counts}
     */
    public ArrayList<ArrayList<GroupCountCell>> getCounts() {
        return counts;
    }

    /**
     * Getter for the list of counts with the given index in {@link #counts}.
     *
     * @param pOuterIndex the index of the list in {@link #counts}
     * @return the list of counts of the given index.
     */
    public ArrayList<GroupCountCell> getList(int pOuterIndex) {
        return counts.get(pOuterIndex);
    }

    /**
     * Getter for the count value with the given indices.
     * Gives {@link GroupCountCell#getValue()}.
     *
     * @param pOuterIndex the index of the value in {@link #counts}
     * @param pInnerIndex the index of the value in a child of {@link #counts}
     * @return the count value
     */
    public int getValue(int pOuterIndex, int pInnerIndex) {
        return counts.get(pOuterIndex).get(pInnerIndex).getValue();
    }

    /**
     * Getter for the {@link GroupCountCell#isCrossedOut()}.
     *
     * @param pOuterIndex the index of the count in {@link #counts}
     * @param pInnerIndex the index of the count in a child of {@link #counts}
     * @return a boolean saying whether the count is crossed out or not
     */
    public boolean isValueCrossedOut(int pOuterIndex, int pInnerIndex) {
        return counts.get(pOuterIndex).get(pInnerIndex).isCrossedOut();
    }

    /**
     * Adds a count to the end of the list at the given index in {@link #counts}.
     *
     * @param pOuterIndex index of the count
     * @param pValue      the count's value
     */
    public void addValueToList(int pOuterIndex, int pValue) {
        try {
            // add the given count to the list
            ArrayList<GroupCountCell> currentList = counts.get(pOuterIndex);
            currentList.add(new GroupCountCell(pValue, false));
        } catch (IndexOutOfBoundsException e) {
            // make a new list for this index
            // add the given count to the new list
            ArrayList<GroupCountCell> newList = new ArrayList<>();
            newList.add(new GroupCountCell(pValue, false));
            counts.add(newList);
        }
    }

    /**
     * Looks if the count is empty. In this case it means, there are only zeros.
     *
     * @return true, if the count is empty, false otherwise
     */
    public boolean isEmpty() {
        // check if there is at least one field filled
        for (ArrayList<GroupCountCell> currentList : counts) {
            for (GroupCountCell currentValue : currentList) {
                if (currentValue.getValue() != 0) {
                    // there's an value != 0
                    // return false
                    return false;
                }
            }
        }

        // returns true here
        // there are only zeros in the count
        return true;
    }

    /**
     * Looks if the list with the given index exists.
     *
     * @param pOuterIndex the index of the list
     * @return true if the list exists, false otherwise
     */
    public boolean existsList(int pOuterIndex) {
        try {
            counts.get(pOuterIndex);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Toggles the {@link GroupCountCell#isCrossedOut()} attribute for the value with the given index.
     *
     * @param pOuterIndex the outer index of the value
     * @param pInnerIndex the inner index of the value
     */
    public void toggleCrossedOut(int pOuterIndex, int pInnerIndex) {
        GroupCountCell countValue = counts.get(pOuterIndex).get(pInnerIndex);

        // toggle crossed out
        countValue.setCrossedOut(!countValue.isCrossedOut());

        // replace count in lists
        counts.get(pOuterIndex).set(pInnerIndex, countValue);
    }

    /**
     * Overrides {@link Object#equals(Object)}.
     *
     * @param pOther an object to compare to this one
     * @return a boolean, true if the {@link GroupCount}s are equal.
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof GroupCount) {
            GroupCount other = (GroupCount) pOther;

            return counts.equals(other.getCounts());
        }

        // return false
        // the given object is of another type
        return false;
    }

}
