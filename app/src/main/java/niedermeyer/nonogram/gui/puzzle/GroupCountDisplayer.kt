package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.GroupCounts

class GroupCountDisplayer(private val context: Context) {

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
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

    fun getColumnCountRow(
        pColumnCount: GroupCounts,
        pOnCountClick: View.OnClickListener?
    ): LinearLayout {
        val row = LinearLayout(context)
        val rowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = rowParams

        for (columnIndex in pColumnCount.counts.indices) {
            row.addView(getColumnCountView(pColumnCount, columnIndex, pOnCountClick))
        }
        return row
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
                pOnCountClick!!
            )
            // add the view to the new layout
            layout.addView(countView)
        }
        return layout
    }

}