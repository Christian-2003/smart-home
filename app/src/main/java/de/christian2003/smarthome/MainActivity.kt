package de.christian2003.smarthome

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.ui.theme.SmartHomeTheme
import de.christian2003.smarthome.data.view.cert.CertView
import de.christian2003.smarthome.data.view.cert.CertViewModel
import de.christian2003.smarthome.data.view.main.MainView
import de.christian2003.smarthome.data.view.main.MainViewModel
import de.christian2003.smarthome.data.view.room.RoomView
import de.christian2003.smarthome.data.view.room.RoomViewModel
import de.christian2003.smarthome.data.view.settings.SettingsView
import de.christian2003.smarthome.data.view.settings.SettingsViewModel
import de.christian2003.smarthome.data.view.url.UrlView
import de.christian2003.smarthome.data.view.url.UrlViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartHome()
        }
    }

}


@Composable
fun SmartHome() {
    val navController = rememberNavController()

    val preferences = LocalContext.current.getSharedPreferences("smart_home", Context.MODE_PRIVATE)
    val serverUrl: String? = preferences.getString("server_url", null)
    var hasServerUrl: Boolean = !serverUrl.isNullOrEmpty()
    var hasCert: Boolean = preferences.getString("cert_alias", null) != null

    val startDestination: String = if(!hasServerUrl) {
        "settings/url"
    } else if (!hasCert) {
        "settings/cert"
    } else {
        "main"
    }

    val mainViewModel: MainViewModel = viewModel()

    val settingsViewModel: SettingsViewModel = viewModel()
    settingsViewModel.init()

    val urlViewModel: UrlViewModel = viewModel()
    val certViewModel: CertViewModel = viewModel()

    SmartHomeTheme {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("main") {
                mainViewModel.init(SmartHomeRepository.getInstance(LocalContext.current))
                MainView(
                    viewModel = mainViewModel,
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    },
                    onNavigateToRoom = { position ->
                        navController.navigate("room/$position")
                    }
                )
            }
            composable("room/{position}") { backStackEntry ->
                val position: Int? = try {
                    backStackEntry.arguments?.getString("position")!!.toInt()
                } catch (e: Exception) {
                    null
                }
                if (position != null) {
                    val roomViewModel: RoomViewModel = viewModel()
                    roomViewModel.init(SmartHomeRepository.getInstance(LocalContext.current), position)

                    RoomView(
                        viewModel = roomViewModel,
                        onNavigateUp = {
                            navController.navigateUp()
                        }
                    )
                }
            }
            composable("settings") {
                SettingsView(
                    viewModel = settingsViewModel,
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToUrl = {
                        navController.navigate("settings/url")
                    },
                    onNavigateToCert = {
                        navController.navigate("settings/cert")
                    }
                )
            }
            composable("settings/url") {
                urlViewModel.init()
                UrlView(
                    viewModel = urlViewModel,
                    isFirstOnStack = !hasServerUrl,
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToNext = {
                        hasServerUrl = true
                        if (!hasCert) {
                            navController.navigate("settings/cert")
                        }
                        else {
                            navController.navigate("main")
                        }
                    }
                )
            }
            composable("settings/cert") {
                certViewModel.init()
                CertView(
                    viewModel = certViewModel,
                    isFirstOnStack = !hasCert && hasServerUrl,
                    isConfiguration = !hasCert,
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToNext = {
                        hasCert = true
                        navController.navigate("main")
                    }
                )
            }
        }
    }
}
