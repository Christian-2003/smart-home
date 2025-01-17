package de.christian2003.smarthome.data.view.room

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Class implements the view model for the page to display the contents of a room.
 */
class RoomViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the preferences in which to store the URL.
     */
    private val preferences = getApplication<Application>().getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Attribute stores the repository through which to access the data.
     */
    private lateinit var repository: SmartHomeRepository

    /**
     * Attribute stores whether to display warnings.
     */
    var showWarnings: Boolean by mutableStateOf(true)

    /**
     * Attribute stores whether to display errors.
     */
    var showErrors: Boolean by mutableStateOf(true)

    /**
     * Attribute stores the room to display.
     */
    lateinit var room: ShRoom

    /**
     * Attribute stores the items of the room that shall be displayed. This contains user information,
     * infos as well as devices.
     */
    lateinit var items: List<Any>


    /**
     * Method initializes the view model.
     *
     * @param repository    Repository from which to source the data.
     * @param position      Position of the room to display.
     */
    fun init(repository: SmartHomeRepository, position: Int) {
        this.repository = repository
        room = repository.rooms[position]

        showWarnings = preferences.getBoolean("show_warnings", true)
        showErrors = preferences.getBoolean("show_errors", true)

        val mutableList = mutableListOf<Any>()
        mutableList.addAll(room.userInformation)
        mutableList.addAll(room.infos)
        mutableList.addAll(room.devices)
        items = mutableList
    }

}
