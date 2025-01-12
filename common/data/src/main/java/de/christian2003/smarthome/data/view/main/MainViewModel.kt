package de.christian2003.smarthome.data.view.main

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Class implements the view model for the main view.
 */
class MainViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the repository through which to access the data.
     */
    private lateinit var repository: SmartHomeRepository

    /**
     * Attribute indicates whether the webpage content is currently loading.
     */
    var isLoading: Boolean by mutableStateOf(false)

    /**
     * Attribute stores the list of rooms.
     */
    var rooms: List<ShRoom> by mutableStateOf(emptyList())


    /**
     * Method initializes the view model.
     */
    fun init(repository: SmartHomeRepository) {
        this.repository = repository
        this.isLoading = repository.isLoading
        this.rooms = repository.rooms
    }

}
