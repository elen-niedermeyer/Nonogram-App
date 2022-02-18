package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.View
import android.widget.TableLayout
import niedermeyer.nonogram.logics.GameFieldCell

class GameFieldDisplayer(private val context: Context) {

    fun getPuzzleView(
        cells: List<GameFieldCell>,
        rowNumber: Int,
        cellSize: Int,
        onCellClick: View.OnClickListener
    ): TableLayout {
        val table = TableLayout(context)

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

            if ((i + 1) % rowNumber == 0) {
                table.addView(rowView)
                rowView = GameFieldRowView(context)
            }
        }

        return table
    }

}