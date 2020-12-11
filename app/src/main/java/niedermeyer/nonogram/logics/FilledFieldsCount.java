package niedermeyer.nonogram.logics;

import java.util.ArrayList;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class FilledFieldsCount {

    /**
     * The allCountsList list.
     */
    private final ArrayList<ArrayList<CountValue>> allCountsList;

    /**
     * Constructor.
     * Initializes {@link #allCountsList} with a new empty list.
     */
    public FilledFieldsCount() {
        allCountsList = new ArrayList<>();
    }

    /**
     * Getter for {@link #allCountsList}.
     *
     * @return {@link #allCountsList}
     */
    public ArrayList<ArrayList<CountValue>> getCountsList() {
        return allCountsList;
    }

    /**
     * Getter for the list of counts with the given index in {@link #allCountsList}.
     *
     * @param pOuterIndex the index of the list in {@link #allCountsList}
     * @return the list of counts of the given index.
     */
    public ArrayList<CountValue> get(int pOuterIndex) {
        return allCountsList.get(pOuterIndex);
    }

    /**
     * Getter for the count value with the given indices.
     * Gives {@link CountValue#getValue()}.
     *
     * @param pOuterIndex the index of the value in {@link #allCountsList}
     * @param pInnerIndex the index of the value in a child of {@link #allCountsList}
     * @return the count value
     */
    public int getValue(int pOuterIndex, int pInnerIndex) {
        return allCountsList.get(pOuterIndex).get(pInnerIndex).getValue();
    }

    /**
     * Getter for the {@link CountValue#getIsCrossedOut()}.
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
            ArrayList<CountValue> currentCounts = allCountsList.get(pOuterIndex);
            currentCounts.add(new CountValue(pValue));
        } catch (IndexOutOfBoundsException e) {
            // make a new list for this index
            // add the given count to the new list
            ArrayList<CountValue> newCounts = new ArrayList<>();
            newCounts.add(new CountValue(pValue));
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
            ArrayList<CountValue> countValues = allCountsList.get(outerIndex);
            for (int innerIndex = 0; innerIndex < countValues.size(); innerIndex++) {
                if (getValue(outerIndex, innerIndex) != 0) {
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
     * Toggles the {@link CountValue#getIsCrossedOut()} attribute for the value with the given index.
     *
     * @param pOuterIndex the outer index of the value
     * @param pInnerIndex the inner index of the value
     */
    public void toggleCrossedOut(int pOuterIndex, int pInnerIndex) {
        CountValue countValue = allCountsList.get(pOuterIndex).get(pInnerIndex);

        // toggle crossed out
        countValue.setIsCrossedOut(!countValue.getIsCrossedOut());

        // replace count in lists
        allCountsList.get(pOuterIndex).set(pInnerIndex, countValue);
    }

    /**
     * Overrides {@link Object#equals(Object)}.
     *
     * @param pOther an object to compare to this one
     * @return a boolean, true if the {@link FilledFieldsCount}s are equal.
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof FilledFieldsCount) {
            FilledFieldsCount other = (FilledFieldsCount) pOther;

            if (allCountsList.size() != other.getCountsList().size()) {
                // return false if the lists have different sizes
                return false;
            }

            for (int i = 0; i < allCountsList.size(); i++) {
                if (allCountsList.get(i).size() != other.get(i).size()) {
                    // return false if the inner lists have different sizes
                    return false;
                }
            }

            for (int i = 0; i < allCountsList.size(); i++) {
                ArrayList<CountValue> innerList = allCountsList.get(i);
                for (int j = 0; j < innerList.size(); j++) {
                    CountValue countValue = innerList.get(j);
                    if (!countValue.equals(other.get(i).get(j))) {
                        // return false if the field counts are different
                        return false;
                    }
                }
            }

            // return true
            // everything is equal
            return true;
        }

        // return false
        // the given object is of another type
        return false;
    }

}
