package de.christian2003.smarthome.auto.screen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.christian2003.smarthome.auto.screen.room.RoomScreen

class MainScreen(carContext: CarContext): Screen(carContext) {

    override fun onGetTemplate(): Template {
        val itemListBuilder = ItemList.Builder()
        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Bathroom")
                .setOnClickListener {
                    screenManager.push(RoomScreen(carContext, "Bathroom"))
                }
                .build()
        )

        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Kitchen")
                .setOnClickListener {
                    screenManager.push(RoomScreen(carContext, "Kitchen"))
                }
                .build()
        )
        itemListBuilder.addItem(
            Row.Builder()
                .setTitle("Living Room")
                .setOnClickListener {
                    screenManager.push(RoomScreen(carContext, "Living Room"))
                }
                .build()
        )

        return ListTemplate.Builder()
            .setSingleList(itemListBuilder.build())
            .setHeaderAction(Action.APP_ICON)
            .build()
    }

}
