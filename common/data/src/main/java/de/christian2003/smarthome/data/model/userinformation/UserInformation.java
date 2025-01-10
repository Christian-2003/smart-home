package de.christian2003.smarthome.data.model.userinformation;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Call models the information that can be displayed for the user.
 */
public class UserInformation implements Serializable {

    /**
     * States which type of information will de displayed for the user.
     */
    @NonNull
    private final InformationType informationType;

    /**
     * States the title of the information that will be displayed.
     */
    @NonNull
    private final InformationTitle informationTitle;

    /**
     * The description that will explain the error or warning further.
     */
    @NonNull
    private final String description;

    /**
     * Stores whether the description is visible in the UI.
     */
    private boolean descriptionVisible;

    /**
     *Constructor instantiates a new user information.
     *
     * @param informationType       States which type of information will de displayed for the user.
     * @param informationTitle      States the title of the information that will be displayed.
     * @param description           The description that will explain the error or warning further.
     */
    public UserInformation(@NonNull InformationType informationType, @NonNull InformationTitle informationTitle,@NonNull String description) {
        this.informationType = informationType;
        this.informationTitle = informationTitle;
        this.description = description;
        descriptionVisible = false;
    }

    /**
     * Gets the type of the information.
     *
     * @return      The type of the information.
     */
    @NonNull
    public InformationType getInformationType() {
        return informationType;
    }

    /**
     * Gets the title of the information.
     *
     * @return      The title of the information.
     */
    @NonNull
    public InformationTitle getInformationTitle() {
        return informationTitle;
    }

    /**
     * Gets the description of the information.
     *
     * @return      The description of the information.
     */
    @NonNull
    public String getDescription() {
        return description;
    }

    /**
     * Method returns whether the description is visible.
     *
     * @return  Whether the description is visible.
     */
    public boolean isDescriptionVisible() {
        return descriptionVisible;
    }

    /**
     * Method changes whether the description is visible.
     *
     * @param descriptionVisible    Whether the description is visible.
     */
    public void setDescriptionVisible(boolean descriptionVisible) {
        this.descriptionVisible = descriptionVisible;
    }

}
