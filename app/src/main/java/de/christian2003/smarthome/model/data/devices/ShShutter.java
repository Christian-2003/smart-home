package de.christian2003.smarthome.model.data.devices;

import androidx.annotation.Nullable;


/**
 * Class models a shutter for the smart home device.
 */
public class ShShutter extends ShGenericDevice {

    /**
     * Attribute stores the text for the button to set the percentage to which to close the
     * shutter.
     */
    @Nullable
    private final String setButtonText;

    /**
     * Attribute stores the percentage to which the shutter is closed. This is {@code null} if no
     * percentage is provided.
     */
    @Nullable
    private String percentage;

    /**
     * Attribute stores the time at which the shutter was closed? This is {@code null} if no time
     * is provided.
     */
    @Nullable
    private String time;


    /**
     * Constructor instantiates a new shutter for the smart home.
     *
     * @param name          Name for the shutter.
     * @param setButtonText Text for the button through which to set the percentage to which the
     *                      shutter is closed.
     * @param percentage    Percentage to which the shutter is closed.
     * @param time          Time at which the shutter was closed?
     */
    public ShShutter(@Nullable String name, @Nullable String setButtonText, @Nullable String percentage, @Nullable String time) {
        super(name, null);
        this.setButtonText = setButtonText;
        this.percentage = percentage;
        this.time = time;
    }


    /**
     * Method returns the text for the button through which to set the percentage to which the
     * shutter is closed.
     *
     * @return  Percentage to which the shutter is closed.
     */
    @Nullable
    public String getSetButtonText() {
        return setButtonText;
    }

    /**
     * Method returns the percentage to which the shutter is closed. This returns {@code null} if no
     * percentage is provided.
     *
     * @return  Percentage to which the shutter is closed.
     */
    @Nullable
    public String getPercentage() {
        return percentage;
    }

    /**
     * Method returns the time at which the shutter was closed. This returns {@code null} if no time
     * is provided.
     *
     * @return  Time at which the shutter was closed
     */
    @Nullable
    public String getTime() {
        return time;
    }

}
