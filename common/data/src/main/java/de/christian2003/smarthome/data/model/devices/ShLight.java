package de.christian2003.smarthome.data.model.devices;

import androidx.annotation.NonNull;
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
     * The milli amp of the light.
     */
    @Nullable
    private final String milliAmp;

    /**
     * Constructor instantiates a new light.
     *
     * @param name          Name for the light.
     * @param specifier     The specifier of the device to distinguish different devices in a room that are the same type.
     * @param imageUri      URI for the image for the light.
     * @param onButtonText  Text for the button to turn on the light.
     * @param offButtonText Text for the button to turn off the light.
     * @param milliAmp      The milli amp of the light.
     */
    public ShLight(@NonNull String name, @Nullable String specifier, @Nullable String imageUri, @Nullable String onButtonText, @Nullable String offButtonText, @Nullable String milliAmp) {
        super(name, specifier , imageUri);
        this.onButtonText = onButtonText;
        this.offButtonText = offButtonText;
        this.milliAmp = milliAmp;
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

    /**
     * Method returns the milli amp of the lighting.
     *
     * @return The milli amp of the lighting.
     */
    @Nullable
    public String getMilliAmp() {
        return milliAmp;
    }

}
