package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.Nonogram

class GroupCountDisplayer(private val context: Context, private val nonogram: Nonogram) {

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
            val cell = nonogram.rowCounts!!.get(outerIndex, innerIndex)
            cell.toggleCrossedOut()
            for (view in views) {
                (view as GroupCountCellView).setCustomTextColor(cell.isCrossedOut)
            }
        } else if (rowOrColumn == context.getString(R.string.column)) {
            val cell = nonogram.columnCounts!!.get(outerIndex, innerIndex)
            cell.toggleCrossedOut()
            for (view in views) {
                (view as GroupCountCellView).setCustomTextColor(cell.isCrossedOut)
            }
        }
    }

    fun getRowCountView(
        index: Int,
        cellSize: Int,
    ): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        val counts = nonogram.rowCounts!!.get(index)

        // add an view for each count of the row
        for (i in counts.indices) {
            // make a new view
            val countView = GroupCountCellView(
                context,
                counts[i],
                cellSize,
                String.format(context.getString(R.string.tag_row_count), index, i),
                onCountClick,
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

    fun getColumnCountRow(
        cellSize: Int
    ): LinearLayout {
        val row = LinearLayout(context)
        val rowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = rowParams

        for (columnIndex in nonogram.columnCounts!!.counts.indices) {
            row.addView(getColumnCountView(columnIndex, cellSize))
        }
        return row
    }

    private fun getColumnCountView(
        index: Int,
        cellSize: Int
    ): LinearLayout {
        // make a new layout
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.BOTTOM
        val counts = nonogram.columnCounts!!.get(index)

        // add the value in this row for each column
        for (i in counts.indices) {
            val countView = GroupCountCellView(
                context,
                counts[i],
                cellSize,
                String.format(context.getString(R.string.tag_column_count), index, i),
                onCountClick
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

}