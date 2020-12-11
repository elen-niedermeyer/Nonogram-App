package niedermeyer.nonogram.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import androidx.core.widget.NestedScrollView;

public class ScaleView extends NestedScrollView {
    private final ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.f;

    public ScaleView(Context context) {
        super(context);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        scaleDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final ScaleView view = this;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                canvas.save();
                canvas.scale(scaleFactor, scaleFactor);
                canvas.restore();
            }
        });

    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();


            System.out.println("#### SCALED ####");

            // Don't let the object get too small or too large.
            //scaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

}
