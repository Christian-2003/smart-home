package de.christian2003.smarthome.data.view.url

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
 * Composable displays the view through which to edit the server URL.
 *
 * @param viewModel     View model for the view.
 * @param onNavigateUp  Callback to invoke to navigate up on the navigation stack.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlView(
    viewModel: UrlViewModel,
    isFirstOnStack: Boolean,
    onNavigateUp: () -> Unit,
    onNavigateToNext: () -> Unit
) {
    val context = LocalContext.current
    val saveSuccess = stringResource(R.string.url_save_success)
    val saveError = stringResource(R.string.url_save_error)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.url_title),
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
                .verticalScroll(rememberScrollState())
        ) {
            SmartHomeInfoCard(
                message = stringResource(R.string.url_info)
            )

            InputSection(
                url = viewModel.url,
                isUrlValid = viewModel.isUrlValid,
                isFirstOnStack = isFirstOnStack,
                onUrlChanged = {
                    viewModel.url = it
                    viewModel.isUrlValid()
                },
                onCancelClicked = onNavigateUp,
                onSaveClicked = {
                    if (viewModel.saveUrl()) {
                        Toast.makeText(context, saveSuccess, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, saveError, Toast.LENGTH_LONG).show()
                    }
                    if (isFirstOnStack) {
                        onNavigateToNext()
                    }
                    else {
                        onNavigateUp()
                    }
                }
            )
        }
    }
}


/**
 * Composable displays the main input section for the view.
 *
 * @param url               URL to display within the input section.
 * @param isUrlValid        Whether the URL passed is valid.
 * @param onUrlChanged      Callback to invoke once the URL changes.
 * @param onCancelClicked   Callback to invoke once the cancel button is clicked.
 * @param onSaveClicked     Callback to invoke once the save button is clicked.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputSection(
    url: String,
    isUrlValid: Boolean,
    isFirstOnStack: Boolean,
    onUrlChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    val errorMessage = if (isUrlValid) { "" } else { stringResource(R.string.url_error) }

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
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = url,
            onValueChange = {
                onUrlChanged(it)
            },
            label = {
                Text(stringResource(R.string.url_input_server))
            },
            isError = !isUrlValid,
            supportingText = {
                Text(errorMessage)
            }
        )
        FlowRow(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onCancelClicked
            ) {
                Text(stringResource(R.string.button_cancel))
            }
            Button(
                modifier = Modifier.padding(start = dimensionResource(R.dimen.space_horizontal_between)),
                onClick = onSaveClicked,
                enabled = isUrlValid && url.isNotEmpty()
            ) {
                Text(if (isFirstOnStack) { stringResource(R.string.button_next) } else {stringResource(R.string.button_save)})
            }
        }
    }
}
