package niedermeyer.nonogram.logics;

import java.util.Random;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class NonogramGenerator {

    /**
     * current nonogram
     */
    private int[][] nonogram;
    /**
     * current counts
     */
    private FilledFieldsCount countsColumns = null;
    private FilledFieldsCount countsRows = null;

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
     * Getter for {@link #countsColumns}.
     * Sets {@link #countsColumns} if it's not initialized yet and returns it.
     *
     * @return {@link #countsColumns}
     */
    public FilledFieldsCount getCountsColumns() {
        if (countsColumns == null) {
            countsColumns = countProvedFieldsPerColumn();
        }
        return countsColumns;
    }

    /**
     * Setter for {@link #countsColumns}.
     *
     * @param pCountsColumn a {@link FilledFieldsCount}
     */
    public void setCountsColumns(FilledFieldsCount pCountsColumn) {
        countsColumns = pCountsColumn;
    }

    /**
     * Getter for {@link #countsRows}.
     * Sets {@link #countsRows} if it's not initialized yet and returns it.
     *
     * @return {@link #countsRows}
     */
    public FilledFieldsCount getCountsRows() {
        if (countsRows == null) {
            countsRows = countProvedFieldsPerRow();
        }
        return countsRows;
    }

    /**
     * Setter for {@link #countsRows}.
     *
     * @param pCountsRow a {@link FilledFieldsCount}
     */
    public void setCountsRows(FilledFieldsCount pCountsRow) {
        countsRows = pCountsRow;
    }

    /**
     * Makes a new game field. Generates a nonogram and counts the fields in rows and columns.
     * Sets {@link #nonogram}, {@link #countsRows} and {@link #countsColumns}.
     * Makes a new nonogram again if all fields of the generated one are empty.
     *
     * @param pNumberOfRows    the number of rows the new game field should have
     * @param pNumberOfColumns the number of columns the new game field should have
     */
    public void makeNewGame(int pNumberOfRows, int pNumberOfColumns) {
        do {
            nonogram = generateNonogram(pNumberOfRows, pNumberOfColumns);
            countsRows = countProvedFieldsPerRow();
            countsColumns = countProvedFieldsPerColumn();
        } while (countsRows.isEmpty());
    }

    /**
     * Generates a new nonogram of the given size.
     * Makes an two dimensional array of the given size. Fills the array with random {@link NonogramConstants#FIELD_EMPTY} and {@link NonogramConstants#FIELD_PROVED}.
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
                int k = random.nextInt(2);
                // gives 0 or 1
                // set FIELD_PROVED for 1 and FIELD_EMTPY for 0
                if (k == 1) {
                    nonogram[i][j] = NonogramConstants.FIELD_PROVED;
                } else {
                    nonogram[i][j] = NonogramConstants.FIELD_EMPTY;
                }
            }
        }

        return nonogram;
    }

    /**
     * Counts the groups in the rows of the {@link #nonogram}.
     * Saves the counts in a {@link FilledFieldsCount} object.
     *
     * @return a {@link FilledFieldsCount} with the row counts of {@link #nonogram}
     */
    private FilledFieldsCount countProvedFieldsPerRow() {
        FilledFieldsCount provedFields = new FilledFieldsCount();

        // iterate over the rows of the nonogram
        for (int rowCount = 0; rowCount < nonogram.length; rowCount++) {
            int groupElements = 0;
            // iterate over each value in the row
            for (int valueInRowCount = 0; valueInRowCount < nonogram[rowCount].length; valueInRowCount++) {
                if (nonogram[rowCount][valueInRowCount] == NonogramConstants.FIELD_PROVED) {
                    // here's a group of proved fields
                    // add 1 for each field to the counter
                    groupElements += 1;
                }
                if (nonogram[rowCount][valueInRowCount] == NonogramConstants.FIELD_EMPTY && groupElements != 0) {
                    // here's the end of one group
                    // add the number and resets the counter
                    provedFields.addCount(rowCount, groupElements);
                    groupElements = 0;
                }
            }
            // end of the for loop for a row

            if (groupElements != 0) {
                // add the last counter if there is one
                provedFields.addCount(rowCount, groupElements);
            }

            if (provedFields.isEmpty(rowCount)) {
                // if the list is empty, there are no proved fields
                // add 0
                provedFields.addCount(rowCount, 0);
            }
        }

        return provedFields;
    }

    /**
     * Counts the groups in the columns of the {@link #nonogram}.
     * Saves the counts in a {@link FilledFieldsCount} object.
     *
     * @return a {@link FilledFieldsCount} with the column counts of {@link #nonogram}
     */
    private FilledFieldsCount countProvedFieldsPerColumn() {
        FilledFieldsCount provedFields = new FilledFieldsCount();

        // iterate over the columns of the nonogram
        for (int columnCount = 0; columnCount < nonogram[0].length; columnCount++) {
            int res = 0;
            // iterate over each value in the column
            for (int valueInColumnCount = 0; valueInColumnCount < nonogram.length; valueInColumnCount++) {
                if (nonogram[valueInColumnCount][columnCount] == NonogramConstants.FIELD_PROVED) {
                    // here's a group of proved fields
                    // add 1 for each field to the counter
                    res += 1;
                }
                if (nonogram[valueInColumnCount][columnCount] == NonogramConstants.FIELD_EMPTY && res != 0) {
                    // here's the end of one group
                    // add the number to the list and resets the counter
                    provedFields.addCount(columnCount, res);
                    res = 0;
                }
            }
            // end of the for loop for this column

            if (res != 0) {
                // add the last counter to the list if there is one
                provedFields.addCount(columnCount, res);
            }

            if (provedFields.isEmpty(columnCount)) {
                // if the list is empty, there are no proved fields
                // add 0 to the list
                provedFields.addCount(columnCount, 0);
            }
        }

        return provedFields;
    }
}
