package niedermeyer.nonogram.persistence

import android.content.Context
import niedermeyer.nonogram.R

class GameOptionsPersistence(private val context: Context) {

    var numberOfRows: Int
        get() = if (Companion.numberOfRows > 0) {
            Companion.numberOfRows
        } else {
            FIELD_SIZE_DEFAULT
        }
        set(numberOfRows) {
            Companion.numberOfRows = numberOfRows
            val prefs = context.getSharedPreferences(
                context.getString(R.string.prefs_game_options),
                Context.MODE_PRIVATE
            )
            val prefsEdit = prefs.edit()
            prefsEdit.putInt(context.getString(R.string.prefs_rows), numberOfRows)
            prefsEdit.apply()
        }

    var numberOfColumns: Int
        get() = if (Companion.numberOfColumns > 0) {
            Companion.numberOfColumns
        } else {
            FIELD_SIZE_DEFAULT
        }
        set(numberOfColumns) {
            Companion.numberOfColumns = numberOfColumns
            val prefs = context.getSharedPreferences(
                context.getString(R.string.prefs_game_options),
                Context.MODE_PRIVATE
            )
            val prefsEdit = prefs.edit()
            prefsEdit.putInt(context.getString(R.string.prefs_columns), numberOfColumns)
            prefsEdit.apply()
        }

    init {
        val prefs = context.getSharedPreferences(
            context.getString(R.string.prefs_game_options),
            Context.MODE_PRIVATE
        )
        Companion.numberOfRows =
            prefs.getInt(context.getString(R.string.prefs_rows), FIELD_SIZE_DEFAULT)
        Companion.numberOfColumns =
            prefs.getInt(context.getString(R.string.prefs_columns), FIELD_SIZE_DEFAULT)
    }

    companion object {
        // Number of rows and columns if the preferences couldn't be loaded
        private const val FIELD_SIZE_DEFAULT = 5

        // Static variables
        private var numberOfRows = 0
        private var numberOfColumns = 0
    }


}