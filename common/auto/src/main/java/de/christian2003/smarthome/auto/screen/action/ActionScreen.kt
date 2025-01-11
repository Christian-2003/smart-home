package de.christian2003.smarthome.auto.screen.action

import androidx.annotation.OptIn
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.annotations.ExperimentalCarApi
import androidx.car.app.model.Action
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template


class ActionScreen(
    carContext: CarContext,
    private val device: String
): Screen(carContext) {

    @OptIn(ExperimentalCarApi::class)
    override fun onGetTemplate(): Template {
        val messageBuilder = MessageTemplate.Builder("This is an action for $device")

        messageBuilder.addAction(
            Action.Builder()
                .setTitle("On")
                .setOnClickListener {

                }
                .build()
        )

        messageBuilder.addAction(
            Action.Builder()
                .setTitle("Off")
                .setOnClickListener {

                }
                .build()
        )

        messageBuilder.setTitle(device)
        messageBuilder.setHeaderAction(Action.BACK)


        return messageBuilder.build()
    }

}
