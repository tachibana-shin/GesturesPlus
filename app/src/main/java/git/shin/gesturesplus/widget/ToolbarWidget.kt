package git.shin.gesturesplus.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.service.GestureAccessibilityService

class ToolbarWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ToolbarContent()
        }
    }

    @Composable
    private fun ToolbarContent() {
        Row(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToolbarButton(android.R.drawable.ic_lock_lock, GestureAction.LOCK_SCREEN)
            Spacer(modifier = GlanceModifier.width(16.dp))
            ToolbarButton(android.R.drawable.ic_menu_camera, GestureAction.SCREENSHOT)
            Spacer(modifier = GlanceModifier.width(16.dp))
            ToolbarButton(android.R.drawable.ic_menu_info_details, GestureAction.NOTIFICATIONS)
        }
    }

    @Composable
    private fun ToolbarButton(iconRes: Int, action: GestureAction) {
        Image(
            provider = ImageProvider(iconRes),
            contentDescription = stringResource(action.labelRes),
            modifier = GlanceModifier
                .size(40.dp)
                .clickable(
                    actionRunCallback<ToolbarActionCallback>(
                        actionParametersOf(ActionKey to action.name)
                    )
                )
        )
    }

    companion object {
        val ActionKey = ActionParameters.Key<String>("action")
    }
}

class ToolbarActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val actionName = parameters[ToolbarWidget.ActionKey]
        if (actionName != null) {
            val action = GestureAction.valueOf(actionName)
            GestureAccessibilityService.instance?.performAction(action)
        }
    }
}

class ToolbarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ToolbarWidget()
}
