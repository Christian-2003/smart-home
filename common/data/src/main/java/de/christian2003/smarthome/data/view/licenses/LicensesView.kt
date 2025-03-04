package de.christian2003.smarthome.data.view.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import de.christian2003.smarthome.data.R
import de.christian2003.smarthome.data.model.licenses.License


/**
 * View displays a list of all licenses used by the app.
 *
 * @param viewModel     View model for the view.
 * @param navigateUp    Callback invoked to navigate up on the navigation stack.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesView(
    viewModel: LicensesViewModel,
    navigateUp: () -> Unit
) {
    viewModel.loadLicenses()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.licenses_title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            }
            else {
                LicensesList(
                    licenses = viewModel.licenses,
                    onLicenseClicked = { license ->
                        viewModel.loadLicenseText(license)
                    }
                )
            }

            if (viewModel.displayedLicenseName != null && viewModel.displayedLicenseText != null) {
                LicenseDialog(
                    licenseName = viewModel.displayedLicenseName!!,
                    licenseText = viewModel.displayedLicenseText!!,
                    onDismiss = {
                        viewModel.displayedLicenseName = null
                        viewModel.displayedLicenseText = null
                    }
                )
            }
        }
    }
}


/**
 * Composable displays a list of all licenses used.
 *
 * @param licenses          List of licenses to display.
 * @param onLicenseClicked  Callback invoked once a license is clicked.
 */
@Composable
fun LicensesList(
    licenses: List<License>,
    onLicenseClicked: (License) -> Unit
) {
    LazyColumn {
        items(licenses) { license ->
            LicensesListRow(
                license = license,
                onLicenseClicked = onLicenseClicked
            )
        }
    }
}


/**
 * Composable displays a single license that is used by the app.
 *
 * @param license           License to display.
 * @param onLicenseClicked  Callback invoked once the license is clicked.
 */
@Composable
fun LicensesListRow(
    license: License,
    onLicenseClicked: (License) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLicenseClicked(license)
            }
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical_between),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = license.softwareName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = license.licenseName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = license.softwareVersion,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.space_horizontal_between))
        )
    }
}


/**
 * Composable displays a dialog through which a license can be displayed.
 *
 * @param licenseName       Name of the license displayed.
 * @param licenseText       Text of the license to display.
 * @param onDismiss         Callback to close the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDialog(
    licenseName: String,
    licenseText: String,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.space_horizontal))
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.space_horizontal_between)),
                    painter = painterResource(R.drawable.ic_license),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = ""
                )
                Text(
                    text = licenseName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.space_vertical)),
                text = licenseText,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
