package niedermeyer.nonogram.gui;

import android.util.TypedValue;
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
import niedermeyer.nonogram.logics.CountValue;
import niedermeyer.nonogram.logics.FilledFieldsCount;
import niedermeyer.nonogram.logics.NonogramGenerator;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

/**
 * @author Elen Niedermeyer, last updated 2020-10-18
 */
public class CountFilledFieldsDisplayer {

    private static final float TEXT_SIZE_FACTOR = 0.7f;
    private static final float TEXT_MARGIN_FACTOR = 0.5f;

    /**
     * Context activity.
     */
    private GameActivity activity;
    private NonogramGenerator nonogram;

    /**
     * Counts
     */
    private FilledFieldsCount columnCounts;
    private FilledFieldsCount rowCounts;

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

            // toggle the counts background
            if (rowOrColumn.equals(activity.getString(R.string.row))) {
                toggleCount(rowCounts, v, outerIndex, innerIndex);
            } else if (rowOrColumn.equals(activity.getString(R.string.column))) {
                toggleCount(columnCounts, v, outerIndex, innerIndex);

            }
        }
    };

    /**
     * Constructor. Initializes {@link #activity} and {@link #nonogram}.
     *
     * @param pActivity the context activity
     * @param pNonogram the current nonogram
     */
    public CountFilledFieldsDisplayer(GameActivity pActivity, NonogramGenerator pNonogram) {
        activity = pActivity;
        nonogram = pNonogram;
    }

    /**
     * Getter for {@link #columnCounts}.
     *
     * @return {@link #columnCounts}
     */
    public FilledFieldsCount getColumnCounts() {
        return columnCounts;
    }

    /**
     * Getter for {@link #rowCounts}.
     *
     * @return {@link #rowCounts}
     */
    public FilledFieldsCount getRowCounts() {
        return rowCounts;
    }

    /**
     * Adds the counts to the given table by {@link #addCountsRows(TableLayout)} and {@link #addColumnCounts(TableLayout)}.
     * The row counts must be added first.
     *
     * @param pTable the table layout on which tha counts should be added
     * @return the given modified table layout
     */
    public TableLayout addCounts(TableLayout pTable) {
        pTable = addCountsRows(pTable);
        pTable = addColumnCounts(pTable);
        return pTable;
    }

    /**
     * Initializes or updates {@link #rowCounts}.
     * Makes a view with counts at the start of each row of the given table.
     *
     * @param pTable the table where the counts should be added
     * @return the modified table
     */
    private TableLayout addCountsRows(TableLayout pTable) {
        // get row counts
        rowCounts = nonogram.getCountsRows();

        // add the counts at the start of each row of the table
        for (int rowCounter = 0; rowCounter < rowCounts.getCountsList().size(); rowCounter++) {
            // make a new layout
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.LEFT);

            ArrayList<CountValue> counts = rowCounts.get(rowCounter);

            // add an text view for each count of the row
            for (int innerCounter = 0; innerCounter < counts.size(); innerCounter++) {
                int value = counts.get(innerCounter).getValue();

                // make a new text view
                TextView countView = new TextView(activity);
                countView.setText(String.format(Locale.getDefault(), "%d", value));
                countView.setTextSize(TypedValue.COMPLEX_UNIT_PX, new GameOptionsPersistence(activity).getCellSize() * TEXT_SIZE_FACTOR);
                countView.setTag(String.format(activity.getString(R.string.tag_row_count), rowCounter, innerCounter));
                countView.setClickable(true);
                countView.setOnClickListener(countOnClick);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, (int) (new GameOptionsPersistence(activity).getCellSize() * TEXT_MARGIN_FACTOR), 0);
                countView.setLayoutParams(params);

                // load the crossed out background if necessary
                if (counts.get(innerCounter).getIsCrossedOut()) {
                    countView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
                }

                // add the view to the new layout
                layout.addView(countView);
            }

            // get the row and add the new layout at index 0
            TableRow row = (TableRow) pTable.getChildAt(rowCounter);
            row.addView(layout, 0);
        }

        return pTable;
    }

    /**
     * Initializes or updates {@link #columnCounts}.
     * Makes rows with counts for the columns.
     *
     * @param pTable the table where the counts should be added
     * @return the modified table
     */
    private TableLayout addColumnCounts(TableLayout pTable) {
        // get column counts
        columnCounts = nonogram.getCountsColumns();

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        // make a new row
        TableRow columnCountsRow = new TableRow(activity);
        columnCountsRow.setLayoutParams(rowParams);
        columnCountsRow.setGravity(Gravity.TOP);
        // add an empty text view at the start, here are the row counts
        columnCountsRow.addView(new LinearLayout(activity));

        for (int columnCounter = 0; columnCounter < columnCounts.getCountsList().size(); columnCounter++) {
            // make a new layout
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.TOP);

            ArrayList<CountValue> counts = columnCounts.get(columnCounter);

            // add the value in this row for each column
            for (int innerCounter = 0; innerCounter < counts.size(); innerCounter++) {
                int value = counts.get(innerCounter).getValue();

                // make a new text view
                TextView countView = new TextView(activity);
                countView.setText(String.format(Locale.getDefault(), "%d", value));
                countView.setTextSize(TypedValue.COMPLEX_UNIT_PX, new GameOptionsPersistence(activity).getCellSize() * TEXT_SIZE_FACTOR);
                countView.setTag(String.format(activity.getString(R.string.tag_row_count), columnCounter, innerCounter));
                countView.setClickable(true);
                countView.setOnClickListener(countOnClick);
                countView.setGravity(Gravity.CENTER_HORIZONTAL);

                // load the crossed out background if necessary
                if (counts.get(innerCounter).getIsCrossedOut()) {
                    countView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
                }

                // add the view to the new layout
                layout.addView(countView);
            }

            // add the new row to the table
            columnCountsRow.addView(layout);
        }

        pTable.addView(columnCountsRow, 0);
        return pTable;
    }

    /**
     * Toggles the background of the given view. It would be striked, if it wasn't before and the other way around.
     *
     * @param pCounts           the {@link FilledFieldsCount} object that contains the count
     * @param pView             the view to toggle the background
     * @param pOuterIndexOfView the outer index of the view in {@link #rowCounts}
     * @param pInnerIndexOfView the inner index of the view in {@link #rowCounts}
     */
    private void toggleCount(FilledFieldsCount pCounts, View pView, int pOuterIndexOfView, int pInnerIndexOfView) {
        // if the clicked view was a row count
        // toggle the background, stroke or not
        if (pCounts.isValueCrossedOut(pOuterIndexOfView, pInnerIndexOfView)) {
            // set the background to nothing if it is stroke
            pView.setBackgroundResource(0);
        } else {
            // set the strike resource
            pView.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
        pCounts.toggleCrossedOut(pOuterIndexOfView, pInnerIndexOfView);
    }
}
