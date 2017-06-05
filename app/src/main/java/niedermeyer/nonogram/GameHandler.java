package niedermeyer.nonogram;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import niedermeyer.nonogram.logics.NonogramFields;
import niedermeyer.nonogram.logics.NonogramGenerator;

/**
 * @author Elen Niedermeyer, last updated 2017-05-12
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

    public void newGame() {
        Message msg = new Message();
        int[] gameSize = {NonogramActivity.numberOfRows, NonogramActivity.numberOfColumns};
        msg.obj = gameSize;
        this.sendMessage(msg);
    }

    public void resetGame() {
        generateGameField();
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

        int c;
        if (r == 0) {
            c = id;
        } else if (r > 0 && r < 10) {
            String jString = idString.substring(1);
            c = Integer.parseInt(jString);
        } else {
            String jString = idString.substring(2);
            c = Integer.parseInt(jString);
        }

        fieldValue = actualField[r][c];

        if (fieldValue == NonogramFields.NOTHING.getValue()) {
            v.setBackgroundResource(R.drawable.button_field_black);
            actualField[r][c] = NonogramFields.PROVED.getValue();
        } else if (fieldValue == NonogramFields.PROVED.getValue()) {
            v.setBackgroundResource(R.drawable.button_field_cross);
            actualField[r][c] = NonogramFields.EMPTY.getValue();
        } else if (fieldValue == NonogramFields.EMPTY.getValue()) {
            v.setBackgroundResource(R.drawable.button_field_white);
            actualField[r][c] = NonogramFields.NOTHING.getValue();
        }

        int[][] actualFieldCopy = new int[actualField.length][actualField[0].length];
        for (int i = 0; i < actualField.length; i++) {
            for (int j = 0; j < actualField[i].length; j++) {
                if (actualField[i][j] == -1) {
                    actualFieldCopy[i][j] = NonogramFields.EMPTY.getValue();
                } else {
                    actualFieldCopy[i][j] = actualField[i][j];
                }
            }
        }

        if (Arrays.deepEquals(actualFieldCopy, nonogram)) {
            newGame();
        }
    }

    /**
     * Makes the game field.
     * Makes a table row for each row. IDs are the place in the nonogram array.
     * Adds the counts left for the rows and on the top for the columns.
     */
    private void generateGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.game_table);
        // clear the field, remove all rows from table
        table.removeAllViews();

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;

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
                b.setBackgroundResource(R.drawable.button_field_white);
                // set size
                int buttonSize = (int) activity.getResources().getDimension(R.dimen.field_button_size);
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
                if (!countsAsText.equals("")) {
                    countsAsText += "\n" + value;
                } else {
                    countsAsText += value;
                }
            }

            // makes the new text view with the pasted text
            TextView counts = (TextView) activity.getLayoutInflater().inflate(R.layout.count_view_top, null);
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
            if (!countsAsText.equals("")) {
                countsAsText += "   " + value;
            } else {
                countsAsText += value;
            }
        }

        // makes the new text view with the pasted string
        TextView counts = (TextView) activity.getLayoutInflater().inflate(R.layout.count_view_left, null);
        counts.setText(countsAsText);

        return counts;
    }
}
