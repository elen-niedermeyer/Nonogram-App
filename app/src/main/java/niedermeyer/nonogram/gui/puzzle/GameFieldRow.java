package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameFieldRow extends TableRow {

    public GameFieldRow(Context context) {
        super(context);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(rowParams);
        this.setGravity(Gravity.CENTER);
    }

}
