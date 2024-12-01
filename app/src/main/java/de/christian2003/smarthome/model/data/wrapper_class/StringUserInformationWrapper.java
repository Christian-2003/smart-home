package de.christian2003.smarthome.model.data.wrapper_class;

import androidx.annotation.Nullable;

import de.christian2003.smarthome.model.user_information.UserInformation;

/**
 * Wrapper for returning a String that contains e.g. the milliAmp of a light a user information.
 */
public class StringUserInformationWrapper {

    /**
     * Attribute stores the milli amp of a light.
     */
    @Nullable
    private final String milliAmp;

    /**
     * Attribute stores a user information.
     */
    @Nullable
    private final UserInformation userInformation;

    /**
     * Constructor instantiates a new {@link StringUserInformationWrapper} to return a String and user information form the methods that gather the properties of devices.
     *
     * @param milliAmp      The milli amp of a light.
     * @param userInformation       The user information that might occur while gathering the properties.
     */
    public StringUserInformationWrapper(@Nullable String milliAmp, @Nullable UserInformation userInformation) {
        this.milliAmp = milliAmp;
        this.userInformation = userInformation;
    }

    /**
     * Method returns a String containing a property of a devices.
     *
     * @return      A String that contains a property.
     */
    @Nullable
    public String getMilliAmp() {
        return milliAmp;
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
