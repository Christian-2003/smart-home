package de.christian2003.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.christian2003.smarthome.data.ui.theme.SmartHomeTheme
import de.christian2003.smarthome.data.view.cert.CertView
import de.christian2003.smarthome.data.view.cert.CertViewModel
import de.christian2003.smarthome.data.view.main.MainView
import de.christian2003.smarthome.data.view.main.MainViewModel
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

    val mainViewModel: MainViewModel = viewModel()
    mainViewModel.init(mutableListOf()) //TODO: Load data

    val settingsViewModel: SettingsViewModel = viewModel()
    settingsViewModel.init()

    SmartHomeTheme {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainView(
                    viewModel = mainViewModel,
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    },
                    onNavigateToRoom = {

                    }
                )
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
                val urlViewModel: UrlViewModel = viewModel()
                urlViewModel.init()
                UrlView(
                    viewModel = urlViewModel,
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
            composable("settings/cert") {
                val certViewModel: CertViewModel = viewModel()
                certViewModel.init()
                CertView(
                    viewModel = certViewModel,
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
