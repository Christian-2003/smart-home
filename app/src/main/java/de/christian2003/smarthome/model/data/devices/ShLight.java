package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.christian2003.smarthome.model.data.ShInfoText;
import de.christian2003.smarthome.model.data.wrapper_class.ImageWrapper;
import de.christian2003.smarthome.model.data.wrapper_class.OnOffButtonWrapper;
import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.data.wrapper_class.StringUserInformationWrapper;
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
    public ShLight(@NonNull String name, @Nullable String specifier, @Nullable Uri imageUri, @Nullable String onButtonText, @Nullable String offButtonText, @Nullable String milliAmp) {
        super(name, specifier , imageUri);
        this.onButtonText = onButtonText;
        this.offButtonText = offButtonText;
        this.milliAmp = milliAmp;
    }

    /**
     * Finds a single lighting and further properties of it.
     *
     * @param secondDataCell    The second data cell of the table row which contains the lighting.
     * @param lightingName   The name of the lighting.
     * @param specifier     The specifier of the lighting.
     * @return  A {@link RoomDeviceWrapper} which contains the lighting and a list of the warnings that occurred while gathering the information.
     */
    public static RoomDeviceWrapper findSingleLighting(@NonNull Element secondDataCell, @NonNull String lightingName, @NonNull String specifier) {
        ArrayList<UserInformation>  userInformation = new ArrayList<>();

        OnOffButtonWrapper lightingButtonsInformation = findLightingButtons(secondDataCell, specifier);
        if (lightingButtonsInformation.getUserInformation() != null) {
            userInformation.add(lightingButtonsInformation.getUserInformation());
        }

        ImageWrapper imageWrapper = findImage(secondDataCell);
        if (imageWrapper.getUserInformation() != null) {
            userInformation.add(imageWrapper.getUserInformation());
        }

        StringUserInformationWrapper milliAmp = findMilliAmp(secondDataCell);
        if (milliAmp.getUserInformation() != null) {
            userInformation.add(milliAmp.getUserInformation());
        }

        return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShLight(lightingName, specifier, imageWrapper.getImageUri(), lightingButtonsInformation.getOnButton(), lightingButtonsInformation.getOffButton(), milliAmp.getMilliAmp()))), userInformation);
    }

    @NonNull
    public static StringUserInformationWrapper findMilliAmp(@NonNull Element secondDataCell) {
        Element milliAmpNode = secondDataCell.selectFirst("span[id*=mA]");

        if (milliAmpNode != null) {
            return new StringUserInformationWrapper( "(" + milliAmpNode.ownText() + ")", null);
        }
        else {
            String warningDescription = "";
            return new StringUserInformationWrapper(null, new UserInformation(InformationType.INFORMATION, InformationTitle.NoMilliAmpInformation, warningDescription));
        }
    }

    /**
     * Find the image of the element.
     *
     * @param secondDataCell        The second data cell of the table row which contains the lighting.
     * @return      An wrapper which contains the Uri of the image and the user information that occurred while getting the Uri.
     */
    @NonNull
    public static ImageWrapper findImage(@NonNull Element secondDataCell) {
        Element image = secondDataCell.selectFirst("td  img");

        if (image != null) {
            String source = image.attr("src");

            // Check if a source could be found.
            if (!source.isEmpty()){
                return new ImageWrapper(Uri.parse("https://smarthome.de/" + source), null);

            }
            // No image source found for opening but object can be created anyways.
            else {
                String warningDescription = "An image node could be found but not source for the image. The opening could be created anyways. Please check the website and the documentation.";
                return new ImageWrapper(null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
            }
        }
        // No image found for opening but object can be created anyways.
        else {
            String warningDescription = "No image could be found for the opening but it could be created anyways. Please check the website and the documentation.";
            return new ImageWrapper(null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
        }
    }

    /**
     * Extracts the buttons of the lighting element.
     *
     * @param secondDataCell        The second data cell of the table row which contains the opening.
     * @param specifier     The specifier of the lighting.
     * @return      A String array with the content of the first button, the second button and a possible warning that occurred.
     */
    @NonNull
    public static OnOffButtonWrapper findLightingButtons(@NonNull Element secondDataCell, @NonNull String specifier) {
        Element firstButton = secondDataCell.selectFirst("td input[type=button]");

        // Check if the buttons of the lighting could be found.
        if (firstButton != null) {

            String firstButtonText = firstButton.attr("value");
            Element secondButton = secondDataCell.selectFirst("td input[type=button] ~ input[type=button]");

            if (secondButton != null) {

                String secondButtonText = secondButton.attr("value");

                return new OnOffButtonWrapper(firstButtonText, secondButtonText, null);
            }
            // Only one button could be found for the lighting.
            else {
                String warningDescription = "Only one button could be found for the lighting \"" + specifier + "\". Please check the website and the documentation.";
                return new OnOffButtonWrapper(firstButtonText, null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
            }
        }
        // No button could be found for the lighting.
        else {
            String warningDescription = "No buttons could be found for the lighting \"" + specifier + "\". Please check the website and the documentation.";
            return new OnOffButtonWrapper(null, null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
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
