package niedermeyer.nonogram.gui;

import android.view.Gravity;
import android.view.View;
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
 * @author Elen Niedermeyer, last updated 2017-09-30
 */
public class CountFilledFieldsDisplayer {

    private NonogramActivity activity;
    private NonogramGenerator nonogram;

    private CountFilledFields columnCounts;
    private CountFilledFields rowCounts;

    private View.OnClickListener countOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            String[] tagParsed = tag.split("_");
            int outerIndex = Integer.parseInt(tagParsed[1]);
            int innerIndex = Integer.parseInt(tagParsed[2]);
            if (tagParsed[0].equals("row")) {
                if (rowCounts.isStriked(outerIndex, innerIndex)) {
                    v.setBackgroundResource(0);
                } else {
                    v.setBackgroundResource(R.drawable.game_count_striked_off);
                }
                rowCounts.toggleStriked(outerIndex, innerIndex);

            } else if (tagParsed[0].equals("column")) {
                if (columnCounts.isStriked(outerIndex, innerIndex)) {
                    v.setBackgroundResource(0);
                } else {
                    v.setBackgroundResource(R.drawable.game_count_striked_off);
                }
                columnCounts.toggleStriked(outerIndex, innerIndex);
            }
        }
    };

    public CountFilledFieldsDisplayer(NonogramActivity pActivity, NonogramGenerator pNonogram) {
        activity = pActivity;
        nonogram = pNonogram;
    }

    public TableLayout addCounts(TableLayout tab) {
        tab = addRowCounts(tab);
        tab = addColumnCounts(tab);
        return tab;
    }

    private TableLayout addColumnCounts(TableLayout pTable) {
        // get column counts
        columnCounts = nonogram.getColumnCounts();

        // get maximum of numbers
        int neededRows = 0;
        for (int i = 0; i < columnCounts.getSizeOuter(); i++) {
            int x = columnCounts.getSizeInner(i);
            if (x > neededRows) {
                neededRows = x;
            }
        }

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;

        for (int x = 0; x < neededRows; x++) {
            TableRow row = new TableRow(activity);
            // add an empty text view
            row.addView(new TextView(activity));
            for (int i = 0; i < columnCounts.getSizeOuter(); i++) {
                int valuesSize = columnCounts.getSizeInner(i);
                if (valuesSize >= x + 1) {
                    TextView count = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_top, null);
                    count.setText(String.format(Locale.getDefault(), "%d", columnCounts.get(i, x)));
                    count.setTag(String.format(activity.getString(R.string.tag_colum_count), i, x));
                    count.setClickable(true);
                    count.setOnClickListener(countOnClick);
                    row.addView(count);
                } else {
                    row.addView(new TextView(activity));
                }
            }
            row.setLayoutParams(rowParams);
            pTable.addView(row, x);
        }

        return pTable;
    }

    private TableLayout addRowCounts(TableLayout pTable) {
        // get row counts
        rowCounts = nonogram.getRowCounts();

        int counts = pTable.getChildCount();

        for (int i = 0; i < counts; i++) {
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            ArrayList<Integer> values = rowCounts.get(i);
            for (int value : values) {
                int innerIndex = values.indexOf(value);
                TextView count = (TextView) activity.getLayoutInflater().inflate(R.layout.activity_nonogram_count_left, null);
                count.setText(String.format(Locale.getDefault(), "%d", value));
                count.setTag(String.format(activity.getString(R.string.tag_row_count), i, innerIndex));
                count.setClickable(true);
                count.setOnClickListener(countOnClick);
                layout.addView(count);
            }
            TableRow row = (TableRow) pTable.getChildAt(i);
            row.addView(layout, 0);
        }

        return pTable;
    }
}
