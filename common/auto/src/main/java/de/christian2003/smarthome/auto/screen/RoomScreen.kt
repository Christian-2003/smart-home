package de.christian2003.smarthome.auto.screen

import androidx.annotation.OptIn
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.annotations.ExperimentalCarApi
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.christian2003.smarthome.data.model.devices.ShGenericDevice
import de.christian2003.smarthome.data.model.devices.ShLight
import de.christian2003.smarthome.data.model.devices.ShOpening
import de.christian2003.smarthome.data.model.devices.ShOutlet
import de.christian2003.smarthome.data.model.devices.ShShutter
import de.christian2003.smarthome.data.model.room.ShInfoText
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Class implements the screen displaying all devices and labels for a room.
 */
class RoomScreen(
    carContext: CarContext,
    private val room: ShRoom
): Screen(carContext) {

    /**
     * Method creates the template for the screen.
     *
     * @return  Template for the screen.
     */
    @OptIn(ExperimentalCarApi::class)
    override fun onGetTemplate(): Template {
        val itemListBuilder = ItemList.Builder()

        room.infos.forEach { info ->
            itemListBuilder.addItem(buildRow(info))
        }

        room.devices.forEach { device ->
            itemListBuilder.addItem(buildRow(device))
        }

        return ListTemplate.Builder()
            .setSingleList(itemListBuilder.build())
            .setHeaderAction(Action.BACK)
            .setTitle(room.name)
            .build()
    }


    /**
     * Method creates a row for an info text.
     *
     * @param text  Info text for which to create the row.
     * @return      Row created for the info text specified.
     */
    private fun buildRow(text: ShInfoText): Row {
        val builder = Row.Builder()
        builder.setTitle(text.label)
        if (text.text != null) {
            builder.addText(text.text!!)
        }
        return builder.build()
    }

    /**
     * Method builds a row for the generic device specified.
     *
     * @param device    Device for which to create the row.
     * @return          Row created for the device specified.
     */
    private fun buildRow(device: ShGenericDevice): Row {
        return when (device) {
            is ShLight -> buildRowLight(device)
            is ShOpening -> buildRowOpening(device)
            is ShOutlet -> buildRowOutlet(device)
            is ShShutter -> buildRowShutter(device)
            else -> Row.Builder().setTitle("").build()
        }
    }

    /**
     * Method builds a row for the light specified.
     *
     * @param device    Light for which to create the row.
     * @return          Row created for the light specified.
     */
    private fun buildRowLight(device: ShLight): Row {
        val builder = Row.Builder()
        builder.setTitle(if (device.specifier != null) { device.specifier!! } else { device.name })
        if (device.milliAmp != null) {
            builder.addText(device.milliAmp!!)
        }
        return builder.build()
    }

    /**
     * Method builds a row for the opening specified.
     *
     * @param device    Opening for which to create the row.
     * @return          Row created for the opening specified.
     */
    private fun buildRowOpening(device: ShOpening): Row {
        val builder = Row.Builder()
        builder.setTitle(if (device.specifier != null) { device.specifier!! } else { device.name })
        builder.addText(device.openingType.name)
        return builder.build()
    }

    /**
     * Method builds a row for the outlet specified.
     *
     * @param device    Outlet for which to create the row.
     * @return          Row created for the outlet specified.
     */
    private fun buildRowOutlet(device: ShOutlet): Row {
        val builder = Row.Builder()
        builder.setTitle(if (device.specifier != null) { device.specifier!! } else { device.name })
        var text = ""
        if (device.amperage != null) {
            text += device.amperage
        }
        if (device.time != null) {
            if (text.isNotEmpty()) {
                text += ", "
            }
            text += device.time
        }
        if (device.powerConsumption != null) {
            if (text.isNotEmpty()) {
                text += ", "
            }
            text += device.powerConsumption
        }
        if (text.isNotEmpty()) {
            builder.addText(text)
        }
        return builder.build()
    }

    /**
     * Method builds a row for the shutter specified.
     *
     * @param device    Shutter for which to create the row.
     * @return          Row created for the shutter specified.
     */
    private fun buildRowShutter(device: ShShutter): Row {
        val builder = Row.Builder()
        builder.setTitle(if (device.specifier != null) { device.specifier!! } else { device.name })
        if (device.time != null) {
            builder.addText(device.time!!)
        }
        return builder.build()
    }

}
