package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;

/**
 * @author Elen Niedermeyer, last modified 2020-12-12
 */
public class GameFieldDisplayer {

    private final Context context;

    public GameFieldDisplayer(Context pContext) {
        context = pContext;
    }

    public static int getFieldBackgroundResource(int pValue) {
        if (pValue == NonogramConstants.FIELD_FILLED) {
            return R.drawable.game_cell_black;
        } else if (pValue == NonogramConstants.FIELD_EMPTY) {
            return R.drawable.game_cell_cross;
        } else {
            return R.drawable.game_cell_white;
        }
    }

    public TableLayout getPuzzleView(int[][] pUserField, int pCellSize, View.OnClickListener pOnFieldClick) {

        TableLayout table = new TableLayout(context);

        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // add rows of nonogram
        for (int rowIndex = 0; rowIndex < pUserField.length; rowIndex++) {
            // new row
            TableRow rowView = new TableRow(context);
            rowView.setLayoutParams(rowParams);
            rowView.setGravity(Gravity.CENTER);

            // add a button for each field in this row
            for (int columnIndex = 0; columnIndex < pUserField[rowIndex].length; columnIndex++) {
                Button newButton = makeFieldButton(pUserField[rowIndex][columnIndex], pCellSize);

                // set id such that
                // row_index = id/100
                // column_index = id%100
                newButton.setId((rowIndex * 100) + columnIndex);

                newButton.setOnClickListener(pOnFieldClick);
                rowView.addView(newButton);
            }

            table.addView(rowView);
        }

        return table;
    }

    /**
     * Makes a button for the field.
     *
     * @return a new button
     */
    private Button makeFieldButton(int pValue, int pCellSize) {
        Button b = new Button(context);

        // set size
        b.setLayoutParams(new TableRow.LayoutParams(pCellSize, pCellSize));

        b.setBackgroundResource(getFieldBackgroundResource(pValue));

        return b;
    }


}
