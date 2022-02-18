package niedermeyer.nonogram.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-10-08
 */
public class GameOptionsPersistence {

    /**
     * Number of rows and columns if the preferences couldn't be loaded
     */
    private static final int FIELD_SIZE_DEFAULT = 5;
    private static final int CELL_SIZE_DEFAULT = 100;

    /**
     * Static variables
     */
    private static int numberOfRows = 0;
    private static int numberOfColumns = 0;
    private static int cellSize = 0;

    private final Context context;

    /**
     * Constructor, sets {@link #context}. Initializes {@link #numberOfRows} and {@link #numberOfColumns} if they aren't initialized yet.
     *
     * @param pContext the context activity
     */
    public GameOptionsPersistence(Context pContext) {
        context = pContext;

        init();
    }

    public int getNumberOfRows() {
        if (numberOfRows > 0) {
            return numberOfRows;
        } else {
            return FIELD_SIZE_DEFAULT;
        }
    }

    public void setNumberOfRows(int numberOfRows) {
        GameOptionsPersistence.numberOfRows = numberOfRows;
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_game_options), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(context.getString(R.string.prefs_rows), numberOfRows);
        prefsEdit.apply();
    }

    public int getNumberOfColumns() {
        if (numberOfColumns > 0) {
            return numberOfColumns;
        } else {
            return FIELD_SIZE_DEFAULT;
        }
    }

    public void setNumberOfColumns(int numberOfColumns) {
        GameOptionsPersistence.numberOfColumns = numberOfColumns;
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_game_options), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(context.getString(R.string.prefs_columns), numberOfColumns);
        prefsEdit.apply();
    }

    public int getCellSize() {
        if (cellSize > 0) {
            return cellSize;
        } else {
            return CELL_SIZE_DEFAULT;
        }
    }

    public void setCellSize(int cellSize) {
        GameOptionsPersistence.cellSize = cellSize;
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_game_options), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(context.getString(R.string.prefs_cell_size), cellSize);
        prefsEdit.apply();
    }

    /**
     * Loads shared preferences.
     */
    private void init() {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_game_options), Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(context.getString(R.string.prefs_rows), FIELD_SIZE_DEFAULT);
        numberOfColumns = prefs.getInt(context.getString(R.string.prefs_columns), FIELD_SIZE_DEFAULT);
        cellSize = prefs.getInt(context.getString(R.string.prefs_cell_size), CELL_SIZE_DEFAULT);
    }
}
