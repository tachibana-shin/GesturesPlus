package git.shin.gesturesplus.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.data.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GestureAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var windowManager: WindowManager
    private var currentPackage: String? = null
    private var exclusionList = setOf<String>()
    
    private var leftOverlay: View? = null
    private var rightOverlay: View? = null
    private var bottomOverlay: View? = null
    private var topOverlay: View? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        preferenceManager = PreferenceManager(this)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        serviceScope.launch {
            preferenceManager.exclusionListFlow.collect {
                exclusionList = it
                updateOverlayState()
            }
        }
        
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            currentPackage = event.packageName?.toString()
            updateOverlayState()
        }
    }

    private fun updateOverlayState() {
        val isExcluded = currentPackage?.let { exclusionList.contains(it) } ?: false
        if (isExcluded) {
            removeOverlays()
        } else {
            addOverlays()
        }
    }

    private fun addOverlays() {
        if (leftOverlay == null) {
            leftOverlay = createOverlayView(Gravity.START or Gravity.CENTER_VERTICAL, 40, WindowManager.LayoutParams.MATCH_PARENT)
            rightOverlay = createOverlayView(Gravity.END or Gravity.CENTER_VERTICAL, 40, WindowManager.LayoutParams.MATCH_PARENT)
            bottomOverlay = createOverlayView(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, WindowManager.LayoutParams.MATCH_PARENT, 40)
            topOverlay = createOverlayView(Gravity.TOP or Gravity.CENTER_HORIZONTAL, WindowManager.LayoutParams.MATCH_PARENT, 40)
            
            windowManager.addView(leftOverlay, getLayoutParams(Gravity.START or Gravity.CENTER_VERTICAL, 40, WindowManager.LayoutParams.MATCH_PARENT))
            windowManager.addView(rightOverlay, getLayoutParams(Gravity.END or Gravity.CENTER_VERTICAL, 40, WindowManager.LayoutParams.MATCH_PARENT))
            windowManager.addView(bottomOverlay, getLayoutParams(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, WindowManager.LayoutParams.MATCH_PARENT, 40))
            windowManager.addView(topOverlay, getLayoutParams(Gravity.TOP or Gravity.CENTER_HORIZONTAL, WindowManager.LayoutParams.MATCH_PARENT, 40))
        }
    }

    private fun removeOverlays() {
        leftOverlay?.let { windowManager.removeView(it) }
        rightOverlay?.let { windowManager.removeView(it) }
        bottomOverlay?.let { windowManager.removeView(it) }
        topOverlay?.let { windowManager.removeView(it) }
        
        leftOverlay = null
        rightOverlay = null
        bottomOverlay = null
        topOverlay = null
    }

    private fun createOverlayView(gravity: Int, width: Int, height: Int): View {
        return GestureDetectorView(this, this, preferenceManager)
    }

    private fun getLayoutParams(gravity: Int, width: Int, height: Int): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        return WindowManager.LayoutParams(
            width, height,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or 
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.gravity = gravity
        }
    }

    override fun onInterrupt() {}

    fun performAction(action: GestureAction) {
        when (action) {
            GestureAction.BACK -> performGlobalAction(GLOBAL_ACTION_BACK)
            GestureAction.HOME -> performGlobalAction(GLOBAL_ACTION_HOME)
            GestureAction.RECENTS -> performGlobalAction(GLOBAL_ACTION_RECENTS)
            GestureAction.SCREENSHOT -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)
                }
            }
            GestureAction.LOCK_SCREEN -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
                }
            }
            GestureAction.NOTIFICATIONS -> performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
            GestureAction.QUICK_SETTINGS -> performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS)
            GestureAction.POWER_MENU -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    performGlobalAction(GLOBAL_ACTION_POWER_DIALOG)
                }
            }
            GestureAction.SPLIT_SCREEN -> performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)
            GestureAction.FLASHLIGHT -> toggleFlashlight()
            GestureAction.DND -> toggleDND()
            GestureAction.VOLUME_UP -> adjustVolume(true)
            GestureAction.VOLUME_DOWN -> adjustVolume(false)
            GestureAction.MEDIA_PLAY_PAUSE -> sendMediaKey(android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
            GestureAction.MEDIA_NEXT -> sendMediaKey(android.view.KeyEvent.KEYCODE_MEDIA_NEXT)
            GestureAction.MEDIA_PREVIOUS -> sendMediaKey(android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS)
            GestureAction.SEARCH -> {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_APP_BROWSER)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            GestureAction.LAUNCH_CAMERA -> {
                val intent = Intent(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            else -> {}
        }
    }

    private var isFlashlightOn = false

    private fun toggleFlashlight() {
        val cameraManager = getSystemService(CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            isFlashlightOn = !isFlashlightOn
            cameraManager.setTorchMode(cameraId, isFlashlightOn)
        } catch (e: Exception) {
            // Log or handle error
        }
    }

    private fun toggleDND() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        if (notificationManager.isNotificationPolicyAccessGranted) {
            val currentMode = notificationManager.currentInterruptionFilter
            val newMode = if (currentMode == android.app.NotificationManager.INTERRUPTION_FILTER_NONE) 
                android.app.NotificationManager.INTERRUPTION_FILTER_ALL 
                else android.app.NotificationManager.INTERRUPTION_FILTER_NONE
            notificationManager.setInterruptionFilter(newMode)
        } else {
            val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }

    private fun adjustVolume(up: Boolean) {
        val audioManager = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        audioManager.adjustStreamVolume(
            android.media.AudioManager.STREAM_MUSIC,
            if (up) android.media.AudioManager.ADJUST_RAISE else android.media.AudioManager.ADJUST_LOWER,
            android.media.AudioManager.FLAG_SHOW_UI
        )
    }

    private fun sendMediaKey(keyCode: Int) {
        val audioManager = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        val event = android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, keyCode)
        audioManager.dispatchMediaKeyEvent(event)
        val eventUp = android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, keyCode)
        audioManager.dispatchMediaKeyEvent(eventUp)
    }

    companion object {
        private var weakInstance: java.lang.ref.WeakReference<GestureAccessibilityService>? = null
        
        val instance: GestureAccessibilityService?
            get() = weakInstance?.get()
    }

    override fun onCreate() {
        super.onCreate()
        weakInstance = java.lang.ref.WeakReference(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOverlays()
        weakInstance = null
    }
}
