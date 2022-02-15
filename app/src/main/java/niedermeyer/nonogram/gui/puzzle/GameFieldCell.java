package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.logics.NonogramConstants;

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
public class GameFieldCell extends View {

    public GameFieldCell(Context context) {
        super(context);
    }

    /**
     * Creates a cell in the game field.
     */
    public GameFieldCell(Context context, int viewId, int cellSize, int value, OnClickListener onClick) {
        super(context);
        this.setId(viewId);
        this.setLayoutParams(new TableRow.LayoutParams(cellSize, cellSize));
        this.setBackgroundResource(getFieldBackgroundResource(value));
        this.setOnClickListener(onClick);
    }

    public void updateBackground(int newValue) {
        this.setBackgroundResource(getFieldBackgroundResource(newValue));
    }

    private int getFieldBackgroundResource(int pValue) {
        if (pValue == NonogramConstants.FIELD_FILLED) {
            return R.drawable.game_cell_black;
        } else if (pValue == NonogramConstants.FIELD_EMPTY) {
            return R.drawable.game_cell_cross;
        } else {
            return R.drawable.game_cell_white;
        }
    }

}
