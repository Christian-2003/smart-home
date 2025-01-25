package de.christian2003.smarthome.data.model.extraction.search.devices;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.data.model.devices.ShLight;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;
import de.christian2003.smarthome.data.model.wrapper.OnOffButtonWrapper;
import de.christian2003.smarthome.data.model.wrapper.RoomDeviceWrapper;
import de.christian2003.smarthome.data.model.wrapper.StringUserInformationWrapper;

/**
 * Class models a search for a light for the smart home.
 */

public class ShLightSearch {
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

        StringUserInformationWrapper imageWrapper = findImage(secondDataCell);
        if (imageWrapper.getUserInformation() != null) {
            userInformation.add(imageWrapper.getUserInformation());
        }

        StringUserInformationWrapper milliAmp = findMilliAmp(secondDataCell);
        if (milliAmp.getUserInformation() != null) {
            userInformation.add(milliAmp.getUserInformation());
        }

        return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShLight(lightingName, specifier, imageWrapper.getProperty(), lightingButtonsInformation.getOnButton(), lightingButtonsInformation.getOffButton(), milliAmp.getProperty()))), userInformation);
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
    public static StringUserInformationWrapper findImage(@NonNull Element secondDataCell) {
        Element image = secondDataCell.selectFirst("td  img");

        if (image != null) {
            String source = image.attr("src");

            // Check if a source could be found.
            if (!source.isEmpty()){
                return new StringUserInformationWrapper(source, null);

            }
            // No image source found for opening but object can be created anyways.
            else {
                String warningDescription = "An image node could be found but not source for the image. The opening could be created anyways. Please check the website and the documentation.";
                return new StringUserInformationWrapper(null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
            }
        }
        // No image found for opening but object can be created anyways.
        else {
            String warningDescription = "No image could be found for the opening but it could be created anyways. Please check the website and the documentation.";
            return new StringUserInformationWrapper(null, new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription));
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
}
