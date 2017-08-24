package niedermeyer.nonogram.logics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Elen Niedermeyer, last modified 2017-07-31
 */

public class NonogramGenerator {

    private int[][] nonogram;
    private Map<Integer, ArrayList<Integer>> countsRow;
    private Map<Integer, ArrayList<Integer>> countsColumn;

    private Random random = new Random();

    /**
     * Returns the two-dimensional nonogram array.
     *
     * @return {@link #nonogram}
     */
    public int[][] getNonogram() {
        return nonogram;
    }

    /**
     * Sets the {@link #nonogram}.
     *
     * @param pNonogram a two-dimensional array that represents a nonogram
     */
    public void setNonogram(int[][] pNonogram) {
        nonogram = pNonogram;
    }

    /**
     * Returns the map which contains the counts for the rows.
     *
     * @return {@link #countsRow}
     */
    public Map<Integer, ArrayList<Integer>> getCountsRow() {
        countsRow = countProvedFieldsPerRow();
        return countsRow;
    }

    /**
     * Returns the map which contains the counts for the columns.
     *
     * @return {@link #countsColumn}
     */
    public Map<Integer, ArrayList<Integer>> getCountsColumn() {
        countsColumn = countProvedFieldsPerColumn();
        return countsColumn;
    }

    /**
     * Makes a new game field. Generates a nonogram and counts the field in rows and columns.
     * Sets {@link #nonogram}, {@link #countsRow} and {@link #countsColumn}. Makes a new nonogram if all fields of the generated one are empty.
     *
     * @param pNumberOfRows    the number of rows the new game field should have
     * @param pNumberOfColumns the number of columns the new game field should have
     */
    public void makeNewGame(int pNumberOfRows, int pNumberOfColumns) {
        boolean isOnlyEmpty;
        do {
            nonogram = generateNonogram(pNumberOfRows, pNumberOfColumns);
            countsRow = countProvedFieldsPerRow();
            countsColumn = countProvedFieldsPerColumn();

            // check if there is at least one field proved
            isOnlyEmpty = true;
            for (ArrayList<Integer> list : countsRow.values()) {
                for (Integer count : list) {
                    if (count != NonogramFields.EMPTY.getValue()) {
                        isOnlyEmpty = false;
                        break;
                    }
                }
                if (!isOnlyEmpty) {
                    break;
                }
            }
        } while (isOnlyEmpty);
    }

    /**
     * Generates a new nonogram of the given size.
     * Makes an two-dimensional array of the given size. Fills the array with random number 0 or 1.
     *
     * @param pNumberOfRows    the number of rows the new game field should have
     * @param pNumberOfColumns the number of columns the new game field should have
     * @return a nonogram array
     */
    private int[][] generateNonogram(int pNumberOfRows, int pNumberOfColumns) {
        // make two dimensional array with given size
        int[][] nonogram = new int[pNumberOfRows][pNumberOfColumns];

        // fill the array with random 0 or 1
        for (int i = 0; i < nonogram.length; i++) {
            for (int j = 0; j < nonogram[i].length; j++) {
                int k = random.nextInt(2);
                if (k == 1) {
                    nonogram[i][j] = NonogramFields.PROVED.getValue();
                } else {
                    nonogram[i][j] = NonogramFields.EMPTY.getValue();
                }
            }
        }

        return nonogram;
    }

    /**
     * Counts the groups in the rows of the {@link #nonogram}.
     * Saves the counts in a map. Key of the map is the number of the row, value is an array list with the counts in the correct order.
     *
     * @return a map with the row counts of {@link #nonogram}
     */
    private Map<Integer, ArrayList<Integer>> countProvedFieldsPerRow() {
        HashMap<Integer, ArrayList<Integer>> provedFields = new HashMap<>();

        // iterate over the rows of the nonogram
        for (int i = 0; i < nonogram.length; i++) {
            int res = 0;
            ArrayList<Integer> results = new ArrayList<>();
            // iterate over each value in the row
            for (int j = 0; j < nonogram[i].length; j++) {
                if (nonogram[i][j] == NonogramFields.PROVED.getValue()) {
                    // here's a group of proved fields
                    // add 1 for each field to the counter
                    res += 1;
                }
                if (nonogram[i][j] == NonogramFields.EMPTY.getValue() && res != 0) {
                    // here's the end of one group
                    // add the number to the list and resets the counter
                    results.add(res);
                    res = 0;
                }
            }

            // end of the for loop for this row

            if (res != 0) {
                // add the last counter to the list if there is one
                results.add(res);
            }

            if (results.isEmpty()) {
                // if the list is empty, there are no proved fields
                // add 0 to the list
                results.add(0);
            }

            // add the counts for this row to the map
            // key is the number of row
            provedFields.put(i, results);
        }

        return provedFields;
    }

    /**
     * Counts the groups in the columns of the {@link #nonogram}.
     * Saves the counts in a map. Key of the map is the number of the column, value is an array list with the counts in the correct order.
     *
     * @return a map with the column counts of {@link #nonogram}
     */
    private Map<Integer, ArrayList<Integer>> countProvedFieldsPerColumn() {
        HashMap<Integer, ArrayList<Integer>> provedFields = new HashMap<>();

        // iterate over the columns of the nonogram
        for (int i = 0; i < nonogram[0].length; i++) {
            int res = 0;
            ArrayList<Integer> results = new ArrayList<>();
            // iterate over each value in the column
            for (int j = 0; j < nonogram.length; j++) {
                if (nonogram[j][i] == NonogramFields.PROVED.getValue()) {
                    // here's a group of proved fields
                    // add 1 for each field to the counter
                    res += 1;
                }
                if (nonogram[j][i] == NonogramFields.EMPTY.getValue() && res != 0) {
                    // here's the end of one group
                    // add the number to the list and resets the counter
                    results.add(res);
                    res = 0;
                }
            }

            // end of the for loop for this column

            if (res != 0) {
                // add the last counter to the list if there is one
                results.add(res);
            }

            if (results.isEmpty()) {
                // if the list is empty, there are no proved fields
                // add 0 to the list
                results.add(0);
            }

            // add the counts for this column to the map
            // key is the number of column
            provedFields.put(i, results);
        }

        return provedFields;
    }
}
