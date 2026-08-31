package git.shin.gesturesplus.service

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.data.GestureTrigger
import git.shin.gesturesplus.data.PreferenceManager
import git.shin.gesturesplus.utils.HapticUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

class GestureDetectorView(
    context: Context,
    private val service: GestureAccessibilityService,
    private val preferenceManager: PreferenceManager
) : View(context) {

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private var startX = 0f
    private var startY = 0f
    private var isGestureDetected = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerCount = event.pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (pointerCount >= 2) {
                    startX = event.getX(0)
                    startY = event.getY(0)
                    isGestureDetected = false
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isGestureDetected && pointerCount >= 2) {
                    val deltaX = event.getX(0) - startX
                    val deltaY = event.getY(0) - startY

                    if (abs(deltaX) > touchSlop * 3 || abs(deltaY) > touchSlop * 3) {
                        detectGesture(pointerCount, deltaX, deltaY)
                        isGestureDetected = true
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                isGestureDetected = false
            }
        }

        // Trả về false để touch event có thể đi tiếp? 
        // Không, nếu view này ở trên thì nó sẽ nuốt chửng touch.
        // Đây là vấn đề lớn nhất nếu không dùng SYSTEM_ALERT_WINDOW hoặc overlay mỏng.
        return true
    }

    private fun detectGesture(pointerCount: Int, deltaX: Float, deltaY: Float) {
        val trigger = when (pointerCount) {
            2 -> {
                if (abs(deltaY) > abs(deltaX)) {
                    if (deltaY > 0) null // Swipe down 2 fingers?
                    else GestureTrigger.TWO_FINGER_BOTTOM_SWIPE // Swipe up 2 fingers (bottom edge)
                } else {
                    if (deltaX > 0) GestureTrigger.TWO_FINGER_EDGE_LEFT
                    else GestureTrigger.TWO_FINGER_EDGE_RIGHT
                }
            }

            3 -> {
                if (abs(deltaY) > abs(deltaX)) {
                    if (deltaY > 0) GestureTrigger.THREE_FINGER_SWIPE_DOWN
                    else GestureTrigger.THREE_FINGER_SWIPE_UP
                } else {
                    if (deltaX > 0) GestureTrigger.THREE_FINGER_SWIPE_RIGHT
                    else GestureTrigger.THREE_FINGER_SWIPE_LEFT
                }
            }

            4 -> GestureTrigger.FOUR_FINGER_SWIPE
            else -> null
        }

        trigger?.let { t ->
            scope.launch {
                val action = preferenceManager.getActionFlow(t).first()
                if (action != GestureAction.NONE) {
                    service.performAction(action)
                    HapticUtils.vibrate(context)
                }
            }
        }
    }
}
