package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


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
     * The milli amp of the lighting.
     */
    @Nullable
    private final String milliAmp;

    /**
     * Constructor instantiates a new light.
     *
     * @param name          Name for the light.
     * @param imageUri      URI for the image for the light.
     * @param onButtonText  Text for the button to turn on the light.
     * @param offButtonText Text for the button to turn off the light.
     */
    public ShLight(@Nullable String name, @Nullable Uri imageUri, @Nullable String onButtonText, @Nullable String offButtonText, @Nullable String milliAmp) {
        super(name, null, imageUri);
        this.onButtonText = onButtonText;
        this.offButtonText = offButtonText;
        this.milliAmp = milliAmp;
    }

    /**
     * Finds a single lighting and further properties of it.
     *
     * @param secondDataCell    The second data cell of the table row which contains the lighting.
     * @param openingName   The name of the lighting.
     * @param specifier     The specifier of the lighting.
     * @return  A {@link RoomDeviceWrapper} which contains the lighting and a list of the warnings that occurred while gathering the information.
     */
    public RoomDeviceWrapper findSingleLighting(@NonNull Element secondDataCell, @NonNull String openingName, @Nullable String specifier) {
        Element image = secondDataCell.selectFirst("td  img");
        if (image != null) {
            String source = image.attr("src");

            // Check if a source could be found.
            if (!source.isEmpty()){
                Uri imageUri = Uri.parse("https://smarthome.de/" + source);



                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, imageUri))), new ArrayList<>());
            }
            // No image source found for opening but object can be created anyways.
            else {
                System.out.println("Source Empty");
                String warningDescription = "An image node could be found but not source for the image. The opening could be created anyways. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        // No image found for opening but object can be created anyways.
        else {
            System.out.println("keine Image node");
            String warningDescription = "No image could be found for the opening but it could be created anyways. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
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
