package de.christian2003.smarthome.auto

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator


/**
 * Class implements the service through which the app can display contents on a car screen.
 */
class SmartHomeCarAppService: CarAppService() {

    /**
     * Method creates the host validator for the car app.
     *
     * @return  Host validator created.
     */
    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }


    /**
     * Method creates a car app session.
     *
     * @return  Session.
     */
    override fun onCreateSession(): Session {
        return SmartHomeSession()
    }

}
