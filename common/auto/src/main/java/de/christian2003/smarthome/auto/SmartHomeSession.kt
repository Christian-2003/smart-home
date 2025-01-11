package de.christian2003.smarthome.auto

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session
import de.christian2003.smarthome.auto.screen.MainScreen
import de.christian2003.smarthome.auto.screen.room.RoomScreen

class SmartHomeSession: Session() {

    override fun onCreateScreen(intent: Intent): Screen {
        return MainScreen(carContext)
    }

}
