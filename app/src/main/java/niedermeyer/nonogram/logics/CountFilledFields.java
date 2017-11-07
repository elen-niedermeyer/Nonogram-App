package niedermeyer.nonogram.logics;

import java.util.ArrayList;

/**
 * @author Elen Niedermeyer, last modified 2017-11-04
 */
public class CountFilledFields {

    /**
     * The allCountsList list.
     */
    private ArrayList<ArrayList<FieldCount>> allCountsList;

    /**
     * Constructor.
     * Initializes {@link #allCountsList} with a new empty list.
     */
    public CountFilledFields() {
        allCountsList = new ArrayList<ArrayList<FieldCount>>();
    }

    /**
     * Getter for {@link #allCountsList}.
     *
     * @return {@link #allCountsList}
     */
    public ArrayList<ArrayList<FieldCount>> getCountsList() {
        return allCountsList;
    }

    /**
     * Getter for the list of counts with the given index in {@link #allCountsList}.
     *
     * @param pOuterIndex the index of the list in {@link #allCountsList}
     * @return the list of counts of the given index.
     */
    public ArrayList<FieldCount> get(int pOuterIndex) {
        return allCountsList.get(pOuterIndex);
    }

    /**
     * Getter for the count value with the given indices.
     * Gives {@link FieldCount#value}.
     *
     * @param pOuterIndex the index of the value in {@link #allCountsList}
     * @param pInnerIndex the index of the value in a child of {@link #allCountsList}
     * @return the count value
     */
    public int getValue(int pOuterIndex, int pInnerIndex) {
        return allCountsList.get(pOuterIndex).get(pInnerIndex).getValue();
    }

    /**
     * Getter for the {@link FieldCount#isCrossedOut}.
     *
     * @param pOuterIndex the index of the count in {@link #allCountsList}
     * @param pInnerIndex the index of the count in a child of {@link #allCountsList}
     * @return a boolean saying whether the count is crossed out or not
     */
    public boolean isValueCrossedOut(int pOuterIndex, int pInnerIndex) {
        return allCountsList.get(pOuterIndex).get(pInnerIndex).getIsCrossedOut();
    }

    /**
     * Adds a count.
     *
     * @param pOuterIndex index of the count
     * @param pValue      the count's value
     */
    public void addCount(int pOuterIndex, int pValue) {
        try {
            // add the given count to the list
            ArrayList<FieldCount> currentCounts = allCountsList.get(pOuterIndex);
            currentCounts.add(new FieldCount(pValue, false));
        } catch (IndexOutOfBoundsException e) {
            // make a new list for this index
            // add the given count to the new list
            ArrayList<FieldCount> newCounts = new ArrayList<>();
            newCounts.add(new FieldCount(pValue, false));
            allCountsList.add(newCounts);
        }
    }

    /**
     * Looks if the count is empty. In this case it means, there are only zeros.
     *
     * @return true, if the count is empty, false otherwise
     */
    public boolean isEmpty() {
        // check if there is at least one field proved
        for (int outerIndex = 0; outerIndex < allCountsList.size(); outerIndex++) {
            ArrayList<FieldCount> fieldCounts = allCountsList.get(outerIndex);
            for (int innerIndex = 0; innerIndex < fieldCounts.size(); innerIndex++) {
                if (getValue(outerIndex, innerIndex) != 0) {
                    // there's an value != 0
                    // return false
                    return false;
                }
            }
        }

        // returns true here
        // there are only zeros int the count
        return true;
    }

    /**
     * Looks if the list with the given index exists.
     *
     * @param pOuterIndex the index of the list
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty(int pOuterIndex) {
        try {
            allCountsList.get(pOuterIndex);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    /**
     * Toggles the {@link FieldCount#isCrossedOut} attribute for the value with the given index.
     *
     * @param pOuterIndex the outer index of the value
     * @param pInnerIndex the inner index of the value
     */
    public void toggleCrossedOut(int pOuterIndex, int pInnerIndex) {
        FieldCount fieldCount = allCountsList.get(pOuterIndex).get(pInnerIndex);
        if (fieldCount.getIsCrossedOut()) {
            // count is crossed out
            // now it shouldn't be crossed out
            fieldCount.setIsCrossedOut(false);
        } else {
            // count is not crossed out
            // now it should be crossed out
            fieldCount.setIsCrossedOut(true);
        }

        // replace count in lists
        allCountsList.get(pOuterIndex).set(pInnerIndex, fieldCount);
    }
}
