package git.shin.gesturesplus.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.data.GestureTrigger
import git.shin.gesturesplus.data.PreferenceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestureViewModel(application: Application) : AndroidViewModel(application) {
    private val preferenceManager = PreferenceManager(application)

    val gestures: StateFlow<Map<GestureTrigger, GestureAction>> = preferenceManager.allGesturesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = GestureTrigger.entries.associateWith { GestureAction.NONE }
        )

    val themeMode: StateFlow<Int> = preferenceManager.themeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    fun setThemeMode(mode: Int) {
        viewModelScope.launch {
            preferenceManager.setThemeMode(mode)
        }
    }

    fun setGestureAction(trigger: GestureTrigger, action: GestureAction) {
        viewModelScope.launch {
            preferenceManager.setGestureAction(trigger, action)
        }
    }
}
