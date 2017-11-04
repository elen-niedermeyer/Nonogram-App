package niedermeyer.nonogram.logics;

import java.util.ArrayList;

//TODO: refactoring

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

    public void addCount(int pOuterIndex, int pValue) {
        try {
            ArrayList<FieldCount> currentCounts = allCountsList.get(pOuterIndex);
            currentCounts.add(new FieldCount(pValue, false));
        } catch (IndexOutOfBoundsException e) {
            ArrayList<FieldCount> newCounts = new ArrayList<>();
            newCounts.add(new FieldCount(pValue, false));
            allCountsList.add(newCounts);
        }
    }

    public boolean isEmpty() {
        // check if there is at least one field proved
        for (int outerIndex = 0; outerIndex < allCountsList.size(); outerIndex++) {
            ArrayList<FieldCount> fieldCounts = allCountsList.get(outerIndex);
            for (int innerIndex = 0; innerIndex < fieldCounts.size(); innerIndex++) {
                if (getValue(outerIndex, innerIndex) != 0) {
                    // return false
                    return false;
                }
            }
        }

        // returns true here
        return true;
    }

    public boolean isEmpty(int pOuterIndex) {
        try {
            allCountsList.get(pOuterIndex);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

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
