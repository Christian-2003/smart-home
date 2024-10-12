package de.christian2003.smarthome.model.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import de.christian2003.smarthome.model.data.devices.ShGenericDevice;


/**
 * Class models a room for the smart home.
 */
public class ShRoom {

    /**
     * Attribute stores the name of the room.
     */
    @Nullable
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
     * Constructor instantiates a new room.
     *
     * @param name      Name for the smart home.
     * @param infos     List of info texts for the room.
     * @param devices   List of smart home devices for the room.
     */
    public ShRoom(@Nullable String name, @Nullable ArrayList<ShInfoText> infos, @Nullable ArrayList<ShGenericDevice> devices) {
        this.name = name;
        this.infos = infos != null ? infos : new ArrayList<>();
        this.devices = devices != null ? devices : new ArrayList<>();
    }

    /**
     * Method returns the name of the room.
     *
     * @return  Name of the room.
     */
    @Nullable
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

}
