package de.christian2003.smarthome.data.view.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import de.christian2003.smarthome.data.model.room.ShRoom


class MainViewModel: ViewModel() {

    private val _rooms = mutableStateListOf<ShRoom>()

    val rooms: List<ShRoom> get() = _rooms


    fun init(rooms: MutableList<ShRoom>) {
        _rooms.addAll(rooms)
    }

}
