package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.TypedValue
import niedermeyer.nonogram.persistence.GameOptionsPersistence
import android.view.Gravity
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.GroupCountCell
import java.util.*

class GroupCountCellView(
    context: Context,
    countValue: GroupCountCell,
    description: String,
    onClick: OnClickListener,
    paddingLeftRight: Int? = null,
) : AppCompatTextView(context) {

    private val textSizeFactor = 0.7f

    init {
        // make a new text view
        this.text = String.format(Locale.getDefault(), "%d", countValue.value)
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            GameOptionsPersistence(context).cellSize * textSizeFactor
        )
        this.contentDescription = description
        this.isClickable = true
        setOnClickListener(onClick)
        this.gravity = Gravity.CENTER_HORIZONTAL
        if (paddingLeftRight != null) setPadding(paddingLeftRight, 0, paddingLeftRight, 0)

        // load the crossed out background if necessary
        if (countValue.isCrossedOut) {
            setBackgroundResource(R.drawable.puzzle_count_crossed_out)
        }
    }

    /**
     * Toggles the background of the given view. It would be stroked, if it wasn't before and the other way around.
     *
     * @param isCrossedOut
     */
    fun toggleBackground(isCrossedOut: Boolean) {
        // toggle the background, stroke or not
        if (!isCrossedOut) {
            // set the background to nothing if it is stroke
            setBackgroundResource(0)
        } else {
            // set the strike resource
            setBackgroundResource(R.drawable.puzzle_count_crossed_out)
        }
    }

}