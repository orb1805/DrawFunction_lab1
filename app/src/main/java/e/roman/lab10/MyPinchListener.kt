package e.roman.lab10

import android.util.Log
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView

class MyPinchListener(drawer: Drawer) : SimpleOnScaleGestureListener() {

    var span: Float = 10000f
    val drawer = drawer
    var size = 0f

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        //Log.d("TAG", "PINCH! OUCH!")
        if (detector.currentSpan > detector.previousSpan) {
            size = (detector.currentSpan + span) / 20000
            if (size > 0.5f && size < 4f) {
                drawer.draw(size)
                span += detector.currentSpan
            }
        } else {
            size = (span - detector.currentSpan) / 20000
            if (size > 0.5f && size < 4f) {
                drawer.draw(size)
                span -= detector.currentSpan
            }
        }
        return true
    }
}