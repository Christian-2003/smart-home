package de.christian2003.smarthome.data.model.devices;

import androidx.annotation.Nullable;


/**
 * Class models an outlet for the smart home.
 */
public class ShOutlet extends ShGenericDevice {

    /**
     * Attribute stores the text for the button to turn on the outlet.
     */
    @Nullable
    private final String onButtonText;

    /**
     * Attribute stores the text for the button to turn off the outlet.
     */
    @Nullable
    private final String offButtonText;

    /**
     * Attribute stores the amperage (in "mA") for the outlet.
     */
    @Nullable
    private String amperage;

    /**
     * Attribute stores the time the outlet is on?
     */
    @Nullable
    private String time;

    /**
     * Attribute stores the power consumption (in "Wh") for the outlet.
     */
    @Nullable
    private String powerConsumption;


    /**
     * Constructor instantiates a new outlet.
     *
     * @param name              Name for the outlet.
     * @param imageUri          URI for the image for the outlet.
     * @param onButtonText      Text for the button to turn on the outlet.
     * @param offButtonText     Text for the button to turn off the outlet.
     * @param amperage          Amperage for the outlet (in "mA").
     * @param time              Time that the outlet is turned on (in "h").
     * @param powerConsumption  Power consumption of the outlet (in "Wh").
     */
    public ShOutlet(@Nullable String name, @Nullable String imageUri, @Nullable String onButtonText, @Nullable String offButtonText, @Nullable String amperage, @Nullable String time, @Nullable String powerConsumption) {
        super(name,null, imageUri);
        this.onButtonText = onButtonText;
        this.offButtonText = offButtonText;
        this.amperage = amperage;
        this.time = time;
        this.powerConsumption = powerConsumption;
    }


    /**
     * Method returns the text for the button with which to turn on the outlet.
     *
     * @return  Text for the button to turn on the outlet.
     */
    @Nullable
    public String getOnButtonText() {
        return onButtonText;
    }

    /**
     * Method returns the text for the button with which to turn off the outlet.
     *
     * @return  Text for the button to turn off the outlet.
     */
    @Nullable
    public String getOffButtonText() {
        return offButtonText;
    }

    /**
     * Method returns the amperage for the outlet.
     *
     * @return  Amperage for the outlet.
     */
    @Nullable
    public String getAmperage() {
        return amperage;
    }

    /**
     * Method returns the time that the outlet is turned on.
     *
     * @return  Time that the outlet is turned on.
     */
    @Nullable
    public String getTime() {
        return time;
    }

    /**
     * Method returns the power consumption for the outlet.
     *
     * @return  Power consumption for the outlet.
     */
    @Nullable
    public String getPowerConsumption() {
        return powerConsumption;
    }

}
