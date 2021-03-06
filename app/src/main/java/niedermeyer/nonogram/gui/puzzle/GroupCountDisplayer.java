package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.CountValue;
import niedermeyer.nonogram.logics.GroupCount;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2020-12-11
 */
public class GroupCountDisplayer {

    private static final float TEXT_SIZE_FACTOR = 0.7f;
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

        // add an text view for each count of the row
        for (int i = 0; i < counts.size(); i++) {
            // make a new text view
            TextView countView = getTextView(counts.get(i), pOnCountClick);
            countView.setContentDescription(String.format(context.getString(R.string.tag_row_count), pIndex, i));
            countView.setPadding((int) ((new GameOptionsPersistence(context).getCellSize() * TEXT_MARGIN_FACTOR) / 2), 0, (int) ((new GameOptionsPersistence(context).getCellSize() * TEXT_MARGIN_FACTOR) / 2), 0);

            // load the crossed out background if necessary
            if (counts.get(i).isCrossedOut()) {
                countView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
            }

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
            TextView countView = getTextView(counts.get(i), pOnCountClick);
            countView.setContentDescription(String.format(context.getString(R.string.tag_column_count), pIndex, i));

            // add the view to the new layout
            layout.addView(countView);
        }

        return layout;
    }

    private TextView getTextView(CountValue pCountValue, View.OnClickListener pOnCountClick) {
        // make a new text view
        TextView countView = new TextView(context);
        countView.setText(String.format(Locale.getDefault(), "%d", pCountValue.getValue()));
        countView.setTextSize(TypedValue.COMPLEX_UNIT_PX, new GameOptionsPersistence(context).getCellSize() * TEXT_SIZE_FACTOR);
        countView.setClickable(true);
        countView.setOnClickListener(pOnCountClick);
        countView.setGravity(Gravity.CENTER_HORIZONTAL);

        // load the crossed out background if necessary
        if (pCountValue.isCrossedOut()) {
            countView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
        return countView;
    }


    /**
     * Toggles the background of the given view. It would be stroked, if it wasn't before and the other way around.
     *
     * @param pCounts           the {@link GroupCount} object that contains the count
     * @param pView             the view to toggle the background
     * @param pOuterIndexOfView the outer index of the view
     * @param pInnerIndexOfView the inner index of the view
     */
    public void toggleCount(GroupCount pCounts, View pView, int pOuterIndexOfView, int pInnerIndexOfView) {
        // if the clicked view was a row count
        // toggle the background, stroke or not
        if (pCounts.isValueCrossedOut(pOuterIndexOfView, pInnerIndexOfView)) {
            // set the background to nothing if it is stroke
            pView.setBackgroundResource(0);
        } else {
            // set the strike resource
            pView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
    }
}
