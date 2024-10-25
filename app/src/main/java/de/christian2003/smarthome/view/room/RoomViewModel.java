package de.christian2003.smarthome.view.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import de.christian2003.smarthome.model.data.ShRoom;


public class RoomViewModel extends ViewModel {

    @Nullable
    private ShRoom room;


    public RoomViewModel() {
        room = null;
    }


    @Nullable
    public ShRoom getRoom() {
        return room;
    }

    public void setRoom(@NonNull ShRoom room) {
        this.room = room;
    }

}
