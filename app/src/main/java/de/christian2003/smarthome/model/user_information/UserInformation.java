package de.christian2003.smarthome.model.user_information;

import androidx.annotation.NonNull;

/**
 * Call models the information that can be displayed for the user.
 */
public class UserInformation {

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
     *Constructor instantiates a new user information.
     *
     * @param informationType
     * @param informationTitle
     * @param description
     */
    public UserInformation(@NonNull InformationType informationType, @NonNull InformationTitle informationTitle,@NonNull String description) {
        this.informationType = informationType;
        this.informationTitle = informationTitle;
        this.description = description;
    }

    @NonNull
    public InformationType getInformationType() {
        return informationType;
    }

    @NonNull
    public InformationTitle getInformationTitle() {
        return informationTitle;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
