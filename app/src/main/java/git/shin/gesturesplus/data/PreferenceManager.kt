package git.shin.gesturesplus.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gestures_prefs")

class PreferenceManager(private val context: Context) {

    companion object {
        private val EXCLUSION_LIST = stringSetPreferencesKey("exclusion_list")
        
        fun getGestureKey(trigger: GestureTrigger) = stringPreferencesKey("gesture_${trigger.name}")
    }

    val exclusionListFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[EXCLUSION_LIST] ?: emptySet()
        }

    val allGesturesFlow: Flow<Map<GestureTrigger, GestureAction>> = context.dataStore.data
        .map { preferences ->
            GestureTrigger.entries.associateWith { trigger ->
                val actionName = preferences[getGestureKey(trigger)] ?: GestureAction.NONE.name
                try {
                    GestureAction.valueOf(actionName)
                } catch (e: Exception) {
                    GestureAction.NONE
                }
            }
        }

    fun getActionFlow(trigger: GestureTrigger): Flow<GestureAction> = context.dataStore.data
        .map { preferences ->
            val actionName = preferences[getGestureKey(trigger)] ?: GestureAction.NONE.name
            try {
                GestureAction.valueOf(actionName)
            } catch (e: Exception) {
                GestureAction.NONE
            }
        }

    suspend fun setGestureAction(trigger: GestureTrigger, action: GestureAction) {
        context.dataStore.edit { preferences ->
            preferences[getGestureKey(trigger)] = action.name
        }
    }

    suspend fun addToExclusionList(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[EXCLUSION_LIST] ?: emptySet()
            preferences[EXCLUSION_LIST] = current + packageName
        }
    }

    suspend fun removeFromExclusionList(packageName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[EXCLUSION_LIST] ?: emptySet()
            preferences[EXCLUSION_LIST] = current - packageName
        }
    }
}
