package de.christian2003.smarthome.data.view.cert

import android.content.Context
import android.content.ContextWrapper
import android.security.KeyChain
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import de.christian2003.smarthome.data.R
import de.christian2003.smarthome.data.ui.utils.SmartHomeInfoCard


/**
 * Function extends the Context class to get the base activity of the app.
 */
fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


/**
 * Composable displays the view through which to edit the client certificate
 *
 * @param viewModel     View model for the view.
 * @param onNavigateUp  Callback to invoke to navigate up on the navigation stack.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertView(
    viewModel: CertViewModel,
    isFirstOnStack: Boolean,
    isConfiguration: Boolean,
    onNavigateUp: () -> Unit,
    onNavigateToNext: () -> Unit
) {
    val activity = LocalContext.current.getActivity()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cert_title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    if (!isFirstOnStack) {
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SmartHomeInfoCard(
                message = stringResource(R.string.cert_info)
            )
            if (viewModel.isCertSelected != null && viewModel.isCertSelected!!) {
                Log.d("Cert", "InputSection")
                InputSection(
                    onRemoveCertClicked = {
                        viewModel.removeCert()
                    },
                    onSelectCertClicked = {
                        KeyChain.choosePrivateKeyAlias(
                            activity!!,
                            { alias ->
                                if (alias != null) {
                                    viewModel.importCert(alias)
                                }
                            },
                            null,
                            null,
                            null,
                            -1,
                            null
                        )
                    }
                )
            }
            else if (viewModel.isCertSelected != null) {
                Log.d("Cert", "InputSectionError")
                InputSectionError(
                    onSelectCertClicked = {
                        KeyChain.choosePrivateKeyAlias(
                            activity!!,
                            { alias ->
                                if (alias != null) {
                                    viewModel.importCert(alias)
                                }
                            },
                            null,
                            null,
                            null,
                            -1,
                            null
                        )
                    }
                )
            }
            else {
                Log.d("Cert", "No input section")
            }
            if (isConfiguration) {
                Button(
                    onClick = {
                        onNavigateToNext()
                    },
                    enabled = viewModel.isCertSelected != null && viewModel.isCertSelected!!
                ) {
                    Text(stringResource(R.string.button_next))
                }
            }
        }
    }
}


/**
 * Composable displays the input section of the page that is visible when no valid certificate is
 * available.
 *
 * @param onSelectCertClicked   Callback to invoke to import a certificate.
 */
@Composable
fun InputSectionError(
    onSelectCertClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corners_default)))
            .border(
                width = dimensionResource(R.dimen.borders_default),
                color = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(dimensionResource(R.dimen.corners_default))
            )
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = dimensionResource(R.dimen.space_horizontal_between)),
                painter = painterResource(R.drawable.ic_error),
                tint = MaterialTheme.colorScheme.onErrorContainer,
                contentDescription = ""
            )
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.cert_error_invalid),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = dimensionResource(R.dimen.space_vertical_between)),
            onClick = onSelectCertClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onErrorContainer,
                contentColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(stringResource(R.string.button_select_cert))
        }
    }
}


/**
 * Composable displays the input section of the page that is visible when a valid certificate is
 * available.
 *
 * @param onRemoveCertClicked   Callback to invoke once the user clicks the button to remove the
 *                              certificate currently imported.
 * @param onSelectCertClicked   Callback to invoke to import a different certificate.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputSection(
    onRemoveCertClicked: () -> Unit,
    onSelectCertClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corners_default)))
            .border(
                width = dimensionResource(R.dimen.borders_default),
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(dimensionResource(R.dimen.corners_default))
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
    ) {
        Text(
            text = stringResource(R.string.cert_selected),
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = dimensionResource(R.dimen.space_vertical_between)),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onRemoveCertClicked
            ) {
                Text(stringResource(R.string.button_remove_cert))
            }
            Button(
                modifier = Modifier.padding(start = dimensionResource(R.dimen.space_horizontal_between)),
                onClick = onSelectCertClicked
            ) {
                Text(stringResource(R.string.button_select_other_cert))
            }
        }
    }
}
