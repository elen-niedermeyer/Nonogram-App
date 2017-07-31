package niedermeyer.nonogram.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-07-31
 */
public class GameSizeHandler {

    /**
     * Static variables representing the size of the game field
     */
    public static int numberOfRows;
    public static int numberOfColumns;

    /**
     * activity, which makes an instance of this class
     */
    private Activity activity;

    /**
     * Constructor, sets {@link #activity}. Initializes {@link #numberOfRows} and {@link #numberOfColumns}.
     *
     * @param pActivity the invoking activity
     */
    public GameSizeHandler(Activity pActivity) {
        activity = pActivity;
        //initializeFieldSizes();
    }

    /**
     * Loads the game field sizes of shared preferences.
     * Sets {@link #numberOfRows} and {@link #numberOfColumns}.
     */
    public void initializeFieldSizes() {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.prefs_group_game_size), Context.MODE_PRIVATE);
        numberOfRows = prefs.getInt(activity.getString(R.string.prefs_rows), 5);
        numberOfColumns = prefs.getInt(activity.getString(R.string.prefs_columns), 5);
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
}
