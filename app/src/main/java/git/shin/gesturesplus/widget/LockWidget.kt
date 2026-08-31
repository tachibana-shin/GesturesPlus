package git.shin.gesturesplus.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import git.shin.gesturesplus.data.GestureAction
import git.shin.gesturesplus.service.GestureAccessibilityService

class LockWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceContent()
        }
    }

    @Composable
    private fun GlanceContent() {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(actionRunCallback<LockActionCallback>()),
            contentAlignment = Alignment.Center
        ) {
            // Invisible/Transparent Widget
            Spacer(modifier = GlanceModifier.fillMaxSize())
        }
    }
}

class LockActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        GestureAccessibilityService.instance?.performAction(GestureAction.LOCK_SCREEN)
    }
}

class LockWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidget()
}
