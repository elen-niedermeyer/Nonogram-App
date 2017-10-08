package niedermeyer.nonogram.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import niedermeyer.nonogram.R;

/**
 * @author Elen Niedermeyer, last modified 2017-08-27
 */
public class StatisticsPersistence {

    private static final int SCORE_DEFAULT = 0;

    /**
     * activity, which makes an instance of this class
     */
    private Activity activity;

    /**
     * Constructor, sets {@link #activity}.
     *
     * @param pActivity the invoking activity
     */
    public StatisticsPersistence(Activity pActivity) {
        activity = pActivity;
    }

    public int getNumberOfSavedPuzzles(int pNumberOne, int pNumberTwo) {
        String prefName = getPreferenceName(pNumberOne, pNumberTwo);
        return getNumberOfSavedPuzzles(prefName);
    }

    public void saveNewScore() {
        String prefName = getPreferenceName(PuzzleSizePersistence.numberOfRows, PuzzleSizePersistence.numberOfColumns);
        int score = getNumberOfSavedPuzzles(prefName);
        score = score + 1;
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.prefs_group_statistics), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putInt(prefName, score);
        prefsEdit.apply();
    }

    private String getPreferenceName(int pNumberOne, int pNumberTwo) {
        String prefName;
        if (pNumberOne > pNumberTwo) {
            prefName = String.format(activity.getString(R.string.prefs_statistic), pNumberTwo, pNumberOne);
        } else {
            prefName = String.format(activity.getString(R.string.prefs_statistic), pNumberOne, pNumberTwo);
        }
        return prefName;
    }

    private int getNumberOfSavedPuzzles(String pPrefName) {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getString(R.string.prefs_group_statistics), Context.MODE_PRIVATE);
        int score = prefs.getInt(pPrefName, SCORE_DEFAULT);
        return score;
    }
}
