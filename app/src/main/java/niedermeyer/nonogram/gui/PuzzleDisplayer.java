package niedermeyer.nonogram.gui;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Arrays;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.FilledFieldsCount;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.logics.NonogramGenerator;
import niedermeyer.nonogram.persistence.PuzzleSizePersistence;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-09-30
 */
public class PuzzleDisplayer {

    /**
     * Context activity
     */
    private NonogramActivity activity;

    private CountFilledFieldsDisplayer countsDisplayer;

    /**
     * Logical elements
     */
    private NonogramGenerator generator = new NonogramGenerator();
    private int[][] nonogram;

    /**
     * Statistics
     */
    private StatisticsPersistence statistics;

    /**
     * Field that the users creates
     */
    private int[][] usersCurrentField;

    /**
     * Listener for the buttons on the puzzle.
     */
    private OnClickListener fieldOnClick = new OnClickListener() {
        /**
         * Overrides {@link OnClickListener#onClick(View)}.
         * Changes the background of the clicked field in the puzzle:
         * If the field was {@link NonogramConstants#FIELD_NO_DECISION} it becomes {@link NonogramConstants#FIELD_PROVED}.
         * If it was {@link NonogramConstants#FIELD_PROVED} it becomes {@link NonogramConstants#FIELD_EMPTY}.
         * If it was {@link NonogramConstants#FIELD_EMPTY} it becomes {@link NonogramConstants#FIELD_NO_DECISION}.
         * <p>
         * Looks if the nonogram is solved.
         *
         * @param v the clicked view, a field of the nonogram
         */
        @Override
        public void onClick(View v) {
            // get position of the clicked view
            int rowNumber = parseRowNumberFromFieldView(v);
            int columnNumber = parseColumnNumberFromFieldView(v);

            // changes the clicked field
            int fieldValue = usersCurrentField[rowNumber][columnNumber];
            if (fieldValue == NonogramConstants.FIELD_NO_DECISION) {
                v.setBackgroundResource(R.drawable.game_field_btn_black);
                usersCurrentField[rowNumber][columnNumber] = NonogramConstants.FIELD_PROVED;
            } else if (fieldValue == NonogramConstants.FIELD_PROVED) {
                v.setBackgroundResource(R.drawable.game_field_btn_cross);
                usersCurrentField[rowNumber][columnNumber] = NonogramConstants.FIELD_EMPTY;
            } else if (fieldValue == NonogramConstants.FIELD_EMPTY) {
                v.setBackgroundResource(R.drawable.game_field_btn_white);
                usersCurrentField[rowNumber][columnNumber] = NonogramConstants.FIELD_NO_DECISION;
            }

            // prove if the nonogram is solved now
            if (isPuzzleSolved()) {
                doActionsAfterPuzzleWasSolved();
            }
        }
    };

    /**
     * Constructor.
     * Initializes {@link #activity},{@link #countsDisplayer} and {@link #statistics}.
     *
     * @param pActivity the activity in which the class is used
     */
    public PuzzleDisplayer(NonogramActivity pActivity) {
        activity = pActivity;
        countsDisplayer = new CountFilledFieldsDisplayer(activity, generator);
        statistics = new StatisticsPersistence(activity);
    }

    /**
     * Getter for the nonogram.
     *
     * @return {@link #nonogram}
     */
    public int[][] getNonogram() {
        return nonogram;
    }

    /**
     * Getter for the field that the user creates.
     *
     * @return {@link #usersCurrentField}
     */
    public int[][] getUsersCurrentField() {
        return usersCurrentField;
    }

    /**
     * Getter for {@link CountFilledFieldsDisplayer#columnCounts}.
     *
     * @return {@link CountFilledFieldsDisplayer#columnCounts}
     */
    public FilledFieldsCount getColumnCounts() {
        return countsDisplayer.getColumnCounts();
    }

    /**
     * Getter for {@link CountFilledFieldsDisplayer#rowCounts}.
     *
     * @return {@link CountFilledFieldsDisplayer#rowCounts}
     */
    public FilledFieldsCount getRowCounts() {
        return countsDisplayer.getRowCounts();
    }

    /**
     * Creates a new nonogram puzzle.
     * Generates a new nonogram by {@link #generator}. Sets {@link #nonogram}.
     * Resets {@link #usersCurrentField}.
     * Generates the GUI by {@link #generateNewGameField()}.
     */
    public void displayNewGame() {
        int numberOfRows = PuzzleSizePersistence.numberOfRows;
        int numberOfColumns = PuzzleSizePersistence.numberOfColumns;

        // make new puzzle and initialize the private fields
        generator.makeNewGame(numberOfRows, numberOfColumns);
        nonogram = generator.getNonogram();
        // make array for user-generated field
        makeNewUsersCurrentArray(numberOfRows, numberOfColumns);

        generateNewGameField();
    }

    /**
     * Creates a nonogram field by a given puzzle.
     * Sets {@link #nonogram} and {@link #usersCurrentField} by the given parameters.
     * Generates the GUI by {@link #generateSavedGameField()}.
     * <p>
     * Generates a new game field by {@link #displayNewGame()}, if the parameters are null.
     *
     * @param pNonogram          an array that should represent {@link #nonogram}
     * @param pUsersCurrentField an array that should represent {@link #usersCurrentField}
     */
    public void displayNewGame(int[][] pNonogram, int[][] pUsersCurrentField, FilledFieldsCount pCountsColumns, FilledFieldsCount pCountsRows) {
        if (pNonogram != null) {
            // initialize nonogram object
            nonogram = pNonogram;
            generator.setNonogram(pNonogram);
            generator.setCountsColumns(pCountsColumns);
            generator.setCountsRows(pCountsRows);

            // initialize user-generated field array
            if (pUsersCurrentField != null) {
                usersCurrentField = pUsersCurrentField;
            } else {
                makeNewUsersCurrentArray(nonogram.length, nonogram[0].length);
            }

            generateSavedGameField();

        } else {
            // make new game if the field couldn't be set
            displayNewGame();
        }
    }

    /**
     * Resets the game.
     * Just makes a new GUI for the current nonogram by {@link #generateNewGameField()}.
     */
    public void resetGame() {
        generateNewGameField();
    }

    /**
     * Gets the tag of the given view. The view is a field of the puzzle
     * The tag's structure is "[row number]_[column number]".
     * Returns the part of the row number.
     *
     * @param pView view which's row number you want to know
     * @return the row number of the field
     */
    private int parseRowNumberFromFieldView(View pView) {
        String tag = (String) pView.getTag();
        String[] tagSplit = tag.split(activity.getString(R.string.string_devider));
        return Integer.parseInt(tagSplit[0]);
    }

    /**
     * Gets the tag of the given view. The view is a field of the puzzle
     * The tag's structure is "[row number]_[column number]".
     * Returns the part of the column number.
     *
     * @param pView view which's column number you want to know
     * @return the column number of the field
     */
    private int parseColumnNumberFromFieldView(View pView) {
        String tag = (String) pView.getTag();
        String[] tagSplit = tag.split(activity.getString(R.string.string_devider));
        return Integer.parseInt(tagSplit[1]);
    }

    /**
     * Proofs if the puzzle is solved by comparing the {@link #nonogram} with the {@link #usersCurrentField}.
     *
     * @return a boolean, true if the puzzle is solved.
     */
    private boolean isPuzzleSolved() {
        // make a copy of the field generated by the user
        // therefore fields without a decision must be seen as empty
        int[][] usersFieldCopy = new int[usersCurrentField.length][usersCurrentField[0].length];
        for (int i = 0; i < usersCurrentField.length; i++) {
            for (int j = 0; j < usersCurrentField[i].length; j++) {
                if (usersCurrentField[i][j] == NonogramConstants.FIELD_NO_DECISION) {
                    usersFieldCopy[i][j] = NonogramConstants.FIELD_EMPTY;
                } else {
                    usersFieldCopy[i][j] = usersCurrentField[i][j];
                }
            }
        }

        return (Arrays.deepEquals(usersFieldCopy, nonogram));
    }

    /**
     * If the puzzle was solved, this method saves it in the statistics.
     * Also shows the won animation. The animation will start a new puzzle then.
     */
    private void doActionsAfterPuzzleWasSolved() {
        // save won puzzle in statistics
        statistics.saveNewScore();

        // show the animation
        new Animations().showWonAnimation(activity);
    }

    /**
     * Sets the {@link #usersCurrentField}.
     * Makes an empty array and fills it with {@link NonogramConstants#FIELD_NO_DECISION}
     *
     * @param pNumberOfRows    number of rows of the current puzzle
     * @param pNumberOfColumns number of columns of the current puzzle
     */
    private void makeNewUsersCurrentArray(int pNumberOfRows, int pNumberOfColumns) {
        // make a new array for the current user-generated field
        usersCurrentField = new int[pNumberOfRows][pNumberOfColumns];
        for (int[] innerArray : usersCurrentField) {
            Arrays.fill(innerArray, NonogramConstants.FIELD_NO_DECISION);
        }
    }

    /**
     * Makes the puzzle's surface.
     * Makes a table row for each row.
     * Adds the counts for the rows and for the columns with {@link CountFilledFieldsDisplayer}
     */
    private void generateNewGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.activity_nonogram_field);
        // clear the field, remove all rows from table
        table.removeAllViews();

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        // add rows of nonogram
        for (int rowCounter = 0; rowCounter < nonogram.length; rowCounter++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setLayoutParams(rowParams);
            row.setGravity(Gravity.CENTER);

            // add a button for each field in this row
            for (int columnCounter = 0; columnCounter < nonogram[rowCounter].length; columnCounter++) {
                Button newButton = makeFieldButton();
                // set tag
                newButton.setTag(rowCounter + activity.getString(R.string.string_devider) + columnCounter);

                // set background
                newButton.setBackgroundResource(R.drawable.game_field_btn_white);

                // add button to row
                row.addView(newButton);
            }
            // add row to table
            table.addView(row);
        }

        // add rows with counts of the columns
        table = countsDisplayer.addCounts(table);
    }

    /**
     * Makes the puzzle's surface.
     * Makes a table row for each row. The value of the fields is given by {@link #usersCurrentField}.
     * Adds the counts for the rows and for the columns with {@link CountFilledFieldsDisplayer}
     */
    private void generateSavedGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.activity_nonogram_field);
        // clear the field, remove all rows from table
        table.removeAllViews();

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        // add rows of nonogram
        for (int rowCounter = 0; rowCounter < nonogram.length; rowCounter++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setLayoutParams(rowParams);
            row.setGravity(Gravity.CENTER);

            // add a button for each field in this row
            for (int columnCounter = 0; columnCounter < nonogram[rowCounter].length; columnCounter++) {
                Button newButton = makeFieldButton();
                // set tag
                newButton.setTag(rowCounter + activity.getString(R.string.string_devider) + columnCounter);

                // set background resource for the given field in usersCurrentField
                if (usersCurrentField[rowCounter][columnCounter] == NonogramConstants.FIELD_NO_DECISION) {
                    newButton.setBackgroundResource(R.drawable.game_field_btn_white);
                } else if (usersCurrentField[rowCounter][columnCounter] == NonogramConstants.FIELD_PROVED) {
                    newButton.setBackgroundResource(R.drawable.game_field_btn_black);
                } else if (usersCurrentField[rowCounter][columnCounter] == NonogramConstants.FIELD_EMPTY) {
                    newButton.setBackgroundResource(R.drawable.game_field_btn_cross);
                }

                // add button to row
                row.addView(newButton);
            }
            // add row to table
            table.addView(row);
        }

        // add rows with counts of the columns
        table = countsDisplayer.addCounts(table);
    }

    /**
     * Makes a button for the field.
     * Sets the size and the on click listener {@link #fieldOnClick}.
     *
     * @return a new button
     */
    private Button makeFieldButton() {
        Button b = new Button(activity);
        // set size
        int buttonSize = (int) activity.getResources().getDimension(R.dimen.field_button_size);
        b.setLayoutParams(new TableRow.LayoutParams(buttonSize, buttonSize));
        // set the onClickListener implemented in this class
        b.setOnClickListener(fieldOnClick);
        return b;
    }
}
