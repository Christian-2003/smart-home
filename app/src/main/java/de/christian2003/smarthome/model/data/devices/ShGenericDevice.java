package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;
import androidx.annotation.Nullable;


/**
 * Class models a generic smart home device. All other smart home devices must extend this class.
 */
public abstract class ShGenericDevice {

    /**
     * Attribute stores the name of the device. This name is shown to the user.
     */
    @Nullable
    private final String name;

    /**
     * Attribute stores the URI for the image to display to the user. This will be {@code null} if
     * the smart home device does not have any image.
     */
    @Nullable
    private final Uri imageUri;


    /**
     * Constructor instantiates a new generic smart home device.
     *
     * @param name  Name for the device.
     * @param imageUri  URI for the image to display to the user.
     */
    public ShGenericDevice(@Nullable String name, @Nullable Uri imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }


    /**
     * Method returns the name of the smart home device.
     *
     * @return  Name of the smart home device.
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Method returns the URI for the image to display to the user. The method returns {@code null}
     * if the smart home device does not have any image to display.
     *
     * @return  URI for the image to display to the user.
     */
    @Nullable
    public Uri getImageUri() {
        return imageUri;
    }

}
