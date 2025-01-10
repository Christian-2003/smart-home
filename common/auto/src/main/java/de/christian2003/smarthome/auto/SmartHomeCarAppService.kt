package de.christian2003.smarthome.auto

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator


class SmartHomeCarAppService: CarAppService() {

    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }


    override fun onCreateSession(): Session {
        return SmartHomeSession()
    }

}
