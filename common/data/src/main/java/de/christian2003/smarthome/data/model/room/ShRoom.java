package de.christian2003.smarthome.data.model.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import de.christian2003.smarthome.data.model.devices.ShGenericDevice;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


/**
 * Class models a room for the smart home.
 */
public class ShRoom implements Serializable {

    /**
     * Attribute stores the name of the room.
     */
    @NonNull
    private final String name;

    /**
     * Attribute stores a list of info texts for the room. Exemplary info texts could include
     * temperature, humidity, air pressure, ...
     */
    @NonNull
    private final ArrayList<ShInfoText> infos;

    /**
     * Attribute stores a list of smart home devices for the room, e.g. outlets, openings or lights.
     */
    @NonNull
    private final ArrayList<ShGenericDevice> devices;

    /**
     * Attribute stores a list of all the user information belonging to the room.
     */
    @NonNull
    private final ArrayList<UserInformation> userInformation;

    /**
     * States if the room display the "gesamtstatus".
     */
    private final boolean gesamtstatusElement;

    /**
     * Constructor instantiates a new room.
     *
     * @param name              Name for the room.
     * @param infos             List of info texts for the room.
     * @param devices           List of smart home devices for the room.
     * @param userInformation   List of warnings and errors about the room that should be displayed for the user.
     */
    public ShRoom(@NonNull String name, @Nullable ArrayList<ShInfoText> infos, @Nullable ArrayList<ShGenericDevice> devices, @Nullable ArrayList<UserInformation> userInformation, boolean gesamtstatusElement) {
        this.name = name;
        this.infos = infos != null ? infos : new ArrayList<>();
        this.devices = devices != null ? devices : new ArrayList<>();
        this.userInformation = userInformation != null ? userInformation : new ArrayList<>();
        this.gesamtstatusElement = gesamtstatusElement;
    }

    /**
     * Method returns the name of the room.
     *
     * @return  Name of the room.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Method returns a list of info texts for the room.
     *
     * @return  List of info texts for the room.
     */
    @NonNull
    public ArrayList<ShInfoText> getInfos() {
        return infos;
    }

    /**
     * Method returns a list of smart home devices for the room.
     *
     * @return  List of smart home devices for the room.
     */
    @NonNull
    public ArrayList<ShGenericDevice> getDevices() {
        return devices;
    }

    /**
     * Method returns a list of information for the user that were generated during parsing.
     *
     * @return  List of user information.
     */
    @NonNull
    public ArrayList<UserInformation> getUserInformation() {
        return userInformation;
    }

    /**
     * Method returns a boolean that states if the room display the "gesamtstatus".
     *
     * @return  Boolean that states if the room display the "gesamtstatus".
     */
    public boolean isGesamtstatusElement() {return gesamtstatusElement;}
}
