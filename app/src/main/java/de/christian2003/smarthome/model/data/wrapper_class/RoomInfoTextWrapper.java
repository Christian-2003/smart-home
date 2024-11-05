package de.christian2003.smarthome.model.data.wrapper_class;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import de.christian2003.smarthome.model.data.ShInfoText;
import de.christian2003.smarthome.model.user_information.UserInformation;

/**
 * Wrapper for returning a list of info texts and user information.
 */
public class RoomInfoTextWrapper {
    /**
     * Attribute stores a list of info texts for the room. Exemplary info texts could include
     * temperature, humidity, air pressure, ...
     */
    @NonNull
    private final ArrayList<ShInfoText> infoTexts;

    /**
     * Attribute stores a list of all the user information belonging to the room.
     */
    @NonNull
    private final ArrayList<UserInformation> userInformation;

    /**
     * Constructor instantiates a new RoomInformationWrapper to return the info texts and user information form the methods that gather the properties of a room.
     *
     * @param infoTexts             A list containing the info texts that were return by a certain method.
     * @param userInformation       A list containing the user information that occurred in the process of gathering the information of a room.
     */
    public RoomInfoTextWrapper(@NonNull ArrayList<ShInfoText> infoTexts, @NonNull ArrayList<UserInformation> userInformation) {
        this.infoTexts = infoTexts;
        this.userInformation = userInformation;
    }

    /**
     * Method returns the list containing the information texts.
     *
     * @return      List containing the information texts.
     */
    @NonNull
    public ArrayList<ShInfoText> getInfoTexts() {
        return infoTexts;
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
}
