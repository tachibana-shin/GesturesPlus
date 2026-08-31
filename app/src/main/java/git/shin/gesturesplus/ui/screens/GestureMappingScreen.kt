package git.shin.gesturesplus.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import git.shin.gesturesplus.R
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.data.GestureTrigger
import git.shin.gesturesplus.ui.GestureViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestureMappingScreen(
    viewModel: GestureViewModel = viewModel()
) {
    val gestures by viewModel.gestures.collectAsStateWithLifecycle()

    val triggers = GestureTrigger.entries
    var selectedTrigger by remember { mutableStateOf<GestureTrigger?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.gesture_mapping_title)) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(triggers) { trigger ->
                val currentAction = gestures[trigger] ?: GestureAction.NONE

                ListItem(
                    headlineContent = { Text(stringResource(trigger.labelRes)) },
                    supportingContent = {
                        Text(
                            stringResource(
                                R.string.label_action,
                                stringResource(currentAction.labelRes)
                            )
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = getIconForAction(currentAction),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .clickable { selectedTrigger = trigger }
                        .fillMaxWidth(),
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }

    if (selectedTrigger != null) {
        AlertDialog(
            onDismissRequest = { selectedTrigger = null },
            title = {
                Text(
                    stringResource(
                        R.string.select_action_title,
                        stringResource(selectedTrigger!!.labelRes)
                    )
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(GestureAction.entries) { action ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(action.labelRes),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = getIconForAction(action),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.clickable {
                                viewModel.setGestureAction(selectedTrigger!!, action)
                                selectedTrigger = null
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedTrigger = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

fun getIconForAction(action: GestureAction): ImageVector {
    return when (action) {
        GestureAction.NONE -> Icons.Default.DoNotDisturb
        GestureAction.BACK -> Icons.AutoMirrored.Filled.ArrowBack
        GestureAction.HOME -> Icons.Default.Home
        GestureAction.RECENTS -> Icons.Default.Layers
        GestureAction.SCREENSHOT -> Icons.Default.Screenshot
        GestureAction.LOCK_SCREEN -> Icons.Default.Lock
        GestureAction.NOTIFICATIONS -> Icons.Default.Notifications
        GestureAction.QUICK_SETTINGS -> Icons.Default.Settings
        GestureAction.POWER_MENU -> Icons.Default.PowerSettingsNew
        GestureAction.SPLIT_SCREEN -> Icons.Default.VerticalSplit
        GestureAction.FLASHLIGHT -> Icons.Default.FlashlightOn
        GestureAction.DND -> Icons.Default.DoNotDisturbOn
        GestureAction.VOLUME_UP -> Icons.AutoMirrored.Filled.VolumeUp
        GestureAction.VOLUME_DOWN -> Icons.AutoMirrored.Filled.VolumeDown
        GestureAction.MEDIA_PLAY_PAUSE -> Icons.Default.PlayArrow
        GestureAction.MEDIA_NEXT -> Icons.Default.SkipNext
        GestureAction.MEDIA_PREVIOUS -> Icons.Default.SkipPrevious
        GestureAction.SEARCH -> Icons.Default.Search
        GestureAction.LAUNCH_CAMERA -> Icons.Default.CameraAlt
    }
}
