package niedermeyer.nonogram.gui.activity

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.LinearLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import kotlin.math.max
import kotlin.math.min


class GameActivityView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    var startTop = 0

    init {
        this.setWillNotDraw(false)
    }

    private var mScaleFactor = 1f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = max(1f, min(mScaleFactor, 5f))

            invalidate()
            return true
        }
    }
    private val scaleGestureDetector = ScaleGestureDetector(context, scaleListener)

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            var dx = distanceX.toInt()
            var dy = distanceY.toInt()
            val point = IntArray(2)
            getLocationOnScreen(point)

            val MIN_X = point[0] / mScaleFactor
            val MAX_X =
                (width * mScaleFactor - width - scrollX * mScaleFactor) / mScaleFactor // right point of view - display width - currentPosition
            if (scrollX + dx < MIN_X) {
                dx = (MIN_X - scrollX).toInt()
            } else if (scrollX + dx > MAX_X) {
                dx = (MAX_X - scrollX).toInt()
            }

            var insetTop = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetTop = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            }
            // TODO: Y-scrolling
            val MIN_Y = (point[1] - marginTop - insetTop) / mScaleFactor
            val MAX_Y =
                (getChildAt(0).height * mScaleFactor - height - scrollY * mScaleFactor) / mScaleFactor
            if (scrollY + dy < MIN_Y) {
                dy = (MIN_Y - scrollY).toInt()
            } else if (scrollY + dy > MAX_Y) {
                dy = (MAX_Y - scrollY).toInt()
            }
            scrollBy(dx, dy)
            return true
        }
    }
    private val gestureDetector = GestureDetectorCompat(context, mGestureListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onTouchEvent(ev!!)
        return false
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.scaleX = mScaleFactor
        this.scaleY = mScaleFactor

        canvas?.apply {
            save()
            // onDraw() code goes here
            restore()
        }
    }

}