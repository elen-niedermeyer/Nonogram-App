package niedermeyer.nonogram.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class StatisticsPersistence {

    /**
     * default value for score is 0
     */
    private static final int SCORE_DEFAULT = 0;

    /**
     * context activity
     */
    private final Context context;

    /**
     * Constructor, sets {@link #context}.
     *
     * @param pContext the context
     */
    public StatisticsPersistence(Context pContext) {
        context = pContext;
    }

    /**
     * Gets the count of solved puzzles for the given size.
     *
     * @param pNumberOne the number of rows or columns
     * @param pNumberTwo the other number of rows or columns
     * @return the number of solved puzzles for the given size
     */
    public int getCountOfSSolvedPuzzles(int pNumberOne, int pNumberTwo) {
        String prefName = getPreferenceName(pNumberOne, pNumberTwo);
        return getCountOfSSolvedPuzzles(prefName);
    }

    /**
     * Gets the count of solved puzzles for the current puzzle size. Add one to the count. Saves the new count.
     */
    public void saveNewScore() {
        GameOptionsPersistence gameOptionsPersistence = new GameOptionsPersistence(context);
        String prefName = getPreferenceName(gameOptionsPersistence.getNumberOfRows(), gameOptionsPersistence.getNumberOfColumns());
        // update the count of solved puzzles
        int count = getCountOfSSolvedPuzzles(prefName);
        count = count + 1;
        // save the new score for the current puzzle size
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_group_statistics), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(prefName, count);
        prefsEdit.apply();
    }

    /**
     * Gets the name of the preference where the count for the given size is solved.
     *
     * @param pNumberOne number of rows or columns
     * @param pNumberTwo the other one from the number of rows or columns
     * @return the name of the preference for the given size
     */
    private String getPreferenceName(int pNumberOne, int pNumberTwo) {
        String prefName;
        if (pNumberOne > pNumberTwo) {
            prefName = String.format(context.getString(R.string.prefs_statistic), pNumberTwo, pNumberOne);
        } else {
            prefName = String.format(context.getString(R.string.prefs_statistic), pNumberOne, pNumberTwo);
        }

        return prefName;
    }

    /**
     * Gets the count that is saved in the given preference.
     *
     * @param pPrefName the name of the preference where the count should be given
     * @return the number which is saved in the preference
     */
    private int getCountOfSSolvedPuzzles(String pPrefName) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.prefs_group_statistics), Context.MODE_PRIVATE);
        return prefs.getInt(pPrefName, SCORE_DEFAULT);
    }
}
