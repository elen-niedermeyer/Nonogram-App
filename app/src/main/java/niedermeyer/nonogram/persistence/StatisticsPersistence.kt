package niedermeyer.nonogram.persistence

import android.content.Context
import niedermeyer.nonogram.R

class StatisticsPersistence(private val context: Context) {

    fun getCountOfSSolvedPuzzles(pNumberOne: Int, pNumberTwo: Int): Int {
        val prefName = getPreferenceName(pNumberOne, pNumberTwo)
        return getCountOfSSolvedPuzzles(prefName)
    }

    fun saveNewScore() {
        val gameOptionsPersistence = GameOptionsPersistence(context)
        val prefName = getPreferenceName(
            gameOptionsPersistence.numberOfRows,
            gameOptionsPersistence.numberOfColumns
        )
        // update the count of solved puzzles
        var count = getCountOfSSolvedPuzzles(prefName)
        count++
        // save the new score for the current puzzle size
        val prefs = context.getSharedPreferences(
            context.getString(R.string.prefs_group_statistics),
            Context.MODE_PRIVATE
        )
        val prefsEdit = prefs.edit()
        prefsEdit.putInt(prefName, count)
        prefsEdit.apply()
    }

    private fun getPreferenceName(pNumberOne: Int, pNumberTwo: Int): String {
        return if (pNumberOne > pNumberTwo) {
            String.format(context.getString(R.string.prefs_statistic), pNumberTwo, pNumberOne)
        } else {
            String.format(context.getString(R.string.prefs_statistic), pNumberOne, pNumberTwo)
        }
    }

    private fun getCountOfSSolvedPuzzles(pPrefName: String): Int {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.prefs_group_statistics),
            Context.MODE_PRIVATE
        )
        return prefs.getInt(pPrefName, SCORE_DEFAULT)
    }

    companion object {
        private const val SCORE_DEFAULT = 0
    }

}