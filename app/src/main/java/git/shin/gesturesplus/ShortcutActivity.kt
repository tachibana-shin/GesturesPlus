package git.shin.gesturesplus

import android.app.Activity
import android.os.Bundle
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.service.GestureAccessibilityService

class ShortcutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionName = intent.getStringExtra("action")
        if (actionName != null) {
            try {
                val action = GestureAction.valueOf(actionName)
                GestureAccessibilityService.instance?.performAction(action)
            } catch (e: Exception) {
                // Ignore
            }
        }

        finish()
    }
}
