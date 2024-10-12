package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;
import androidx.annotation.Nullable;


/**
 * Class models an opening in the house (i.e. a door or window).
 */
public class ShOpening extends ShGenericDevice {

    /**
     * Constructor instantiates a new door / window.
     *
     * @param name      Name for the door / window.
     * @param imageUri  URI to the image for the door / window.
     */
    public ShOpening(@Nullable String name, @Nullable Uri imageUri) {
        super(name, imageUri);
    }

}
