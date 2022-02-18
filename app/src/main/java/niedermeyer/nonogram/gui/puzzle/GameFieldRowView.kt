package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.widget.TableLayout
import android.view.Gravity
import android.widget.TableRow

class GameFieldRowView(context: Context?) : TableRow(context) {

    init {
        val rowParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        this.layoutParams = rowParams
        this.gravity = Gravity.CENTER
    }

}