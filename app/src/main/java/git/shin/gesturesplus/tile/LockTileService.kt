package git.shin.gesturesplus.tile

import android.service.quicksettings.TileService
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.service.GestureAccessibilityService

class LockTileService : TileService() {
    override fun onClick() {
        super.onClick()
        GestureAccessibilityService.instance?.performAction(GestureAction.LOCK_SCREEN)
    }
}
