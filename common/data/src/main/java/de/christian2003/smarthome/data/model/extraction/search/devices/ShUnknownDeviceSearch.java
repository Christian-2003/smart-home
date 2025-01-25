package de.christian2003.smarthome.data.model.extraction.search.devices;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.data.model.devices.ShGenericDevice;
import de.christian2003.smarthome.data.model.devices.ShUnknownDevice;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;
import de.christian2003.smarthome.data.model.wrapper.OnOffButtonWrapper;
import de.christian2003.smarthome.data.model.wrapper.RoomDeviceWrapper;
import de.christian2003.smarthome.data.model.wrapper.StringUserInformationWrapper;

/**
 * Class models a search for an unknown devices for the smart home.
 */
public class ShUnknownDeviceSearch {

    /**
     * Finds a single unknown device and further properties of it.
     *
     * @param secondDataCell    The second data cell of the table row which contains the lighting.
     * @param name   The name of the unknown device.
     *
     * @return  A {@link RoomDeviceWrapper} which contains the unknown device and a list of the warnings that occurred while gathering the information.
     */
    @NonNull
    public static RoomDeviceWrapper findUnknownDevice(@NonNull Element secondDataCell, @NonNull String name) {
        OnOffButtonWrapper buttons = ShLightSearch.findLightingButtons(secondDataCell, "");
        StringUserInformationWrapper milliAmp = ShLightSearch.findMilliAmp(secondDataCell);
        StringUserInformationWrapper imageUriWrapper = ShLightSearch.findImage(secondDataCell);

        String informationDescription = "This is not a standard element. Some of its properties might be missing.";
        return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShUnknownDevice(name, imageUriWrapper.getProperty(), buttons.getOnButton(), buttons.getOffButton(), milliAmp.getProperty()))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.INFORMATION, InformationTitle.UnknownElement, informationDescription))));
    }
}
