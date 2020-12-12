package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import niedermeyer.nonogram.logics.GroupCount;

public class PuzzleDisplayer {

    private Context context;

    public PuzzleDisplayer(Context pContext) {
        context = pContext;
    }

    public TableLayout getGameView(int[][] pCurrentUserField, GroupCount pRowCount, GroupCount pColumnCount, int pCellSize, View.OnClickListener onFieldClick) {

        final GameFieldDisplayer fieldDisplayer = new GameFieldDisplayer(context);
        final GroupCountDisplayer groupCountDisplayer = new GroupCountDisplayer(context);
        TableLayout table = fieldDisplayer.getPuzzleView(pCurrentUserField, pCellSize, onFieldClick);

        for (int i = 0; i < pCurrentUserField.length; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            // add row count at the left
            LinearLayout rowCount = groupCountDisplayer.getRowCountView(pRowCount, i);
            rowCount.setGravity(Gravity.END);
            row.addView(rowCount, 0);
            // add row count at the right
            rowCount = groupCountDisplayer.getRowCountView(pRowCount, i);
            rowCount.setGravity(Gravity.START);
            row.addView(rowCount);
        }

        // add column count at the top
        TableRow columnCount = groupCountDisplayer.getColumnCountRow(pColumnCount);
        columnCount.setGravity(Gravity.BOTTOM);
        table.addView(columnCount, 0);
        // add column count at the bottom
        columnCount = groupCountDisplayer.getColumnCountRow(pColumnCount);
        columnCount.setGravity(Gravity.TOP);
        table.addView(columnCount);

        return table;
    }

}
