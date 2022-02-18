package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import niedermeyer.nonogram.logics.GroupCounts
import android.widget.LinearLayout
import niedermeyer.nonogram.R
import niedermeyer.nonogram.persistence.GameOptionsPersistence
import android.widget.TableLayout
import android.view.Gravity
import android.view.View
import android.widget.TableRow

class GroupCountDisplayer(private val context: Context) {

    private val textPaddingFactor = 0.5f

    fun getRowCountView(
        rowCount: GroupCounts,
        index: Int,
        pOnCountClick: View.OnClickListener?
    ): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        val counts = rowCount.get(index)

        // add an view for each count of the row
        for (i in counts.indices) {
            // make a new view
            val countView = GroupCountCellView(
                context,
                counts[i],
                String.format(context.getString(R.string.tag_row_count), index, i),
                pOnCountClick!!,
                (GameOptionsPersistence(context).cellSize * textPaddingFactor / 2).toInt()
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

    fun getColumnCountRow(
        pColumnCount: GroupCounts,
        pOnCountClick: View.OnClickListener?
    ): TableRow {
        val tableRow = TableRow(context)
        val rowParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        tableRow.layoutParams = rowParams

        // add an empty text view at the start, here are the row counts
        tableRow.addView(LinearLayout(context))
        for (columnIndex in pColumnCount.counts.indices) {
            tableRow.addView(getColumnCountView(pColumnCount, columnIndex, pOnCountClick))
        }
        return tableRow
    }

    private fun getColumnCountView(
        columnCount: GroupCounts,
        index: Int,
        pOnCountClick: View.OnClickListener?
    ): LinearLayout {
        // make a new layout
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.BOTTOM
        val counts = columnCount.get(index)

        // add the value in this row for each column
        for (i in counts.indices) {
            val countView = GroupCountCellView(
                context,
                counts[i],
                String.format(context.getString(R.string.tag_column_count), index, i),
                pOnCountClick!!,
                null
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

}