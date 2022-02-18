package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.View
import android.widget.TableLayout

class GameFieldDisplayer(private val context: Context) {

    fun getPuzzleView(
        cells: List<niedermeyer.nonogram.logics.GameFieldCell?>,
        rowNumber: Int,
        cellSize: Int,
        onCellClick: View.OnClickListener
    ): TableLayout {
        val table = TableLayout(context)

        // new row
        var rowView = GameFieldRow(context)
        for (i in cells.indices) {
            val newCell = cells[i]?.let {
                GameFieldCell(
                    context,
                    i,
                    cellSize,
                    it.userValue,
                    onCellClick
                )
            }
            rowView.addView(newCell)

            if ((i + 1) % rowNumber == 0) {
                table.addView(rowView)
                rowView = GameFieldRow(context)
            }
        }

        return table
    }

}