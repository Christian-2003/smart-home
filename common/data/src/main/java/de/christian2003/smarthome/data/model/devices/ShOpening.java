package de.christian2003.smarthome.data.model.devices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Class models an opening in the house (i.e. a door or window).
 */
public class ShOpening extends ShGenericDevice {

    /**
     * The {@link ShOpeningType} of the opening.
     */
    @NonNull
    private final ShOpeningType openingType;

    /**
     * Constructor instantiates a new door / window.
     *
     * @param name  Name for the door / window.
     * @param openingType   The type of the opening (window / door).
     * @param specifier     Specifies the opening.
     * @param imageUri      String that represents the URI to the image for the door / window.
     */
    public ShOpening(@NonNull String name, @NonNull ShOpeningType openingType, @Nullable String specifier, @Nullable String imageUri) {
        super(name, specifier, imageUri);
        this.openingType = openingType;
    }

    /**
     * Method returns the {@link ShOpeningType} of the opening.
     *
     * @return The {@link ShOpeningType} of the opening.
     */
    @NonNull
    public ShOpeningType getOpeningType() {
        return openingType;
    }
}