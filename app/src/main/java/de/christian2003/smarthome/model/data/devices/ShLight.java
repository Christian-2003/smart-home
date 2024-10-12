package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;
import androidx.annotation.Nullable;


/**
 * Class models a light for the smart home.
 */
public class ShLight extends ShGenericDevice {

    /**
     * Attribute stores the text for the button to turn on the light.
     */
    @Nullable
    private final String onButtonText;

    /**
     * Attribute stores the text for the button to turn off the light.
     */
    @Nullable
    private final String offButtonText;


    /**
     * Constructor instantiates a new light.
     *
     * @param name          Name for the light.
     * @param imageUri      URI for the image for the light.
     * @param onButtonText  Text for the button to turn on the light.
     * @param offButtonText Text for the button to turn off the light.
     */
    public ShLight(@Nullable String name, @Nullable Uri imageUri, @Nullable String onButtonText, @Nullable String offButtonText) {
        super(name, imageUri);
        this.onButtonText = onButtonText;
        this.offButtonText = offButtonText;
    }


    /**
     * Method returns the text for the button with which to turn on the light.
     *
     * @return  Text for the button to turn on the light.
     */
    @Nullable
    public String getOnButtonText() {
        return onButtonText;
    }

    /**
     * Method returns the text for the button with which to turn off the light.
     *
     * @return  Text for the button to turn off the light.
     */
    @Nullable
    public String getOffButtonText() {
        return offButtonText;
    }

}
