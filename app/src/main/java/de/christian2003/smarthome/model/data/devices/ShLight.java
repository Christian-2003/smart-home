package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.christian2003.smarthome.model.data.wrapper_class.ImageWrapper;
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
    public ShLight(@NonNull String name, @Nullable Uri imageUri, @Nullable String onButtonText, @Nullable String offButtonText, @Nullable String milliAmp) {
        super(name, null, imageUri);
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
    public RoomDeviceWrapper findSingleLighting(@NonNull Element secondDataCell, @NonNull String lightingName, @NonNull String specifier) {
        ArrayList<UserInformation>  userInformation = new ArrayList<>();

        String[] lightingButtonsInformation = findLightingButtons(secondDataCell, specifier);

        ImageWrapper imageWrapper = findImage(secondDataCell);
        if (imageWrapper.getUserInformation() != null) {
            userInformation.add(imageWrapper.getUserInformation());
        }

        String milliAmp = findMilliAmp(secondDataCell);


        return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShLight(lightingName, imageWrapper.getImageUri(), lightingButtonsInformation[0], lightingButtonsInformation[1], milliAmp))), userInformation);
    }

    @Nullable
    public static String findMilliAmp(Element secondDataCell) {
        Element milliAmpNode = secondDataCell.selectFirst("span[id*=mA]");

        if (milliAmpNode != null) {
            return "(" + milliAmpNode.ownText() + ")";
        }
        else {
            return null;
        }
    }

    /**
     * Find the image of the element.
     *
     * @param secondDataCell        The second data cell of the table row which contains the lighting.
     * @return      An wrapper which contains the Uri of the image and the user information that occurred while getting the Uri.
     */
    @NonNull
    public static ImageWrapper findImage(Element secondDataCell) {
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
    public static String[] findLightingButtons(Element secondDataCell, String specifier) {
        Element firstButton = secondDataCell.selectFirst("td input[button]");

        // Check if the buttons of the lighting could be found.
        if (firstButton != null) {
            String firstButtonText = firstButton.attr("value");
            Element secondButton = secondDataCell.selectFirst("td input[button] ~ input[button]");

            if (secondButton != null) {
                String secondButtonText = secondButton.attr("value");

                return new String[]{firstButtonText, secondButtonText, null};
            }
            // Only one button could be found for the lighting.
            else {
                return new String[]{firstButtonText, null, "Only one button could be found for the lighting \"" + specifier + "\". Please check the website and the documentation."};
            }
        }
        // No button could be found for the lighting.
        else {
            return new String[]{null, null, "No buttons could be found for the lighting \"" + specifier + "\". Please check the website and the documentation."};
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
