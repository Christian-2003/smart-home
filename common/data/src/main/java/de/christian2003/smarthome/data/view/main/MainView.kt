package de.christian2003.smarthome.data.view.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import de.christian2003.smarthome.data.R
import de.christian2003.smarthome.data.model.cert.CertHandler
import de.christian2003.smarthome.data.model.cert.SslTrustResponse
import de.christian2003.smarthome.data.model.cert.SslTrustStatus
import de.christian2003.smarthome.data.model.room.ShRoom
import de.christian2003.smarthome.data.model.userinformation.UserInformation
import de.christian2003.smarthome.data.ui.utils.SmartHomeInfoCard
import de.christian2003.smarthome.data.view.room.ListRowWarning
import kotlinx.coroutines.selects.OnCancellationConstructor
import java.security.cert.X509Certificate


/**
 * Composable displays the main view for the app.
 *
 * @param viewModel             View model for the view.
 * @param onNavigateToSettings  Callback to invoke in order to navigate to the settings
 * @param onNavigateToRoom      Callback to invoke in order to navigate to a specific room.
 * @param onFinishActivity      Callback invoked once the activity (i.e. the entire app) should close.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToRoom: (Int) -> Unit,
    onFinishActivity: () -> Unit
) {
    val context = LocalContext.current
    val untrustCertErrorMessage = stringResource(R.string.main_cert_untrust)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.main_title),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.restartToFetchData()
                        },
                        enabled = !viewModel.isLoading
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            tint = if (!viewModel.isLoading) { MaterialTheme.colorScheme.onSurface } else { MaterialTheme.colorScheme.onSurface.copy(0.5f) },
                            contentDescription = ""
                        )
                    }
                    IconButton(
                        onClick = onNavigateToSettings
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.sslTrustResponse != null && viewModel.sslTrustResponse!!.status != SslTrustStatus.Trusted) {
                SslCert(
                    sslTrustResponse = viewModel.sslTrustResponse!!,
                    onTrustCert = { cert ->
                        val certHandler = CertHandler(context)
                        certHandler.saveCert(cert)
                        Log.d("MainView", "Saved cert")
                        viewModel.restartToFetchData()
                    },
                    onCancel = {
                        Toast.makeText(context, untrustCertErrorMessage, Toast.LENGTH_SHORT).show()
                        onFinishActivity()
                    }
                )
            }
            else if (viewModel.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.space_vertical))
                    )
                }
                Text(
                    text = stringResource(R.string.main_loading),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.space_horizontal),
                            vertical = dimensionResource(R.dimen.space_vertical)
                        )
                )
            }
            else {
                if (viewModel.rooms.isEmpty() && (viewModel.infos.isEmpty() || (viewModel.infos.isNotEmpty() && !viewModel.showErrors && !viewModel.showWarnings))) {
                    EmptyPlaceholder(
                        title = stringResource(R.string.main_empty_title),
                        text = stringResource(R.string.main_empty_text),
                        icon = painterResource(R.drawable.el_rooms),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else {
                    RoomsList(
                        rooms = viewModel.rooms,
                        infos = viewModel.infos,
                        onRoomClicked = { room ->
                            onNavigateToRoom(viewModel.rooms.indexOf(room))
                        },
                        showWarnings = viewModel.showWarnings,
                        showErrors = viewModel.showErrors
                    )
                }
            }
        }
    }
}


/**
 * Composable displays a list of rooms.
 *
 * @param rooms         List of rooms to display.
 * @param onRoomClicked Callback to invoke once a room is clicked.
 */
@Composable
fun RoomsList(
    rooms: List<ShRoom>,
    infos: List<UserInformation>,
    onRoomClicked: (ShRoom) -> Unit,
    showWarnings: Boolean,
    showErrors: Boolean
) {
    LazyColumn {
        items(infos) { information ->
            ListRowWarning(
                information = information,
                showWarnings = showWarnings,
                showErrors = showErrors
            )
        }
        items(rooms) { room ->
            if (room.isGesamtstatusElement) {
                RoomsListRowGeneralStatus(
                    generalStatus = room
                )
                Text(
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.space_horizontal),
                        top = dimensionResource(R.dimen.space_vertical_between),
                        end = dimensionResource(R.dimen.space_horizontal),
                        bottom = dimensionResource(R.dimen.space_vertical_between)),
                    text = stringResource(R.string.main_rooms),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            else {
                RoomsListRow(
                    room = room,
                    onRoomClicked = onRoomClicked
                )
            }
        }
    }
}


/**
 * Composable displays a single room.
 *
 * @param room          Room to display.
 * @param onRoomClicked Callback to invoke once the room is clicked.
 */
@Composable
fun RoomsListRow(
    room: ShRoom,
    onRoomClicked: (ShRoom) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRoomClicked(room)
            }
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical_between),
                horizontal = dimensionResource(R.dimen.space_horizontal))
    ) {
        Text(
            text = room.name,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


/**
 * Composable displays the general status for the smart home.
 *
 * @param generalStatus General status for the smart home.
 */
@Composable
fun RoomsListRowGeneralStatus(
    generalStatus: ShRoom
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
            .padding(vertical = dimensionResource(R.dimen.space_vertical_between))
    ) {
        generalStatus.infos.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.space_horizontal),
                        vertical = dimensionResource(R.dimen.space_vertical_between)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = dimensionResource(R.dimen.space_horizontal_between)),
                    text = item.label,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (item.text != null) { item.text!! } else { "" },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


/**
 * Composable displays an empty placeholder indicating that there is no data available.
 *
 * @param title     Title for the placeholder.
 * @param text      Text for the placeholder.
 * @param icon      Icon for the placeholder.
 * @param modifier  Modifier.
 */
@Composable
fun EmptyPlaceholder(
    title: String,
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.space_horizontal),
            vertical = dimensionResource(R.dimen.space_vertical)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "",
                modifier = Modifier.size(dimensionResource(R.dimen.image_extra_large))
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


/**
 * Composable displays an SSL certificate that the user needs to trust in order to proceed.
 *
 * @param sslTrustResponse  Trust response.
 * @param onTrustCert       Callback invoked once the user trusts the certificate.
 * @param onCancel          Callback invoked once the user does not trust the certificate.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SslCert(
    sslTrustResponse: SslTrustResponse,
    onTrustCert: (X509Certificate) -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SmartHomeInfoCard(message = stringResource(R.string.main_cert_info))
        if (sslTrustResponse.cert == null) {
            SmartHomeInfoCard(
                message = stringResource(R.string.main_cert_unknown),
                iconResource = R.drawable.ic_error,
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                foregroundColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
        else {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.space_horizontal),
                        vertical = dimensionResource(R.dimen.space_vertical)
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
                CertItem(
                    title = stringResource(R.string.main_cert_subject),
                    content = sslTrustResponse.cert.subjectDN.toString()
                )
                CertItem(
                    title = stringResource(R.string.main_cert_issuer),
                    content = sslTrustResponse.cert.issuerDN.toString()
                )
                CertItem(
                    title = stringResource(R.string.main_cert_valid),
                    content = sslTrustResponse.cert.notAfter.toString()
                )
                FlowRow(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onCancel
                    ) {
                        Text(stringResource(R.string.button_cancel))
                    }
                    Button(
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.space_horizontal_between)),
                        onClick = {
                            onTrustCert(sslTrustResponse.cert)
                        }
                    ) {
                        Text(stringResource(R.string.button_trust_cert))
                    }
                }
            }
        }
    }
}


/**
 * Composable displays a certificate item.
 *
 * @param title     Title for the certificate item.
 * @param content   Content of the certificate item.
 */
@Composable
private fun CertItem(
    title: String,
    content: String
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = dimensionResource(R.dimen.space_vertical_between))
    )
    Text(
        text = content,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.space_vertical_between))
    )
}
