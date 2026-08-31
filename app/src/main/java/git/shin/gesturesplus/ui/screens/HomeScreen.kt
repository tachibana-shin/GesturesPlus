package git.shin.gesturesplus.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import git.shin.gesturesplus.R
import git.shin.gesturesplus.utils.ServiceUtils

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(ServiceUtils.isAccessibilityServiceEnabled(context)) }
    var isBatteryIgnored by remember {
        mutableStateOf(
            ServiceUtils.isBatteryOptimizationIgnored(
                context
            )
        )
    }

    // Kiểm tra lại trạng thái khi quay lại app
    LaunchedEffect(Unit) {
        while (true) {
            isEnabled = ServiceUtils.isAccessibilityServiceEnabled(context)
            isBatteryIgnored = ServiceUtils.isBatteryOptimizationIgnored(context)
            kotlinx.coroutines.delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        // Card Accessibility Service
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isEnabled) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isEnabled) stringResource(R.string.accessibility_active) else stringResource(
                        R.string.accessibility_inactive
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEnabled)
                        stringResource(R.string.accessibility_active_desc)
                    else stringResource(R.string.accessibility_inactive_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (isEnabled) stringResource(R.string.btn_open_settings) else stringResource(
                            R.string.btn_enable_now
                        )
                    )
                }
            }
        }

        // Card Battery Optimization
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isBatteryIgnored) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBatteryIgnored) stringResource(R.string.battery_optimized) else stringResource(
                        R.string.battery_unoptimized
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isBatteryIgnored)
                        stringResource(R.string.battery_optimized_desc)
                    else stringResource(R.string.battery_unoptimized_desc),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent =
                            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            // Fallback to settings if direct request fails
                            context.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBatteryIgnored
                ) {
                    Text(
                        if (isBatteryIgnored) stringResource(R.string.btn_already_ignored) else stringResource(
                            R.string.btn_ignore_optimizations
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.instructions_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${stringResource(R.string.instruction_1)}\n${stringResource(R.string.instruction_2)}\n${
                    stringResource(
                        R.string.instruction_3
                    )
                }",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
