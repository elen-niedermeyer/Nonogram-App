package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.CountValue;
import niedermeyer.nonogram.logics.GroupCount;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
public class GroupCountDisplayer {

    private static final float TEXT_MARGIN_FACTOR = 0.5f;

    private final Context context;

    /**
     * Constructor. Initializes {@link #context}.
     *
     * @param pContext the context activity
     */
    public GroupCountDisplayer(Context pContext) {
        context = pContext;
    }

    /**
     * Makes a view with counts at the given index.
     *
     * @param pRowCount the row count
     * @param pIndex    index of the row
     * @return the modified table
     */
    public LinearLayout getRowCountView(GroupCount pRowCount, int pIndex, View.OnClickListener pOnCountClick) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ArrayList<CountValue> counts = pRowCount.getList(pIndex);

        // add an view for each count of the row
        for (int i = 0; i < counts.size(); i++) {
            // make a new view
            GroupCountCell countView = new GroupCountCell(context, counts.get(i), String.format(context.getString(R.string.tag_row_count), pIndex, i), (int) ((new GameOptionsPersistence(context).getCellSize() * TEXT_MARGIN_FACTOR) / 2), pOnCountClick);
            // add the view to the new layout
            layout.addView(countView);
        }

        return layout;
    }

    public TableRow getColumnCountRow(GroupCount pColumnCount, View.OnClickListener pOnCountClick) {
        TableRow tableRow = new TableRow(context);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(rowParams);

        // add an empty text view at the start, here are the row counts
        tableRow.addView(new LinearLayout(context));

        for (int columnIndex = 0; columnIndex < pColumnCount.getCounts().size(); columnIndex++) {
            tableRow.addView(getColumnCountView(pColumnCount, columnIndex, pOnCountClick));
        }
        return tableRow;
    }

    /**
     * Makes row with counts for the columns.
     *
     * @param pColumnCount the column count
     * @param pIndex
     * @return the modified table
     */
    public LinearLayout getColumnCountView(GroupCount pColumnCount, int pIndex, View.OnClickListener pOnCountClick) {
        // make a new layout
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.BOTTOM);

        ArrayList<CountValue> counts = pColumnCount.getList(pIndex);

        // add the value in this row for each column
        for (int i = 0; i < counts.size(); i++) {
            GroupCountCell countView = new GroupCountCell(context, counts.get(i), String.format(context.getString(R.string.tag_column_count), pIndex, i), pOnCountClick);
            // add the view to the new layout
            layout.addView(countView);
        }

        return layout;
    }

}
