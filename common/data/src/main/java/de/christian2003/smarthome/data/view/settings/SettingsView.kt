package de.christian2003.smarthome.data.view.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import de.christian2003.smarthome.data.R


/**
 * Composable displays a view through which the user can edit the app settings.
 *
 * @param viewModel         View model for the view.
 * @param onNavigateUp      Callback to invoke in order to navigate up on the navigation stack.
 * @param onNavigateToUrl   Callback to invoke in order to navigate to the view through which to
 *                          edit the server URL.
 * @param onNavigateToCert  Callback to invoke in order to navigate to the view through which to
 *                          edit the client certificate.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToUrl: () -> Unit,
    onNavigateToCert: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SettingsTitle(title = stringResource(R.string.settings_connection))
            SettingsItemButton(
                setting = stringResource(R.string.settings_connection_url),
                info = stringResource(R.string.settings_connection_url_info),
                onClick = onNavigateToUrl)
            SettingsItemButton(
                setting = stringResource(R.string.settings_connection_cert),
                info = stringResource(R.string.settings_connection_cert_info),
                onClick = onNavigateToCert)
        }
    }
}


/**
 * Composable displays a title for a group of related settings.
 *
 * @param title Title to display.
 */
@Composable
fun SettingsTitle(
    title: String
) {
    Text(
        modifier = Modifier.padding(
            start = dimensionResource(R.dimen.space_horizontal),
            top = dimensionResource(R.dimen.space_vertical),
            end = dimensionResource(R.dimen.space_horizontal),
            bottom = dimensionResource(R.dimen.space_vertical_between)),
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
}


/**
 * Composable displays an item button.
 *
 * @param setting   Title for the setting.
 * @param info      Info for the setting.
 * @param onClick   Callback to invoke when the item button is clicked.
 */
@Composable
fun SettingsItemButton(
    setting: String,
    info: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical_between),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
    ) {
        Text(
            text = setting,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = info,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
