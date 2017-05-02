package niedermeyer.nonogram;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import niedermeyer.nonogram.logics.NonogramFields;
import niedermeyer.nonogram.logics.NonogramGenerator;

/**
 * @author Elen Niedermeyer, last updated 2017-04-28
 */

public class GameHandler extends Handler {

    private Activity activity;

    private NonogramGenerator generator = new NonogramGenerator();
    private int[][] nonogram;
    private Map<Integer, ArrayList<Integer>> rowCounts;
    private Map<Integer, ArrayList<Integer>> columnCounts;

    private int[][] actualField;

    public GameHandler(Activity pActivity) {
        activity = pActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        int[] numbers = (int[]) msg.obj;
        int numberOfRows = numbers[0];
        int numberOfColumns = numbers[1];

        generator.makeNewGame(numberOfRows, numberOfColumns);
        nonogram = generator.getNonogram();
        rowCounts = generator.getCountsRow();
        columnCounts = generator.getCountsColumn();

        actualField = new int[numberOfRows][numberOfColumns];

        generateGameField();
    }

    private void generateGameField() {
        TableLayout table = (TableLayout) activity.findViewById(R.id.game_table);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < nonogram.length; i++) {
            // new row
            TableRow row = new TableRow(activity);
            row.setLayoutParams(rowParams);

            // add count for this row
            row.addView(addRowCount(i));
            // add a button for each field
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
                // add button to row
                row.addView(b);
                // initialize field in array
                actualField[i][j] = NonogramFields.NOTHING.getValue();
            }
            // add row to table
            table.addView(row);
        }
    }

    private TextView addRowCount(int i) {
        ArrayList<Integer> values = rowCounts.get(i);
        String countsAsText = "";
        for (int value : values) {
            countsAsText += value + " ";
        }

        TextView counts = new TextView(activity);
        counts.setText(countsAsText);

        return counts;
    }
}
