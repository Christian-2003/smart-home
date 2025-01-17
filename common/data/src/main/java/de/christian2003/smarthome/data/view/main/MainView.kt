package de.christian2003.smarthome.data.view.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import de.christian2003.smarthome.data.R
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Composable displays the main view for the app.
 *
 * @param viewModel             View model for the view.
 * @param onNavigateToSettings  Callback to invoke in order to navigate to the settings
 * @param onNavigateToRoom      Callback to invoke in order to navigate to a specific room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToRoom: (Int) -> Unit
) {
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
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.space_vertical))
                )
            }
            else {
                RoomsList(
                    rooms = viewModel.rooms,
                    onRoomClicked = { room ->
                        onNavigateToRoom(viewModel.rooms.indexOf(room))
                    }
                )
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
    onRoomClicked: (ShRoom) -> Unit
) {
    LazyColumn {
        items(rooms) { room ->
            if (room.name.contains("Gesamtstatus")) {
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
@OptIn(ExperimentalLayoutApi::class)
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
