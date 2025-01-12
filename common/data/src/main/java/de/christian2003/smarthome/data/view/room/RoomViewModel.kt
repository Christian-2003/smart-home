package de.christian2003.smarthome.data.view.room

import androidx.lifecycle.ViewModel
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.model.room.ShRoom

class RoomViewModel: ViewModel() {

    /**
     * Attribute stores the repository through which to access the data.
     */
    private lateinit var repository: SmartHomeRepository


    lateinit var room: ShRoom


    fun init(repository: SmartHomeRepository, position: Int) {
        this.repository = repository
        room = repository.rooms[position]
    }

}
