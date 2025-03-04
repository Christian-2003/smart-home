package de.christian2003.smarthome.data.view.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import de.christian2003.smarthome.data.R
import java.time.LocalDateTime


/**
 * Composable displays a view through which the user can edit the app settings.
 *
 * @param viewModel             View model for the view.
 * @param onNavigateUp          Callback to invoke in order to navigate up on the navigation stack.
 * @param onNavigateToUrl       Callback to invoke in order to navigate to the view through which to
 *                              edit the server URL.
 * @param onNavigateToCert      Callback to invoke in order to navigate to the view through which to
 *                              edit the client certificate.
 * @param onNavigateToLicenses  Callback to invoke in order to navigate to the view through which to
 *                              display all open source software used by the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToUrl: () -> Unit,
    onNavigateToCert: () -> Unit,
    onNavigateToLicenses: () -> Unit
) {
    val context = LocalContext.current
    val toastTheme = stringResource(R.string.settings_customization_theme_toast)

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
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            //Connection:
            SettingsTitle(title = stringResource(R.string.settings_connection))
            SettingsItemButton(
                setting = stringResource(R.string.settings_connection_url),
                info = stringResource(R.string.settings_connection_url_info),
                onClick = onNavigateToUrl,
                prefixIcon = painterResource(R.drawable.ic_link),
                suffixIcon = painterResource(R.drawable.ic_next)
            )
            SettingsItemButton(
                setting = stringResource(R.string.settings_connection_cert),
                info = stringResource(R.string.settings_connection_cert_info),
                onClick = onNavigateToCert,
                prefixIcon = painterResource(R.drawable.ic_cert),
                suffixIcon = painterResource(R.drawable.ic_next)
            )
            SettingsItemSwitch(
                setting = stringResource(R.string.settings_connection_ssl),
                info = stringResource(R.string.settings_connection_ssl_info),
                checked = viewModel.allowUnsafeSsl,
                onCheckedChanged = { checked ->
                    viewModel.updateAllowUnsafeSsl(checked)
                },
                prefixIcon = painterResource(R.drawable.ic_ssl)
            )

            HorizontalDivider()

            //Customization:
            SettingsTitle(title = stringResource(R.string.settings_customization))
            SettingsItemSwitch(
                setting = stringResource(R.string.settings_customization_theme),
                info = stringResource(R.string.settings_customization_theme_info),
                checked = viewModel.useDynamicTheme,
                onCheckedChanged = { checked ->
                    viewModel.updateUseDynamicTheme(checked)
                    Toast.makeText(context, toastTheme, Toast.LENGTH_SHORT).show()
                },
                prefixIcon = painterResource(R.drawable.ic_theme)
            )
            SettingsItemSwitch(
                setting = stringResource(R.string.settings_customization_warnings),
                info = stringResource(R.string.settings_customization_warnings_info),
                checked = viewModel.showWarnings,
                onCheckedChanged = { checked ->
                    viewModel.updateShowWarnings(checked)
                },
                prefixIcon = painterResource(R.drawable.ic_warning)
            )
            SettingsItemSwitch(
                setting = stringResource(R.string.settings_customization_errors),
                info = stringResource(R.string.settings_customization_errors_info),
                checked = viewModel.showErrors,
                onCheckedChanged = { checked ->
                    viewModel.updateShowErrors(checked)
                },
                prefixIcon = painterResource(R.drawable.ic_error)
            )

            HorizontalDivider()

            //About
            SettingsTitle(title = stringResource(R.string.settings_about))
            SettingsItemButton(
                setting = stringResource(R.string.settings_about_licenses),
                info = stringResource(R.string.settings_about_licenses_info),
                onClick = {
                    onNavigateToLicenses()
                },
                prefixIcon = painterResource(R.drawable.ic_license),
                suffixIcon = painterResource(R.drawable.ic_next)
            )
            SettingsItemButton(
                setting = stringResource(R.string.settings_about_github),
                info = stringResource(R.string.settings_about_github_info),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Christian-2003/smart-home"))
                    context.startActivity(intent)
                },
                prefixIcon = painterResource(R.drawable.ic_github),
                suffixIcon = painterResource(R.drawable.ic_external)
            )
            SettingsItemButton(
                setting = stringResource(R.string.settings_about_system),
                info = stringResource(R.string.settings_about_system_info),
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.setData(uri)
                    context.startActivity(intent)
                },
                prefixIcon = painterResource(R.drawable.ic_android),
                suffixIcon = painterResource(R.drawable.ic_external)
            )

            VersionInfo()
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
            start = dimensionResource(R.dimen.space_horizontal) + dimensionResource(R.dimen.image_medium) + dimensionResource(R.dimen.space_horizontal_between),
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
 * @param setting       Title for the setting.
 * @param info          Info for the setting.
 * @param onClick       Callback to invoke when the item button is clicked.
 * @param prefixIcon    Optional icon displayed at the start of the item button.
 */
@Composable
fun SettingsItemButton(
    setting: String,
    info: String,
    onClick: () -> Unit,
    prefixIcon: Painter? = null,
    suffixIcon: Painter? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
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
        if (prefixIcon != null) {
            Icon(
                painter = prefixIcon,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.space_horizontal_between))
                    .size(dimensionResource(R.dimen.image_medium))
            )
        }
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = setting,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (suffixIcon != null) {
                    Icon(
                        painter = suffixIcon,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = dimensionResource(R.dimen.space_horizontal_between_small))
                            .size(dimensionResource(R.dimen.image_small))
                    )
                }
            }
            Text(
                text = info,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


/**
 * Composable displays an item button.
 *
 * @param setting           Title for the setting.
 * @param info              Info for the setting.
 * @param checked           Whether the switch is checked.
 * @param onCheckedChanged  Callback to invoke once the checked state changes.
 */
@Composable
fun SettingsItemSwitch(
    setting: String,
    info: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    prefixIcon: Painter? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChanged(!checked)
            }
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical_between),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
    ) {
        if (prefixIcon != null) {
            Icon(
                painter = prefixIcon,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.space_horizontal_between))
                    .size(dimensionResource(R.dimen.image_medium))
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = dimensionResource(R.dimen.space_horizontal_between))
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
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
    }
}


/**
 * Composable displays the version info about the app.
 */
@Composable
fun VersionInfo() {
    val context = LocalContext.current
    val versionName: String = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.space_horizontal),
                top = dimensionResource(R.dimen.space_vertical),
                end = dimensionResource(R.dimen.space_horizontal),
                bottom = dimensionResource(R.dimen.space_vertical)
            )
    ) {
        Text(
            text = stringResource(R.string.settings_info_version).replace("{arg}", versionName),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.settings_info_copyright).replace("{arg}", LocalDateTime.now().year.toString()),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Icon(
            painter = painterResource(R.drawable.ic_splash_branding),
            tint = Color.Unspecified,
            contentDescription = ""
        )
    }
}
