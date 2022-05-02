package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import niedermeyer.nonogram.logics.GameFieldCell

class GameFieldView(
    context: Context,
    cells: List<GameFieldCell>,
    columnNumber: Int,
    cellSize: Int,
    onCellClick: OnClickListener
) : LinearLayout(context) {

    init {
        this.orientation = VERTICAL
        this.gravity = Gravity.CENTER

        // new row
        var rowView = GameFieldRowView(context)
        for (i in cells.indices) {
            val newCell = GameFieldCellView(
                context,
                i,
                cellSize,
                cells[i].userValue,
                onCellClick
            )
            rowView.addView(newCell)

            if ((i + 1) % columnNumber == 0) {
                this.addView(rowView)
                rowView = GameFieldRowView(context)
            }
        }
    }

}