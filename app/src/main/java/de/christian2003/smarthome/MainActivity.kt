package de.christian2003.smarthome

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
        //Nachricht an Björn: Habe den Code in die SmartHomeRepository-Klasse bewegt. Hier gehört
        //der nämlich nicht hin. Lösche den Kommentar bitte sobald du die Nachricht gelesen hast!
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
    mainViewModel.init(SmartHomeRepository.getInstance(LocalContext.current))

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
