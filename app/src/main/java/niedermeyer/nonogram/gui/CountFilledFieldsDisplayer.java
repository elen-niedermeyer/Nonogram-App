package niedermeyer.nonogram.gui;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.CountFilledFields;
import niedermeyer.nonogram.logics.NonogramGenerator;

/**
 * @author Elen Niedermeyer, last updated 2017-10-07
 */
public class CountFilledFieldsDisplayer {

    /**
     * Context activity.
     */
    private NonogramActivity activity;
    private NonogramGenerator nonogram;

    /**
     * Counts
     */
    private CountFilledFields columnCounts;
    private CountFilledFields rowCounts;

    /**
     * listener for each count view
     */
    private OnClickListener countOnClick = new OnClickListener() {
        /**
         * Parses the clicked views tag.
         * Toggles the background of the view.
         *
         * @param v clicked view
         */
        @Override
        public void onClick(View v) {
            // parse the tag
            String tag = (String) v.getTag();
            String[] tagParsed = tag.split(activity.getString(R.string.string_devider));
            String rowOrColumn = tagParsed[0];
            int outerIndex = Integer.parseInt(tagParsed[1]);
            int innerIndex = Integer.parseInt(tagParsed[2]);

            if (rowOrColumn.equals(activity.getString(R.string.row))) {
                toggleRowCount(v, outerIndex, innerIndex);
            } else if (rowOrColumn.equals(activity.getString(R.string.column))) {
                toggleColumnCount(v, outerIndex, innerIndex);

            }
        }
    };

    /**
     * Constructor. Initializes {@link #activity} and {@link #nonogram}.
     *
     * @param pActivity the context activity
     * @param pNonogram the current nonogram
     */
    public CountFilledFieldsDisplayer(NonogramActivity pActivity, NonogramGenerator pNonogram) {
        activity = pActivity;
        nonogram = pNonogram;
    }

    /**
     * Adds the counts to the given table by {@link #addRowCounts(TableLayout)} and {@link #addColumnCounts(TableLayout)}.
     * The row counts must be added first.
     *
     * @param pTable the table layout on which tha counts should be added
     * @return the given modified table layout
     */
    public TableLayout addCounts(TableLayout pTable) {
        pTable = addRowCounts(pTable);
        pTable = addColumnCounts(pTable);
        return pTable;
    }

    /**
     * Toggles the background of the given view. It would be striked, if it wasn't before and the other way around.
     *
     * @param pView             the view to toggle the background
     * @param pOuterIndexOfView the outer index of the view in {@link #rowCounts}
     * @param pInnerIndexOfView the inner index of the view in {@link #rowCounts}
     */
    private void toggleRowCount(View pView, int pOuterIndexOfView, int pInnerIndexOfView) {
        // if the clicked view was a row count
        // toggle the background, stroke or not
        if (rowCounts.isStriked(pOuterIndexOfView, pInnerIndexOfView)) {
            // set the background to nothing if it is stroke
            pView.setBackgroundResource(0);
        } else {
            // set the strike resource
            pView.setBackgroundResource(R.drawable.game_count_striked_off);
        }
        rowCounts.toggleStriked(pOuterIndexOfView, pInnerIndexOfView);
    }

    /**
     * Toggles the background of the given view. It would be striked, if it wasn't before and the other way around.
     *
     * @param pView             the view to toggle the background
     * @param pOuterIndexOfView the outer index of the view in {@link #columnCounts}
     * @param pInnerIndexOfView the inner index of the view in {@link #columnCounts}
     */
    private void toggleColumnCount(View pView, int pOuterIndexOfView, int pInnerIndexOfView) {
        // if the clicked view was a column count
        // toggle the background, stroke or not
        if (columnCounts.isStriked(pOuterIndexOfView, pInnerIndexOfView)) {
            // set the background to nothing if it is stroke
            pView.setBackgroundResource(0);
        } else {
            // set the strike resource
            pView.setBackgroundResource(R.drawable.game_count_striked_off);
        }
        columnCounts.toggleStriked(pOuterIndexOfView, pInnerIndexOfView);
    }

    /**
     * Initializes or updates {@link #rowCounts}.
     * Makes a view with counts at the start of each row of the given table.
     *
     * @param pTable the table where the counts should be added
     * @return the modified table
     */
    private TableLayout addRowCounts(TableLayout pTable) {
        // get row counts
        rowCounts = nonogram.getRowCounts();

        int numberOfRows = pTable.getChildCount();
        // add the counts at the start of each row of the table
        for (int counterRow = 0; counterRow < numberOfRows; counterRow++) {
            // make a new layout
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            ArrayList<Integer> counts = rowCounts.get(counterRow);
            // add an text view for each count of the row
            for (int value : counts) {
                // get the inner index of the current value
                int innerIndex = counts.indexOf(value);

                // make a new text view
                TextView countView = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_left, null);
                countView.setText(String.format(Locale.getDefault(), "%d", value));
                countView.setTag(String.format(activity.getString(R.string.tag_row_count), counterRow, innerIndex));
                countView.setClickable(true);
                countView.setOnClickListener(countOnClick);

                // add the view to the new layout
                layout.addView(countView);
            }

            // get the row and add the new layout at index 0
            TableRow row = (TableRow) pTable.getChildAt(counterRow);
            row.addView(layout, 0);
        }

        return pTable;
    }

    //TODO: Think about how to add this column counts

    /**
     * Initializes or updates {@link #columnCounts}.
     * Makes rows with counts for the counts.
     *
     * @param pTable the table where the counts should be added
     * @return the modified table
     */
    private TableLayout addColumnCounts(TableLayout pTable) {
        // get column counts
        columnCounts = nonogram.getColumnCounts();

        // get maximum of counts for a column
        int maxNumberOfCounts = 0;
        for (int i = 0; i < columnCounts.getOuterSize(); i++) {
            int x = columnCounts.getInnerSize(i);
            if (x > maxNumberOfCounts) {
                maxNumberOfCounts = x;
            }
        }

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;
        // add a rows for the counts
        for (int counterRow = 0; counterRow < maxNumberOfCounts; counterRow++) {
            // make a new row
            TableRow row = new TableRow(activity);
            // add an empty text view at the start, here are the row counts
            row.addView(new TextView(activity));

            // add the value in this row for each column
            for (int columnIndex = 0; columnIndex < columnCounts.getOuterSize(); columnIndex++) {
                int valuesSize = columnCounts.getInnerSize(columnIndex);
                if (valuesSize >= counterRow + 1) {
                    // make a text view with the value if there is one left
                    TextView count = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_top, null);
                    count.setText(String.format(Locale.getDefault(), "%d", columnCounts.get(columnIndex, counterRow)));
                    count.setTag(String.format(activity.getString(R.string.tag_colum_count), columnIndex, counterRow));
                    count.setClickable(true);
                    count.setOnClickListener(countOnClick);
                    // add the view to the new row
                    row.addView(count);
                } else {
                    // add an empty view if all counts of this column are already added
                    row.addView(new TextView(activity));
                }
            }

            // add the new row to the table
            row.setLayoutParams(rowParams);
            pTable.addView(row, counterRow);
        }

        return pTable;
    }
}
