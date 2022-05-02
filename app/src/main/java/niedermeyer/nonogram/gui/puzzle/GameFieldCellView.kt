package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.NonogramConstants

class GameFieldCellView(
    context: Context,
    val viewId: Int,
    cellSize: Int,
    value: Int,
    onClick: OnClickListener
) : View(context) {

    init {
        this.layoutParams = LinearLayout.LayoutParams(cellSize, cellSize)
        setBackgroundResource(getFieldBackgroundResource(value))
        setOnClickListener(onClick)
    }

    fun updateBackground(newValue: Int) {
        setBackgroundResource(getFieldBackgroundResource(newValue))
    }

    private fun getFieldBackgroundResource(pValue: Int): Int {
        return when (pValue) {
            NonogramConstants.FIELD_FILLED -> {
                R.drawable.game_cell_black
            }
            NonogramConstants.FIELD_EMPTY -> {
                R.drawable.game_cell_cross
            }
            else -> {
                R.drawable.game_cell_white
            }
        }
    }
}