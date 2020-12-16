package niedermeyer.nonogram.logics;

import java.util.Random;

/**
 * @author Elen Niedermeyer, last modified 2020-12-12
 */
public class NonogramGenerator {

    /**
     * current nonogram
     */
    private int[][] nonogram;
    /**
     * current counts
     */
    private GroupCount rowCount = null;
    private GroupCount columnCount = null;

    private final Random random = new Random();

    /**
     * Getter for {@link #nonogram}.
     *
     * @return {@link #nonogram}
     */
    public int[][] getNonogram() {
        return nonogram;
    }

    /**
     * Setter for {@link #nonogram}.
     *
     * @param pNonogram a two dimensional array that represents a nonogram
     */
    public void setNonogram(int[][] pNonogram) {
        nonogram = pNonogram;
    }

    /**
     * Getter for {@link #rowCount}.
     * Sets {@link #rowCount} if it's not initialized yet and returns it.
     *
     * @return {@link #rowCount}
     */
    public GroupCount getRowCount() {
        if (rowCount == null) {
            rowCount = createRowCount();
        }
        return rowCount;
    }

    /**
     * Getter for {@link #columnCount}.
     * Sets {@link #columnCount} if it's not initialized yet and returns it.
     *
     * @return {@link #columnCount}
     */
    public GroupCount getColumnCount() {
        if (columnCount == null) {
            columnCount = createColumnCount();
        }
        return columnCount;
    }

    /**
     * Makes a new game field. Generates a nonogram and counts the fields in rows and columns.
     * Sets {@link #nonogram}, {@link #rowCount} and {@link #columnCount}.
     * Makes a new nonogram again if all fields of the generated one are empty.
     *
     * @param pNumberOfRows    the number of rows the new game field should have
     * @param pNumberOfColumns the number of columns the new game field should have
     */
    public void makeNewGame(int pNumberOfRows, int pNumberOfColumns) {
        do {
            nonogram = generateNonogram(pNumberOfRows, pNumberOfColumns);
            rowCount = createRowCount();
            columnCount = createColumnCount();
        } while (rowCount.isEmpty());
    }

    /**
     * Generates a new nonogram of the given size.
     * Makes an two dimensional array of the given size. Fills the array with random {@link NonogramConstants#FIELD_EMPTY} and {@link NonogramConstants#FIELD_FILLED}.
     *
     * @param pNumberOfRows    the number of rows the new game field should have
     * @param pNumberOfColumns the number of columns the new game field should have
     * @return a nonogram array
     */
    private int[][] generateNonogram(int pNumberOfRows, int pNumberOfColumns) {
        // make two dimensional array with given size
        int[][] nonogram = new int[pNumberOfRows][pNumberOfColumns];

        // fill the array with random values
        for (int i = 0; i < nonogram.length; i++) {
            for (int j = 0; j < nonogram[i].length; j++) {
                nonogram[i][j] = random.nextInt(2);
            }
        }

        return nonogram;
    }

    /**
     * Counts the groups in the rows of the {@link #nonogram}.
     * Saves the counts in a {@link GroupCount} object.
     *
     * @return a {@link GroupCount} with the row counts of {@link #nonogram}
     */
    private GroupCount createRowCount() {
        GroupCount newRowCount = new GroupCount();

        // iterate over the rows of the nonogram
            for (int rowCount = 0; rowCount < nonogram.length; rowCount++) {
                int groupElements = 0;
                // iterate over each value in the row
                for (int value : nonogram[rowCount]) {
                    if (value == NonogramConstants.FIELD_FILLED) {
                        // here's a group of filled fields
                        // add 1 for each field to the counter
                        groupElements += 1;
                    }
                    if (value == NonogramConstants.FIELD_EMPTY && groupElements != 0) {
                        // here's the end of one group
                        // add the number and resets the counter
                        newRowCount.addValueToList(rowCount, groupElements);
                        groupElements = 0;
                    }
                }
                // end of the for loop for a row

                if (groupElements != 0) {
                    // add the last counter if there is one
                    newRowCount.addValueToList(rowCount, groupElements);
                }

                if (!newRowCount.existsList(rowCount)) {
                    // if the list is empty, there are no filled fields
                    // add 0
                    newRowCount.addValueToList(rowCount, 0);
                }
            }
        return newRowCount;
    }

    /**
     * Counts the groups in the columns of the {@link #nonogram}.
     * Saves the counts in a {@link GroupCount} object.
     *
     * @return a {@link GroupCount} with the column counts of {@link #nonogram}
     */
    private GroupCount createColumnCount() {
        GroupCount newColumnCount = new GroupCount();

        // iterate over the columns of the nonogram
        for (int columnCount = 0; columnCount < nonogram[0].length; columnCount++) {
            int res = 0;
            // iterate over each value in the column
            for (int[] columns : nonogram) {
                if (columns[columnCount] == NonogramConstants.FIELD_FILLED) {
                    // here's a group of filled fields
                    // add 1 for each field to the counter
                    res += 1;
                }
                if (columns[columnCount] == NonogramConstants.FIELD_EMPTY && res != 0) {
                    // here's the end of one group
                    // add the number to the list and resets the counter
                    newColumnCount.addValueToList(columnCount, res);
                    res = 0;
                }
            }
            // end of the for loop for this column

            if (res != 0) {
                // add the last counter to the list if there is one
                newColumnCount.addValueToList(columnCount, res);
            }

            if (!newColumnCount.existsList(columnCount)) {
                // if the list is empty, there are no proved fields
                // add 0 to the list
                newColumnCount.addValueToList(columnCount, 0);
            }
        }

        return newColumnCount;
    }
}

