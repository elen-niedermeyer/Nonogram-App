package niedermeyer.nonogram.gui.puzzle;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;

import java.util.Locale;

import niedermeyer.nonogram.R;
import niedermeyer.nonogram.persistence.GameOptionsPersistence;

/**
 * @author Elen Niedermeyer, last modified 2022-02-15
 */
public class GroupCountCell extends androidx.appcompat.widget.AppCompatTextView {

    private static final float TEXT_SIZE_FACTOR = 0.7f;

    public GroupCountCell(Context context) {
        super(context);
    }

    public GroupCountCell(Context context, niedermeyer.nonogram.logics.GroupCountCell countValue, String description, OnClickListener onClick) {
        super(context);
        // make a new text view
        this.setText(String.format(Locale.getDefault(), "%d", countValue.getValue()));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, new GameOptionsPersistence(context).getCellSize() * TEXT_SIZE_FACTOR);
        this.setContentDescription(description);
        this.setClickable(true);
        this.setOnClickListener(onClick);
        this.setGravity(Gravity.CENTER_HORIZONTAL);

        // load the crossed out background if necessary
        if (countValue.isCrossedOut()) {
            this.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
    }

    public GroupCountCell(Context context, niedermeyer.nonogram.logics.GroupCountCell countValue, String description, int paddingLeftRight, OnClickListener onClick) {
        super(context);
        // make a new text view
        this.setText(String.format(Locale.getDefault(), "%d", countValue.getValue()));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, new GameOptionsPersistence(context).getCellSize() * TEXT_SIZE_FACTOR);
        this.setContentDescription(description);
        this.setClickable(true);
        this.setOnClickListener(onClick);
        this.setGravity(Gravity.CENTER_HORIZONTAL);

        this.setPadding(paddingLeftRight, 0, paddingLeftRight, 0);

        // load the crossed out background if necessary
        if (countValue.isCrossedOut()) {
            this.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
    }

    /**
     * Toggles the background of the given view. It would be stroked, if it wasn't before and the other way around.
     *
     * @param isCrossedOut
     */
    public void toggleBackground(boolean isCrossedOut) {
        // toggle the background, stroke or not
        if (!isCrossedOut) {
            // set the background to nothing if it is stroke
            this.setBackgroundResource(0);
        } else {
            // set the strike resource
            this.setBackgroundResource(R.drawable.puzzle_count_crossed_out);
        }
    }

}
