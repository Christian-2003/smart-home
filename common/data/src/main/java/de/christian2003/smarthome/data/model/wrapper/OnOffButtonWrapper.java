package de.christian2003.smarthome.data.model.wrapper;

import androidx.annotation.Nullable;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


/**
 * Wrapper for returning the buttons of a light and user information that occurs while gathering the properties.
 */
public class OnOffButtonWrapper {
    /**
     * Attribute stores the label of the on button.
     */
    @Nullable
    private final String onButton;

    /**
     * Attribute stores the label of the off button.
     */
    @Nullable
    private final String offButton;

    /**
     * Attribute stores a user information.
     */
    @Nullable
    private final UserInformation userInformation;

    /**
     *Constructor instantiates a new {@link OnOffButtonWrapper} to return the labels of the buttons and the user information that occurred while gathering the properties.
     *
     * @param onButton      The label of the on button.
     * @param offButton     The label of the off button.
     * @param userInformation       The user information that might occur while gathering the properties.
     */
    public OnOffButtonWrapper(@Nullable String onButton, @Nullable String offButton, @Nullable UserInformation userInformation) {
        this.onButton = onButton;
        this.offButton = offButton;
        this.userInformation = userInformation;
    }

    /**
     * Method returns a string with the label of the on button.
     *
     * @return      A string with the label of the on button.
     */
    @Nullable
    public String getOnButton() {
        return onButton;
    }

    /**
     * Method returns a string with the label of the off button.
     *
     * @return      A string with the label of the off button.
     */
    @Nullable
    public String getOffButton() {
        return offButton;
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
