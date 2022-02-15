package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
public class GameFieldDisplayer {

    private final Context context;

    public GameFieldDisplayer(Context pContext) {
        context = pContext;
    }

    public TableLayout getPuzzleView(int[][] pUserField, int pCellSize, View.OnClickListener pOnFieldClick) {
        TableLayout table = new TableLayout(context);

        // add rows of nonogram
        for (int rowIndex = 0; rowIndex < pUserField.length; rowIndex++) {
            // new row
            GameFieldRow rowView = new GameFieldRow(context);

            // add a view for each cell in this row
            for (int columnIndex = 0; columnIndex < pUserField[rowIndex].length; columnIndex++) {
                // set id such that row_index = id/100 and column_index = id%100
                GameFieldCell newCell = new GameFieldCell(context, (rowIndex * 100) + columnIndex, pCellSize, pUserField[rowIndex][columnIndex], pOnFieldClick);
                rowView.addView(newCell);
            }

            table.addView(rowView);
        }

        return table;
    }

}
