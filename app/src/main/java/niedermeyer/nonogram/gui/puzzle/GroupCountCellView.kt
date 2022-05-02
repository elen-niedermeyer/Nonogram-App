package niedermeyer.nonogram.gui.puzzle

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import niedermeyer.nonogram.R
import niedermeyer.nonogram.logics.GroupCountCell
import java.util.*

class GroupCountCellView(
    context: Context,
    countValue: GroupCountCell,
    cellSize: Int,
    description: String,
    onClick: OnClickListener,
) : AppCompatTextView(context) {

    private val textSizeFactor = 0.7f

    init {
        // make a new text view
        this.text = String.format(Locale.getDefault(), "%d", countValue.value)
        this.width = cellSize
        this.height = cellSize
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            cellSize * textSizeFactor
        )
        this.contentDescription = description
        this.isClickable = true
        setOnClickListener(onClick)
        this.gravity = Gravity.CENTER_HORIZONTAL

        setCustomTextColor(countValue.isCrossedOut)
    }

    /**
     * Toggles the text color of the given view. It would be gray, if it wasn't before and the other way around.
     *
     * @param isCrossedOut
     */
    fun setCustomTextColor(isCrossedOut: Boolean) {
        // toggle the text color, default or gray
        val colorId = if (isCrossedOut) R.color.gray else R.color.secondary

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(context.getColor(colorId))
        } else {
            setTextColor(context.resources.getColor(colorId))
        }
    }

}