package de.christian2003.smarthome.data.model.wrapper;

import androidx.annotation.NonNull;
import java.util.ArrayList;
import de.christian2003.smarthome.data.model.devices.ShGenericDevice;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


/**
 * Wrapper for returning a list of devices and user information.
 */
public class RoomDeviceWrapper {
    /**
     * Attribute stores a list of devices for the room.
     */
    @NonNull
    private ArrayList<ShGenericDevice> devices;

    /**
     * Attribute stores a list of all the user information belonging to the room.
     */
    @NonNull
    private ArrayList<UserInformation> userInformation;

    /**
     * Constructor instantiates a new RoomInformationWrapper to return the devices and user information form the methods that gather the properties of a room.
     *
     * @param devices               A list containing the devices that were return by a certain method.
     * @param userInformation       A list containing the user information that occurred in the process of gathering the information of a room.
     */
    public RoomDeviceWrapper(@NonNull ArrayList<ShGenericDevice> devices, @NonNull ArrayList<UserInformation> userInformation) {
        this.devices = devices;
        this.userInformation = userInformation;
    }

    /**
     * Method returns the list containing the devices.
     *
     * @return      List containing the devices.
     */
    @NonNull
    public ArrayList<ShGenericDevice> getDevices() {
        return devices;
    }

    /**
     * Method returns the list containing the user information.
     *
     * @return      List containing the user information.
     */
    @NonNull
    public ArrayList<UserInformation> getUserInformation() {
        return userInformation;
    }

    /**
     * Sets the devices for the {@link RoomDeviceWrapper}.
     *
     * @param devices       A list of the devices that should be set for the {@link RoomDeviceWrapper}.
     */
    public void setDevices(@NonNull ArrayList<ShGenericDevice> devices) {
        this.devices = devices;
    }

    /**
     * Sets the user information for the {@link RoomDeviceWrapper}.
     *
     * @param userInformation       A list of the user information that should be set for the {@link RoomDeviceWrapper}.
     */
    public void setUserInformation(@NonNull ArrayList<UserInformation> userInformation) {
        this.userInformation = userInformation;
    }

    /**
     * Adds a list of devices to the existing list of the {@link RoomDeviceWrapper} object.
     *
     * @param devices       A list of the devices that should be added to the {@link RoomDeviceWrapper}.
     */
    public void addDevices(@NonNull ArrayList<ShGenericDevice> devices) {
        this.devices.addAll(devices);
    }

    /**
     * Adds a list of user information to the existing list of the {@link RoomDeviceWrapper} object.
     *
     * @param userInformation       A list of the user information that should be added to the {@link RoomDeviceWrapper}.
     */
    public void addUserInformation(@NonNull ArrayList<UserInformation> userInformation) {
        this.userInformation.addAll(userInformation);
    }

    /**
     * Combines two {@link RoomDeviceWrapper} objects.
     *
     * @param handedOverWrapper     The {@link RoomDeviceWrapper} that should be added.
     */
    public void combineWrapper(RoomDeviceWrapper handedOverWrapper) {
        this.addDevices(handedOverWrapper.getDevices());
        this.addUserInformation(handedOverWrapper.getUserInformation());
    }

}
