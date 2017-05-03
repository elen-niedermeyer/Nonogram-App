package niedermeyer.nonogram;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import niedermeyer.nonogram.logics.NonogramFields;
import niedermeyer.nonogram.logics.NonogramGenerator;

/**
 * @author Elen Niedermeyer, last updated 2017-05-02
 */

public class GameHandler extends Handler implements OnClickListener {

    private Activity activity;

    private NonogramGenerator generator = new NonogramGenerator();
    private int[][] nonogram;
    private Map<Integer, ArrayList<Integer>> rowCounts;
    private Map<Integer, ArrayList<Integer>> columnCounts;

    private int[][] actualField;

    /**
     * Constructor.
     *
     * @param pActivity the activity in which the game handler is used
     */
    public GameHandler(Activity pActivity) {
        activity = pActivity;
    }

    /**
     * Overrides the method handleMessage in the class Handler.
     * Creates a new game field with the size which is given by the message.
     *
     * @param msg
     */
    @Override
    public void handleMessage(Message msg) {
        // get message object
        int[] numbers = (int[]) msg.obj;
        int numberOfRows = numbers[0];
        int numberOfColumns = numbers[1];

        // make new game field and initialize the private fields
        generator.makeNewGame(numberOfRows, numberOfColumns);
        nonogram = generator.getNonogram();
        rowCounts = generator.getCountsRow();
        columnCounts = generator.getCountsColumn();

        // make a new array for the actual game field
        actualField = new int[numberOfRows][numberOfColumns];

        generateGameField();
    }

    @Override
    public void onClick(View v) {
        int fieldValue;

        TableRow row = (TableRow) v.getParent();
        int r = row.getId();

        int id = v.getId();
        String idString = Integer.toString(id);

        int i;
        int j;
        if (r == 0) {
            i = 0;
            j = id;
        } else if (r > 0 && r < 10) {
            String iString = idString.substring(0, 1);
            i = Integer.parseInt(iString);
            String jString = idString.substring(1);
            j = Integer.parseInt(jString);
        } else {
            String iString = idString.substring(0, 2);
            i = Integer.parseInt(iString);
            String jString = idString.substring(2);
            j = Integer.parseInt(jString);
        }

        fieldValue = actualField[i][j];

        if (fieldValue == NonogramFields.NOTHING.getValue()) {
            v.setBackgroundResource(R.drawable.button_black);
            actualField[i][j] = NonogramFields.PROVED.getValue();
        } else if (fieldValue == NonogramFields.PROVED.getValue()) {
            v.setBackgroundResource(R.drawable.button_cross);
            actualField[i][j] = NonogramFields.EMPTY.getValue();
        } else if (fieldValue == NonogramFields.EMPTY.getValue()) {
            v.setBackgroundResource(R.drawable.button_white);
            actualField[i][j] = NonogramFields.NOTHING.getValue();
        }
    }

    /**
     * Makes the game field in the layout.
     * Makes a table row for each row. IDs are the place in the nonogram array.
     * Adds the counts left for the rows and on the top for the columns.
     */
    private void generateGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.game_table);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        // add row with counts of the columns
        TableRow columnCounts = makeColumnCountRow();
        columnCounts.setLayoutParams(rowParams);
        table.addView(columnCounts);

        // add rows of nonogram
        for (int i = 0; i < nonogram.length; i++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setId(i);
            row.setLayoutParams(rowParams);

            // add count for this row
            row.addView(makeRowCountView(i));
            // add a button for each field in this row
            for (int j = 0; j < nonogram[i].length; j++) {
                Button b = new Button(activity);
                // make and set id
                String id = i + "" + j;
                b.setId(Integer.parseInt(id));
                // set background
                b.setBackgroundResource(R.drawable.button_white);
                // set size
                int buttonSize = (int) activity.getResources().getDimension(R.dimen.button_size);
                b.setLayoutParams(new TableRow.LayoutParams(buttonSize, buttonSize));
                // set the onClickListener implemented in this class
                b.setOnClickListener(this);
                // add button to row
                row.addView(b);
                // initialize field in array
                actualField[i][j] = NonogramFields.NOTHING.getValue();
            }
            // add row to table
            table.addView(row);
        }
    }

    /**
     * Makes a table column that shows the column counts in text views.
     * Numbers in one text view are separated by line separator.
     *
     * @return a table row with column counts
     */
    private TableRow makeColumnCountRow() {
        TableRow row = new TableRow(activity);

        // add an empty text view
        row.addView(new TextView(activity));

        // add filled views for all columns
        for (int i = 0; i < columnCounts.size(); i++) {
            ArrayList<Integer> values = columnCounts.get(i);
            String countsAsText = "";
            // paste all values for one column to one string
            for (int value : values) {
                countsAsText += "\n" + value;
            }

            // makes the new text view with the pasted text
            TextView counts = new TextView(activity, null, R.style.CountViews);
            counts.setText(countsAsText);

            // add the text view to the row
            row.addView(counts);
        }

        return row;
    }

    /**
     * Makes a text view with the row count of row i. Numbers in the text view are separated by tabs.
     *
     * @param i the number of the row, which count should be in the text view
     * @return a text view with the row counts of row i
     */
    private TextView makeRowCountView(int i) {
        ArrayList<Integer> values = rowCounts.get(i);
        String countsAsText = "";
        // paste all numbers for this row to one string
        for (int value : values) {
            countsAsText += "\t" + value;
        }

        // makes the new text view with the pasted string
        TextView counts = new TextView(activity, null, R.style.CountViews);
        counts.setText(countsAsText);

        return counts;
    }
}
