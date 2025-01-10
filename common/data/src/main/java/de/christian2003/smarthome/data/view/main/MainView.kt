package de.christian2003.smarthome.data.view.main

import androidx.compose.runtime.Composable

@Composable
fun MainView(
    viewModel: MainViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToRoom: (String) -> Unit
) {

}
