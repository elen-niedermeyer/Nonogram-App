package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout

class GameFieldRowView(context: Context?) : LinearLayout(context) {

    init {
        val rowParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        this.layoutParams = rowParams
        this.gravity = Gravity.CENTER
        this.orientation = LinearLayout.HORIZONTAL
    }

}