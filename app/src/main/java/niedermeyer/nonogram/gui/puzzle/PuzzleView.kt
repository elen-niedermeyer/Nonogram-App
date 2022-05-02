package niedermeyer.nonogram.gui.puzzle

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowInsets
import android.widget.LinearLayout
import niedermeyer.nonogram.logics.Nonogram
import kotlin.math.min


class PuzzleView(
    context: Activity,
    offsetTop: Int,
    nonogram: Nonogram,
    onFieldClick: OnClickListener
) : LinearLayout(context) {

    init {
        // init cellSize
        val cellSize = calculateCellSize(context, nonogram, offsetTop)

        val gameFieldView = GameFieldView(
            context,
            nonogram.cells,
            nonogram.columnNumber,
            cellSize,
            onFieldClick
        )
        val groupCountDisplayer = GroupCountDisplayer(context, nonogram)
        // add row counts
        for (i in 0 until nonogram.rowNumber) {
            val row = gameFieldView.getChildAt(i) as GameFieldRowView
            // add row count at the left
            var rowCountView =
                groupCountDisplayer.getRowCountView(i, cellSize)
            rowCountView.gravity = Gravity.END
            row.addView(rowCountView, 0)
            // add row count at the right
            rowCountView =
                groupCountDisplayer.getRowCountView(i, cellSize)
            rowCountView.gravity = Gravity.START
            row.addView(rowCountView)
        }

        // add column count at the top
        var columnCount =
            groupCountDisplayer.getColumnCountRow(cellSize)
        columnCount.gravity = Gravity.BOTTOM
        gameFieldView.addView(columnCount, 0)
        // add column count at the bottom
        columnCount = groupCountDisplayer.getColumnCountRow(cellSize)
        columnCount.gravity = Gravity.TOP
        gameFieldView.addView(columnCount)

        this.addView(gameFieldView)
    }

    private fun calculateCellSize(
        context: Activity,
        nonogram: Nonogram,
        offsetTop: Int
    ): Int {
        val displayWidth: Int
        val displayHeight: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = context.windowManager.currentWindowMetrics
            displayWidth = metrics.bounds.width()
            val insets =
                metrics.windowInsets.getInsets(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout() or WindowInsets.Type.systemBars())
            displayHeight = metrics.bounds.height() - insets.top - insets.bottom - offsetTop
        } else {
            val metrics = DisplayMetrics()
            context.windowManager.defaultDisplay.getMetrics(metrics)
            displayWidth = metrics.widthPixels
            displayHeight = metrics.heightPixels
        }
        // width = column number + row counts
        val maxCellsInRow = nonogram.columnNumber + 2 * nonogram.rowCounts!!.maxLength
        val maxWidth = displayWidth / maxCellsInRow
        //height = row number + column counts
        val maxCellsInColumn = nonogram.rowNumber + 2 * nonogram.columnCounts!!.maxLength
        val maxHeight = displayHeight / maxCellsInColumn

        return min(maxWidth, maxHeight)
    }

}