package git.shin.gesturesplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import git.shin.gesturesplus.ui.GestureViewModel
import git.shin.gesturesplus.ui.screens.ExclusionListScreen
import git.shin.gesturesplus.ui.screens.GestureMappingScreen
import git.shin.gesturesplus.ui.screens.HomeScreen
import git.shin.gesturesplus.ui.theme.GesturesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GesturesTheme {
                GesturesApp()
            }
        }
    }
}

@Composable
fun GesturesApp() {
    val gestureViewModel: GestureViewModel = viewModel()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = stringResource(it.labelRes)
                        )
                    },
                    label = { Text(stringResource(it.labelRes)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> HomeScreen()
            AppDestinations.GESTURES -> GestureMappingScreen(viewModel = gestureViewModel)
            AppDestinations.EXCLUSION -> ExclusionListScreen()
        }
    }
}

enum class AppDestinations(
    val labelRes: Int,
    val icon: Int,
) {
    HOME(R.string.ic_home_label, android.R.drawable.ic_menu_info_details),
    GESTURES(R.string.gesture_mapping_title, android.R.drawable.ic_menu_manage),
    EXCLUSION(R.string.exclusion_list_title, android.R.drawable.ic_menu_close_clear_cancel),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GesturesTheme {
        Greeting("Android")
    }
}