package niedermeyer.nonogram.persistence.puzzle

import android.content.Context
import niedermeyer.nonogram.R

class PuzzlePersistence(private val context: Context) {

    val isFirstPuzzle: Boolean
        get() {
            val prefs = context.getSharedPreferences(
                context.getString(R.string.prefs_first_game),
                Context.MODE_PRIVATE
            )
            // read the preference, true is the default value
            val isFirstGame = prefs.getBoolean(context.getString(R.string.prefs_first_game), true)
            if (isFirstGame) {
                // set the preference to false
                val edit = prefs.edit()
                edit.putBoolean(context.getString(R.string.prefs_first_game), false)
                edit.apply()
            }
            return isFirstGame
        }

}