package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.GroupCounts
import niedermeyer.nonogram.logics.Nonogram

class PuzzleDisplayer(private val context: Context) {

    private var rowCount: GroupCounts? = null
    private var columnCount: GroupCounts? = null

    // listener for count view
    private val onCountClick = View.OnClickListener { v ->
        // parse the tag
        val tag = v.contentDescription as String
        val tagParsed = tag.split(context.getString(R.string.string_divider)).toTypedArray()
        val rowOrColumn = tagParsed[0]
        val outerIndex = tagParsed[1].toInt()
        val innerIndex = tagParsed[2].toInt()
        val views = ArrayList<View>()
        (context as AppCompatActivity).findViewById<View>(R.id.activity_game_root)
            .findViewsWithText(views, tag, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)

        // toggle the counts background
        if (rowOrColumn == context.getString(R.string.row)) {
            val cell = rowCount!!.get(outerIndex, innerIndex)
            cell.toggleCrossedOut()
            for (view in views) {
                (view as GroupCountCellView).setCustomTextColor(cell.isCrossedOut)
            }
        } else if (rowOrColumn == context.getString(R.string.column)) {
            val cell = columnCount!!.get(outerIndex, innerIndex)
            cell.toggleCrossedOut()
            for (view in views) {
                (view as GroupCountCellView).setCustomTextColor(cell.isCrossedOut)
            }
        }
    }

    fun getGameView(
        nonogram: Nonogram,
        cellSize: Int,
        onFieldClick: View.OnClickListener
    ): LinearLayout {
        rowCount = nonogram.rowCounts
        columnCount = nonogram.columnCounts
        val fieldDisplayer = GameFieldDisplayer(context)
        val gameFieldView = fieldDisplayer.getPuzzleView(
            nonogram.cells,
            nonogram.rowNumber,
            cellSize,
            onFieldClick
        )
        val groupCountDisplayer = GroupCountDisplayer(context)
        // add row counts
        for (i in 0 until nonogram.rowNumber) {
            val row = gameFieldView.getChildAt(i) as LinearLayout
            // add row count at the left
            var rowCountView =
                groupCountDisplayer.getRowCountView(nonogram.rowCounts!!, i, onCountClick)
            rowCountView.gravity = Gravity.END
            row.addView(rowCountView, 0)
            // add row count at the right
            rowCountView =
                groupCountDisplayer.getRowCountView(nonogram.rowCounts!!, i, onCountClick)
            rowCountView.gravity = Gravity.START
            row.addView(rowCountView)
        }

        // add column count at the top
        var columnCount =
            groupCountDisplayer.getColumnCountRow(nonogram.columnCounts!!, onCountClick)
        columnCount.gravity = Gravity.BOTTOM
        gameFieldView.addView(columnCount, 0)
        // add column count at the bottom
        columnCount = groupCountDisplayer.getColumnCountRow(nonogram.columnCounts!!, onCountClick)
        columnCount.gravity = Gravity.TOP
        gameFieldView.addView(columnCount)

        return gameFieldView
    }

}