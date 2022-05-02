package niedermeyer.nonogram.gui.activity

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.LinearLayout
import kotlin.math.max
import kotlin.math.min


class GameActivityView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    init {
        this.setWillNotDraw(false)
    }

    private var mScaleFactor = 1f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = max(1f, min(mScaleFactor, 5.0f))

            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev)
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