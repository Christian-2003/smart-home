package de.christian2003.smarthome.data.model.extraction.search.devices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
     * Find a single unknown device.
     * @param tableRow  The table row which contains the cells with the unknown device.
     * @param roomName  The name of the room in which the device is located.
     * @return      A {@link RoomDeviceWrapper} which contains the unknown device and a list of the warnings that occurred while gathering the information.
     */
    @NonNull
    public static RoomDeviceWrapper findUnknownDevice(@NonNull Element tableRow, @NonNull String roomName) {
        Element firstDataCell = tableRow.selectFirst("td");

        if (firstDataCell != null) {
            String name = firstDataCell.ownText() + " " + roomName;
            Element secondDataCell = tableRow.selectFirst("td ~ td");

            if (secondDataCell != null) {
                return ShUnknownDeviceSearch.gatherUnknownDeviceProperties(secondDataCell, name, null, null);
            }
            else {
                String warningDescription = "No second table row with that should contain the properties of the " + name + " could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShUnknownDevice(name, null, null, null, null, null, null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));

            }
        }
        else {
            String warningDescription = "An expected element in the room " + roomName + " was not found.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));

        }
    }

    /**
     * Find the properties of an unknown device.
     *
     * @param secondDataCell    The second data cell of the table row which contains the lighting.
     * @param name   The name of the unknown device.
     * @param hourDataCell  The data cell which contains the hours.
     * @param whDataCell    The data cell which contains the wh.
     *
     * @return  A {@link RoomDeviceWrapper} which contains the unknown device and a list of the warnings that occurred while gathering the information.
     */
    @NonNull
    public static RoomDeviceWrapper gatherUnknownDeviceProperties(@NonNull Element secondDataCell, @NonNull String name, @Nullable Element hourDataCell, @Nullable Element whDataCell) {
        OnOffButtonWrapper buttons = ShLightSearch.findLightingButtons(secondDataCell, "");
        StringUserInformationWrapper milliAmp = ShLightSearch.findMilliAmp(secondDataCell);
        StringUserInformationWrapper imageUriWrapper = ShLightSearch.findImage(secondDataCell);

        String informationDescription = "This is not a standard element. Some of its properties might be missing.";
        return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShUnknownDevice(name, imageUriWrapper.getProperty(), buttons.getOnButton(), buttons.getOffButton(), milliAmp.getProperty(), ShLightSearch.findHours(hourDataCell), ShLightSearch.findWh(whDataCell)))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.INFORMATION, InformationTitle.UnknownElement, informationDescription))));
    }
}
