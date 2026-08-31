package git.shin.gesturesplus.data

import git.shin.gesturesplus.R

enum class GestureTrigger(val labelRes: Int) {
    THREE_FINGER_SWIPE_DOWN(R.string.trigger_3f_down),
    THREE_FINGER_SWIPE_UP(R.string.trigger_3f_up),
    THREE_FINGER_SWIPE_LEFT(R.string.trigger_3f_left),
    THREE_FINGER_SWIPE_RIGHT(R.string.trigger_3f_right),
    TWO_FINGER_EDGE_LEFT(R.string.trigger_2f_left),
    TWO_FINGER_EDGE_RIGHT(R.string.trigger_2f_right),
    TWO_FINGER_BOTTOM_SWIPE(R.string.trigger_2f_bottom),
    THREE_FINGER_TAP(R.string.trigger_3f_tap),
    FOUR_FINGER_SWIPE(R.string.trigger_4f_swipe)
}

enum class GestureAction(val labelRes: Int) {
    NONE(R.string.action_none),
    BACK(R.string.action_back),
    HOME(R.string.action_home),
    RECENTS(R.string.action_recents),
    SCREENSHOT(R.string.action_screenshot),
    LOCK_SCREEN(R.string.action_lock),
    NOTIFICATIONS(R.string.action_notifications),
    QUICK_SETTINGS(R.string.action_quick_settings),
    POWER_MENU(R.string.action_power_menu),
    SPLIT_SCREEN(R.string.action_split_screen),
    FLASHLIGHT(R.string.action_flashlight),
    DND(R.string.action_dnd),
    VOLUME_UP(R.string.action_volume_up),
    VOLUME_DOWN(R.string.action_volume_down),
    MEDIA_PLAY_PAUSE(R.string.action_play_pause),
    MEDIA_NEXT(R.string.action_next),
    MEDIA_PREVIOUS(R.string.action_previous),
    SEARCH(R.string.action_search),
    LAUNCH_CAMERA(R.string.action_camera)
}
