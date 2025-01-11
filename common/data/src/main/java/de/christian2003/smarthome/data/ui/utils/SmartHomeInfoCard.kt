package de.christian2003.smarthome.data.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import de.christian2003.smarthome.data.R


/**
 * Composable displays an info card informing the user about some stuff.
 *
 * @param message           Message to display to the user.
 * @param iconResource      Resource ID of the icon to display.
 * @param backgroundColor   Background color for the card.
 * @param foregroundColor   Foreground color for the card.
 */
@Composable
fun SmartHomeInfoCard(
    message: String,
    iconResource: Int = R.drawable.ic_info,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    foregroundColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corners_default)))
            .border(
                width = dimensionResource(R.dimen.borders_default),
                color = foregroundColor,
                shape = RoundedCornerShape(dimensionResource(R.dimen.corners_default))
            )
            .background(backgroundColor)
            .padding(
                vertical = dimensionResource(R.dimen.space_vertical),
                horizontal = dimensionResource(R.dimen.space_horizontal)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = dimensionResource(R.dimen.space_horizontal_between)),
            painter = painterResource(iconResource),
            tint = foregroundColor,
            contentDescription = ""
        )
        Text(
            modifier = Modifier.weight(1f),
            text = message,
            color = foregroundColor
        )
    }
}
