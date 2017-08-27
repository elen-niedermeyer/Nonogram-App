package niedermeyer.nonogram.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-07-31
 */
public class GameSizePersistence {

    /**
     * Static variables representing the size of the game field
     */
    public static int numberOfRows;
    public static int numberOfColumns;

    /**
     * Number of rows and columns if the preferences couldn't be loaded
     */
    private static final int SIZE_DEFAULT = 5;

    /**
     * activity, which makes an instance of this class
     */
    private Activity activity;

    /**
     * Constructor, sets {@link #activity}. Initializes {@link #numberOfRows} and {@link #numberOfColumns} if they aren't initialized yet.
     *
     * @param pActivity the invoking activity
     */
    public GameSizePersistence(Activity pActivity) {
        activity = pActivity;
        if (numberOfRows == 0 && numberOfColumns == 0) {
            // fields are not initialized
            initializeFieldSizes();
        }
    }

    /**
     * Saves {@link #numberOfRows} and {@link #numberOfColumns} in shared preferences.
     */
    public void saveGameSize() {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.prefs_group_game_size), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(activity.getString(R.string.prefs_rows), numberOfRows);
        prefsEdit.putInt(activity.getString(R.string.prefs_columns), numberOfColumns);
        prefsEdit.apply();
    }

    /**
     * Loads the game field sizes of shared preferences.
     * Sets {@link #numberOfRows} and {@link #numberOfColumns}.
     */
    private void initializeFieldSizes() {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.prefs_group_game_size), Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(activity.getString(R.string.prefs_rows), SIZE_DEFAULT);
        numberOfColumns = prefs.getInt(activity.getString(R.string.prefs_columns), SIZE_DEFAULT);
    }
}
