package niedermeyer.nonogram.activities;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;
import niedermeyer.nonogram.logics.NonogramGenerator;
import niedermeyer.nonogram.persistence.GameSizePersistence;
import niedermeyer.nonogram.persistence.StatisticsPersistence;

/**
 * @author Elen Niedermeyer, last updated 2017-08-27
 */

public class GameHandler {

    /**
     * Context activity
     */
    private NonogramActivity activity;

    /**
     * Logical elements
     */
    private NonogramGenerator generator = new NonogramGenerator();
    private int[][] nonogram;
    private Map<Integer, ArrayList<Integer>> rowCounts;
    private Map<Integer, ArrayList<Integer>> columnCounts;

    /**
     * Statistics
     */
    private StatisticsPersistence statistics;

    /**
     * Field that the users creates
     */
    private int[][] actualField;

    /**
     * Listener for the buttons on the game field.
     * Overrides {@link OnClickListener#onClick(View)}.
     * Parses the clicked field's id.
     * Changes the background:
     * If the field was {@link NonogramConstants#FIELD_NOTHING} it becomes {@link NonogramConstants#FIELD_PROVED}.
     * If it was {@link NonogramConstants#FIELD_PROVED} it becomes {@link NonogramConstants#FIELD_EMPTY}.
     * If it was {@link NonogramConstants#FIELD_EMPTY} it becomes {@link NonogramConstants#FIELD_NOTHING}.
     * <p>
     * Looks if the nonogram is solved by comparing the updated {@link #actualField} with {@link #nonogram}.
     */
    private OnClickListener fieldOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // get position of the clicked view
            // get the views parent to get the row number
            TableRow row = (TableRow) v.getParent();
            int r = row.getId();
            // get the view id, it's [row number][column number]
            int id = v.getId();
            String idString = Integer.toString(id);
            // get the column number
            int c;
            if (r == 0) {
                // if the row is 0, the column number is just the view id
                c = id;
            } else if (r > 0 && r < 10) {
                // if the row number has one digit, the column number is the view id without the first digit
                String jString = idString.substring(1);
                c = Integer.parseInt(jString);
            } else {
                // if the row number has two digits, the column number is the view id without the two first digits
                String jString = idString.substring(2);
                c = Integer.parseInt(jString);
            }

            // changes the clicked field
            int fieldValue = actualField[r][c];
            if (fieldValue == NonogramConstants.FIELD_NOTHING) {
                v.setBackgroundResource(R.drawable.game_field_btn_black);
                actualField[r][c] = NonogramConstants.FIELD_PROVED;
            } else if (fieldValue == NonogramConstants.FIELD_PROVED) {
                v.setBackgroundResource(R.drawable.game_field_btn_cross);
                actualField[r][c] = NonogramConstants.FIELD_EMPTY;
            } else if (fieldValue == NonogramConstants.FIELD_EMPTY) {
                v.setBackgroundResource(R.drawable.game_field_btn_white);
                actualField[r][c] = NonogramConstants.FIELD_NOTHING;
            }

            // prove if the nonogram is solved now
            // make a copy, replace all -1 with 0 (these are empty fields but in the GUI they have different meanings)
            int[][] actualFieldCopy = new int[actualField.length][actualField[0].length];
            for (int i = 0; i < actualField.length; i++) {
                for (int j = 0; j < actualField[i].length; j++) {
                    if (actualField[i][j] == NonogramConstants.FIELD_NOTHING) {
                        actualFieldCopy[i][j] = NonogramConstants.FIELD_EMPTY;
                    } else {
                        actualFieldCopy[i][j] = actualField[i][j];
                    }
                }
            }

            // if the copy of the array is equals the nonogram, the game is solved
            if (Arrays.deepEquals(actualFieldCopy, nonogram)) {
                // game is won
                // save it in statistics
                statistics.saveNewScore();
                // make new game
                generator.makeNewGame(GameSizePersistence.numberOfRows, GameSizePersistence.numberOfColumns);
                nonogram = generator.getNonogram();
                // clear actual field variable
                actualField = new int[GameSizePersistence.numberOfRows][GameSizePersistence.numberOfColumns];
                // show the animation
                showWonAnimation();
            }
        }
    };

    private OnClickListener countOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setBackgroundResource(R.drawable.game_count_striked_off);
        }
    };

    /**
     * Constructor.
     * Initializes {@link #activity}
     *
     * @param pActivity the activity in which the game handler is used
     */
    public GameHandler(NonogramActivity pActivity) {
        activity = pActivity;
        statistics = new StatisticsPersistence(activity);
    }

    /**
     * Getter for the nonogram
     *
     * @return {@link #nonogram}
     */
    public int[][] getNonogram() {
        return nonogram;
    }

    /**
     * Getter for the field that the user creates
     *
     * @return {@link #actualField}
     */
    public int[][] getActualField() {
        return actualField;
    }

    /**
     * Creates a new nonogram game field.
     * Generates a new nonogram by {@link #generator}. Sets {@link #nonogram}, {@link #rowCounts} and {@link #columnCounts}.
     * Resets {@link #actualField}.
     * Generates the GUI by {@link #generateNewGameField()}.
     */
    public void newGame() {
        int numberOfRows = GameSizePersistence.numberOfRows;
        int numberOfColumns = GameSizePersistence.numberOfColumns;

        // ToDo look if this row is necessary
        activity.updateToolbarTitle();

        // make new game field and initialize the private fields
        generator.makeNewGame(numberOfRows, numberOfColumns);
        nonogram = generator.getNonogram();
        rowCounts = generator.getCountsRow();
        columnCounts = generator.getCountsColumn();

        // make a new array for the actual game field
        actualField = new int[numberOfRows][numberOfColumns];

        generateNewGameField();
    }

    /**
     * Creates a nonogram field by a given nonogram and field.
     * Sets {@link #nonogram} and {@link #actualField} by the given parameters.
     * Sets {@link #rowCounts} and {@link #columnCounts} with {@link #generator}.
     * Generates the GUI by {@link #generateSavedGameField()}.
     * <p>
     * Generates a new game field by {@link #newGame()}, if the parameters are null.
     *
     * @param pNonogram    an array that should represent {@link #nonogram}
     * @param pActualField an array that should represent {@link #actualField}
     */
    public void newGame(int[][] pNonogram, int[][] pActualField) {
        if (pNonogram != null) {
            // initialize arrays
            nonogram = pNonogram;
            if (pActualField != null) {
                actualField = pActualField;
            } else {
                actualField = new int[nonogram.length][nonogram[0].length];
            }
            // initialize field counts arrays
            generator.setNonogram(pNonogram);
            rowCounts = generator.getCountsRow();
            columnCounts = generator.getCountsColumn();

            generateSavedGameField();

        } else {
            // make new game if the field couldn't be loaded
            newGame();
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
     * Makes the game field.
     * Makes a table row for each row. IDs are the place in the nonogram array.
     * Adds the counts left for the rows and on the top for the columns.
     */
    private void generateNewGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.activity_nonogram_field);
        // clear the field, remove all rows from table
        table.removeAllViews();

        // add row with counts of the columns
        table = addColumnCountRows(table);

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // add rows of nonogram
        for (int i = 0; i < nonogram.length; i++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setId(i);
            row.setLayoutParams(rowParams);
            row.setGravity(Gravity.CENTER);

            // add count for this row
            row.addView(makeRowCountView(i));
            // add a button for each field in this row
            for (int j = 0; j < nonogram[i].length; j++) {
                Button b = new Button(activity);
                // make and set id
                String id = i + "" + j;
                b.setId(Integer.parseInt(id));
                // set background
                b.setBackgroundResource(R.drawable.game_field_btn_white);
                // set size
                int buttonSize = (int) activity.getResources().getDimension(R.dimen.field_button_size);
                b.setLayoutParams(new TableRow.LayoutParams(buttonSize, buttonSize));
                // set the onClickListener implemented in this class
                b.setOnClickListener(fieldOnClick);
                // add button to row
                row.addView(b);
                // initialize field in array
                actualField[i][j] = NonogramConstants.FIELD_NOTHING;
            }
            // add row to table
            table.addView(row);
        }
    }

    /**
     * Makes the game field.
     * Makes a table row for each row. IDs are the place in the nonogram array. The value and so the style of the fields is given by {@link #actualField}.
     * Adds the counts left for the rows and on the top for the columns.
     */
    private void generateSavedGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.activity_nonogram_field);
        // clear the field, remove all rows from table
        table.removeAllViews();

        // add row with counts of the columns
        table = addColumnCountRows(table);

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // add rows of nonogram
        for (int i = 0; i < nonogram.length; i++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setId(i);
            row.setLayoutParams(rowParams);
            row.setGravity(Gravity.CENTER);

            // add count for this row
            row.addView(makeRowCountView(i));

            // add a button for each field in this row
            for (int j = 0; j < nonogram[i].length; j++) {
                Button b = new Button(activity);
                // make and set id
                String id = i + "" + j;
                b.setId(Integer.parseInt(id));
                // set size
                int buttonSize = (int) activity.getResources().getDimension(R.dimen.field_button_size);
                b.setLayoutParams(new TableRow.LayoutParams(buttonSize, buttonSize));
                // set the onClickListener implemented in this class
                b.setOnClickListener(fieldOnClick);
                // add button to row
                row.addView(b);

                // set background resource for the given field in actualField
                if (actualField[i][j] == NonogramConstants.FIELD_NOTHING) {
                    b.setBackgroundResource(R.drawable.game_field_btn_white);
                } else if (actualField[i][j] == NonogramConstants.FIELD_PROVED) {
                    b.setBackgroundResource(R.drawable.game_field_btn_black);
                } else if (actualField[i][j] == NonogramConstants.FIELD_EMPTY) {
                    b.setBackgroundResource(R.drawable.game_field_btn_cross);
                }
            }
            // add row to table
            table.addView(row);
        }
    }

    private TableLayout addColumnCountRows(TableLayout pTable) {
        // get maximum of numbers
        int neededRows = 0;
        for (int i = 0; i < columnCounts.size(); i++) {
            ArrayList<Integer> values = columnCounts.get(i);
            int x = values.size();
            if (x > neededRows) {
                neededRows = x;
            }
        }

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;

        // add filled views for all columns
        for (int x = 0; x < neededRows; x++) {
            TableRow row = new TableRow(activity);
            // add an empty text view
            row.addView(new TextView(activity));
            for (int i = 0; i < columnCounts.size(); i++) {
                ArrayList<Integer> values = columnCounts.get(i);
                if (values.size() >= x + 1) {
                    TextView count = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_top, null);
                    count.setText(Integer.toString(values.get(x)));
                    count.setClickable(true);
                    count.setOnClickListener(countOnClick);
                    row.addView(count);
                } else {
                    row.addView(new TextView(activity));
                }
            }
            row.setLayoutParams(rowParams);
            pTable.addView(row);
        }

        return pTable;
    }

    private LinearLayout makeRowCountView(int i) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ArrayList<Integer> values = rowCounts.get(i);
        for (int value : values) {
            TextView count = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_left, null);
            count.setText(String.format(Locale.getDefault(), "%d", value));
            count.setClickable(true);
            count.setOnClickListener(countOnClick);
            layout.addView(count);
        }

        return layout;
    }

    private void showWonAnimation() {
        final ScrollView gameLayout = (ScrollView) activity.findViewById(R.id.activity_nonogram_scroll_vertical);
        final LinearLayout animationLayout = (LinearLayout) activity.findViewById(R.id.activity_nonogram_animation_won);
        animationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gameLayout.setVisibility(View.VISIBLE);
                animationLayout.setVisibility(View.GONE);
                activity.getGameHandler().newGame();
            }
        });

        crossfade(animationLayout);
    }

    private void crossfade(View pGoIn) {
        int longAnimationDuration = activity.getResources().getInteger(android.R.integer.config_longAnimTime);

        pGoIn.setVisibility(View.VISIBLE);
        pGoIn.animate()
                .alpha(1f)
                .setDuration(longAnimationDuration)
                .setListener(null);
    }
}
