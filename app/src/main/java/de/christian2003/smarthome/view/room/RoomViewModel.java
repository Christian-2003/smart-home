package de.christian2003.smarthome.view.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


public class RoomViewModel extends ViewModel {

    @Nullable
    private ShRoom room;

    private final ArrayList<UserInformation> warnings;

    private final ArrayList<UserInformation> errors;


    public RoomViewModel() {
        room = null;
        warnings = new ArrayList<>();
        errors = new ArrayList<>();
    }


    @Nullable
    public ShRoom getRoom() {
        return room;
    }

    public void setRoom(@NonNull ShRoom room) {
        this.room = room;
        warnings.clear();
        errors.clear();
        for (UserInformation userInformation : room.getUserInformation()) {
            if (userInformation.getInformationType() == InformationType.WARNING) {
                warnings.add(userInformation);
            }
            else if (userInformation.getInformationType() == InformationType.ERROR) {
                errors.add(userInformation);
            }
        }
    }

    @NonNull
    public ArrayList<UserInformation> getWarnings() {
        return warnings;
    }

    @NonNull
    public ArrayList<UserInformation> getErrors() {
        return errors;
    }

}
