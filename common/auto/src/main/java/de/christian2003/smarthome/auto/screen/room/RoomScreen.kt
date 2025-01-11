package de.christian2003.smarthome.auto.screen.room

import android.util.Log
import androidx.annotation.OptIn
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.annotations.ExperimentalCarApi
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.christian2003.smarthome.auto.screen.action.ActionScreen


class RoomScreen(
    carContext: CarContext,
    private val room: String
): Screen(carContext) {

    @OptIn(ExperimentalCarApi::class)
    override fun onGetTemplate(): Template {
        val itemListBuilder = ItemList.Builder()
        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Light")
                .addText("More actions available")
                .setOnClickListener {
                    screenManager.push(ActionScreen(carContext, "$room-Light"))
                }
                .build()
        )

        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Window (33%)")
                .build()
        )
        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Door (0%)")
                .build()
        )

        return ListTemplate.Builder()
            .setSingleList(itemListBuilder.build())
            .setHeaderAction(Action.BACK)
            .setTitle(room)
            .build()
    }

}
