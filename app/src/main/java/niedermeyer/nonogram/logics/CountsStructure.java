package niedermeyer.nonogram.logics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Elen Niedermeyer, last modified 2017-09-08
 */
public class CountsStructure {

    private Map<Integer, ArrayList<Integer>> counts;
    private Map<Integer, ArrayList<Boolean>> isStriked;

    public CountsStructure() {
        counts = new HashMap<Integer, ArrayList<Integer>>();
        isStriked = new HashMap<Integer, ArrayList<Boolean>>();
    }

    public void addCount(int pKeyRowOrColumn, int pValue) {
        if (counts.containsKey(pKeyRowOrColumn)) {
            ArrayList<Integer> countArray = counts.get(pKeyRowOrColumn);
            countArray.add(pValue);
            ArrayList<Boolean> isStrikedArray = isStriked.get(pKeyRowOrColumn);
            isStrikedArray.add(false);
        } else {
            ArrayList<Integer> countArray = new ArrayList<Integer>();
            countArray.add(pValue);
            counts.put(pKeyRowOrColumn, countArray);
            ArrayList<Boolean> isStrikedArray = new ArrayList<Boolean>();
            isStrikedArray.add(false);
            isStriked.put(pKeyRowOrColumn, isStrikedArray);
        }
    }

    public ArrayList<Integer> get(int pKeyRowOrColumn) {
        return counts.get(pKeyRowOrColumn);
    }

    public int get(int pKeyRowOrColumn, int pIndex) {
        ArrayList<Integer> innerList = counts.get(pKeyRowOrColumn);
        return innerList.get(pIndex);
    }

    public int getSizeOuter() {
        return counts.size();
    }

    public int getSizeInner(int pKeyRowOrColumn) {
        ArrayList<Integer> innerList = counts.get(pKeyRowOrColumn);
        return innerList.size();
    }

    public boolean isEmpty() {
        // check if there is at least one field proved
        boolean isOnlyEmpty = true;
        for (ArrayList<Integer> list : counts.values()) {
            for (Integer count : list) {
                if (count != 0) {
                    isOnlyEmpty = false;
                    break;
                }
            }
            if (!isOnlyEmpty) {
                break;
            }
        }
        return isOnlyEmpty;
    }

    public boolean isRowOrColumnEmpty(int pKeyRowOrColumn) {
        if (counts.containsKey(pKeyRowOrColumn)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isStriked(int outerIndex, int innerIndex) {
        ArrayList<Boolean> x = isStriked.get(outerIndex);
        return x.get(innerIndex);
    }

    public void toggleStriked(int outerIndex, int innerIndex) {
        ArrayList<Boolean> x = isStriked.get(outerIndex);
        boolean isStriked = x.get(innerIndex);
        if (isStriked) {
            x.set(innerIndex, false);
        } else {
            x.set(innerIndex, true);
        }
    }


}
