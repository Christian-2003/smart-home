package de.christian2003.smarthome.data.view.cert

import android.app.Activity
import android.content.Intent
import android.graphics.Paint.Align
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import de.christian2003.smarthome.data.R
import de.christian2003.smarthome.data.ui.utils.SmartHomeInfoCard
import kotlinx.coroutines.launch


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
    onNavigateUp: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.data != null && result.data?.data != null) {
            viewModel.certUri = result.data?.data
            viewModel.isPasswordDialogVisible = true
        }
    }

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
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            SmartHomeInfoCard(
                message = stringResource(R.string.cert_info)
            )
            if (viewModel.isValidCertSelected) {
                InputSection(
                    onRemoveCertClicked = {
                        viewModel.removeCert()
                    },
                    onSelectCertClicked = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.setType("application/x-pkcs12")
                        launcher.launch(intent)
                    }
                )
            }
            else {
                InputSectionError(
                    onSelectCertClicked = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.setType("application/x-pkcs12")
                        launcher.launch(intent)
                    }
                )
            }
        }
        if (viewModel.isPasswordDialogVisible) {
            PasswordDialog(
                onPasswordEntered = { password ->
                    viewModel.importCertificate(password)
                    viewModel.isPasswordDialogVisible = false
                },
                onDismissed = {
                    viewModel.isPasswordDialogVisible = false
                }
            )
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


/**
 * Composable displays a dialog through which the user can enter a password.
 *
 * @param onPasswordEntered Callback to invoke once a password was entered.
 * @param onDismissed       Callback to invoke once the dialog closes.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PasswordDialog(
    onPasswordEntered: (String) -> Unit,
    onDismissed: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissed,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.space_horizontal))
        ) {
            Text(
                text = stringResource(R.string.cert_password_info)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {
                    password = it
                },
                label = {
                    Text(stringResource(R.string.cert_password_hint))
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) { VisualTransformation.None } else { PasswordVisualTransformation() },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) { painterResource(R.drawable.ic_visible) } else { painterResource(R.drawable.ic_invisible) }
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            painter = image,
                            contentDescription = ""
                        )
                    }
                }
            )
            FlowRow(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = dimensionResource(R.dimen.space_vertical)),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onDismissed
                ) {
                    Text(stringResource(R.string.button_cancel))
                }
                Button(
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.space_horizontal_between)),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onPasswordEntered(password)
                        }
                    },
                    enabled = password.isNotEmpty()
                ) {
                    Text(stringResource(R.string.button_ok))
                }
            }
        }
    }
}
