package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.GroupCount;

import static android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION;

public class PuzzleDisplayer {

    private final Context context;
    private final GroupCountDisplayer groupCountDisplayer;
    private GroupCount rowCount;
    private GroupCount columnCount;

    /**
     * listener for each count view
     */
    private final View.OnClickListener onCountClick = new View.OnClickListener() {
        /**
         * Parses the clicked views content description.
         * Toggles the background of the view.
         *
         * @param v clicked view
         */
        @Override
        public void onClick(View v) {
            // parse the tag
            String tag = (String) v.getContentDescription();
            String[] tagParsed = tag.split(context.getString(R.string.string_divider));
            String rowOrColumn = tagParsed[0];
            int outerIndex = Integer.parseInt(tagParsed[1]);
            int innerIndex = Integer.parseInt(tagParsed[2]);

            ArrayList<View> views = new ArrayList<>();
            ((AppCompatActivity) context).findViewById(R.id.activity_game_root).findViewsWithText(views, tag, FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

            // toggle the counts background
            if (rowOrColumn.equals(context.getString(R.string.row))) {
                for (View view : views) {
                    groupCountDisplayer.toggleCount(rowCount, view, outerIndex, innerIndex);
                }
                rowCount.toggleCrossedOut(outerIndex, innerIndex);
            } else if (rowOrColumn.equals(context.getString(R.string.column))) {
                for (View view : views) {
                    groupCountDisplayer.toggleCount(columnCount, view, outerIndex, innerIndex);
                }
                columnCount.toggleCrossedOut(outerIndex, innerIndex);
            }
        }
    };

    public PuzzleDisplayer(Context pContext) {
        context = pContext;
        groupCountDisplayer = new GroupCountDisplayer(context);
    }

    public TableLayout getGameView(int[][] pCurrentUserField, GroupCount pRowCount, GroupCount pColumnCount, int pCellSize, View.OnClickListener onFieldClick) {
        rowCount = pRowCount;
        columnCount = pColumnCount;

        final GameFieldDisplayer fieldDisplayer = new GameFieldDisplayer(context);
        TableLayout table = fieldDisplayer.getPuzzleView(pCurrentUserField, pCellSize, onFieldClick);

        for (int i = 0; i < pCurrentUserField.length; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            // add row count at the left
            LinearLayout rowCount = groupCountDisplayer.getRowCountView(pRowCount, i, onCountClick);
            rowCount.setGravity(Gravity.END);
            row.addView(rowCount, 0);
            // add row count at the right
            rowCount = groupCountDisplayer.getRowCountView(pRowCount, i, onCountClick);
            rowCount.setGravity(Gravity.START);
            row.addView(rowCount);
        }

        // add column count at the top
        TableRow columnCount = groupCountDisplayer.getColumnCountRow(pColumnCount, onCountClick);
        columnCount.setGravity(Gravity.BOTTOM);
        table.addView(columnCount, 0);
        // add column count at the bottom
        columnCount = groupCountDisplayer.getColumnCountRow(pColumnCount, onCountClick);
        columnCount.setGravity(Gravity.TOP);
        table.addView(columnCount);

        return table;
    }

}
