package de.christian2003.smarthome.auto.screen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.christian2003.smarthome.data.model.room.ShInfoText
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Class implements the main screen which displays a list of all rooms.
 */
class MainScreen(
    carContext: CarContext,
    private val rooms: List<ShRoom>
): Screen(carContext) {

    /**
     * Method creates the template for the screen.
     *
     * @return  Template for the screen.
     */
    override fun onGetTemplate(): Template {
        val builder = ItemList.Builder()

        if (rooms.isNotEmpty()) {
            rooms[0].infos.forEach { infoText ->
                builder.addItem(buildRow(infoText))
            }
        }
        if (rooms.size >= 2) {
            rooms.subList(1, rooms.size).forEach { room ->
                builder.addItem(buildRow(room))
            }
        }

        return ListTemplate.Builder()
            .setSingleList(builder.build())
            .setHeaderAction(Action.APP_ICON)
            .setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.app_name))
            .build()
    }


    /**
     * Method creates a row for a room.
     *
     * @param room  Room for which to create the row.
     * @return      Row for the room specified.
     */
    private fun buildRow(room: ShRoom): Row {
        val builder = Row.Builder()
        builder.setTitle(room.name)
        builder.addText(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.car_details_subtext))
        builder.setOnClickListener {
            screenManager.push(RoomScreen(carContext, room))
        }
        return builder.build()
    }

    /**
     * Method creates a row for an info text.
     *
     * @param infoText  Info text for which to create the row.
     * @return          Row for the info text specified.
     */
    private fun buildRow(infoText: ShInfoText): Row {
        var title = carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.car_main_text)
        title = title.replace("{label}", infoText.label)
        title = title.replace("{text}", if (infoText.text != null) { infoText.text!! } else { "" })

        val builder = Row.Builder()
        builder.setTitle(title)
        return builder.build()
    }

}
