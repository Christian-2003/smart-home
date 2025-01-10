package de.christian2003.smarthome.data.model.wrapper;

import androidx.annotation.Nullable;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


/**
 * Wrapper for returning a String that contains e.g. the milliAmp of a light a user information.
 */
public class StringUserInformationWrapper {

    /**
     * Attribute stores the milli amp of a light.
     */
    @Nullable
    private final String property;

    /**
     * Attribute stores a user information.
     */
    @Nullable
    private final UserInformation userInformation;

    /**
     * Constructor instantiates a new {@link StringUserInformationWrapper} to return a String and user information form the methods that gather the properties of devices.
     *
     * @param property      A specific property of a device.
     * @param userInformation       The user information that might occur while gathering the properties.
     */
    public StringUserInformationWrapper(@Nullable String property, @Nullable UserInformation userInformation) {
        this.property = property;
        this.userInformation = userInformation;
    }

    /**
     * Method returns a String containing a property of a devices.
     *
     * @return      A String that contains a property.
     */
    @Nullable
    public String getProperty() {
        return property;
    }

    /**
     * Method returns the user information.
     *
     * @return      A {@link UserInformation} object.
     */
    @Nullable
    public UserInformation getUserInformation() {
        return userInformation;
    }

}
