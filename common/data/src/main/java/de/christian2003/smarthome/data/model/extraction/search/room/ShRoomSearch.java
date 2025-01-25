package de.christian2003.smarthome.data.model.extraction.search.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import de.christian2003.smarthome.data.model.devices.ShGenericDevice;
import de.christian2003.smarthome.data.model.devices.ShLight;
import de.christian2003.smarthome.data.model.devices.ShOpening;
import de.christian2003.smarthome.data.model.devices.ShShutter;
import de.christian2003.smarthome.data.model.extraction.search.devices.ShOpeningSearch;
import de.christian2003.smarthome.data.model.extraction.search.devices.ShShutterSearch;
import de.christian2003.smarthome.data.model.room.ShInfoText;
import de.christian2003.smarthome.data.model.room.ShRoom;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;
import de.christian2003.smarthome.data.model.wrapper.RoomDeviceWrapper;
import de.christian2003.smarthome.data.model.wrapper.RoomInfoTextWrapper;

/**
 * Class models a search for a room for the smart home.
 */
public class ShRoomSearch implements Serializable {

    /**
     * Constructor instantiates a new room search object.
     */
    public ShRoomSearch() {

    }

    /**
     * Finds all the rooms of the smart home and returns a list containing all of them.
     *
     * @param document          The document with the source code of the webpage.
     * @return                  Returns a list with all the rooms of the smart home. If no rooms were found an empty list will be returned.
     */

    @NonNull
    public ArrayList<ShRoom> findAllRooms(@NonNull Document document) {
        boolean overallStatus = false;
        ArrayList<ShRoom> shRoomList = new ArrayList<>();

        // Find all rooms of the smart home.
        Elements rooms = document.select("div.room");

        // Iterates through all rooms and get their properties and devices.
        for (Element room: rooms) {
            Element roomNameEl = findRoomName(room);

            // Check if a name was found for the room.
            if (roomNameEl != null) {
                String roomName = roomNameEl.text();
                if (!overallStatus && roomName.toLowerCase().contains("gesamtstatus")) {
                    overallStatus = true;
                    shRoomList.add(0, parseContentTable(room, roomName));
                }
                else {
                    shRoomList.add(parseContentTable(room, roomName));
                }
            }
            else {
                // Div container with the class "room" was found but not title of the room could be found.
            }
        }
        return shRoomList;
    }

    /**
     * Finds the node in the html code which contains the name of the room.
     *
     * @param room          The room element.
     * @return              Returns the elements which contains the name of the room.
     */
    @Nullable
    public Element findRoomName(@NonNull Element room) {
        return room.select("div span.roomName").first();
    }

    /**
     * Parses the elements that are in the content table of a room.
     *
     * @param room          The element node which contains the name of the room.
     * @param roomName      The name of the room.
     * @return              A list of all the info texts of the room and a list with all warnings/ errors that occurred while gathering the information.
     */
    @NonNull
    public ShRoom parseContentTable(@NonNull Element room, @NonNull String roomName) {

        // Get the content table and its elements.
        Element contentTable = room.selectFirst("table");
        if (contentTable != null) {
            Elements tableRows = contentTable.select("tr");

            if (!tableRows.isEmpty()) {
                ArrayList<ShInfoText> shInfoTexts = new ArrayList<>();
                ArrayList<UserInformation> userInformation = new ArrayList<>();
                ArrayList<ShGenericDevice> shGenericDevices = new ArrayList<>();

                // Find the different info texts and devices of the room.
                for (Element tableRow: tableRows) {
                    Set<String> classNames = tableRow.classNames();
                    if (classNames.contains("infoText")) {
                        RoomInfoTextWrapper roomInformationWrapper = ShInfoTextSearch.createInfoText(tableRow);
                        shInfoTexts.addAll(roomInformationWrapper.getInfoTexts());
                        userInformation.addAll(roomInformationWrapper.getUserInformation());
                    }
                    else if (classNames.contains("shutter")) {
                        RoomDeviceWrapper roomDeviceWrapper = ShShutterSearch.createShutterDevice(tableRow, roomName);
                        shGenericDevices.addAll(roomDeviceWrapper.getDevices());
                        userInformation.addAll(roomDeviceWrapper.getUserInformation());
                    }
                    else if (classNames.contains("opening")) {
                        RoomDeviceWrapper roomDeviceWrapper = ShOpeningSearch.createOpeningDevice(tableRow, roomName);
                        shGenericDevices.addAll(roomDeviceWrapper.getDevices());
                        userInformation.addAll(roomDeviceWrapper.getUserInformation());
                    }
                    else if (classNames.contains("status")) {
                        RoomDeviceWrapper roomDeviceWrapper = ShStatusSearch.gatherStatusContent(tableRow, roomName);
                        shGenericDevices.addAll(roomDeviceWrapper.getDevices());
                        userInformation.addAll(roomDeviceWrapper.getUserInformation());
                    }
                }
                return new ShRoom(roomName, shInfoTexts, shGenericDevices, userInformation);
            }
            // Content table was found but it doesn´t contain any rows with information.
            else {
                String warningDescription = "A room was found but no table containing further information to the room could be found. Please check the code of the website and the documentation.";
                return new ShRoom(roomName, null, null, new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        // Room doesn´t contain a content table.
        else {
            String warningDescription = "A room was found but no table containing further information to the room could be found. Please check the code of the website and the documentation.";
            return new ShRoom(roomName, null, null, new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Method prints the properties of a ShRoom object.
     *
     * @param room      The room object which properties should be printed.
     */
    public static void printOutRoom(@NonNull ShRoom room) {
        System.out.println("Room name: " + room.getName() + "Länge Infos: " + room.getInfos().size());
        for (ShInfoText shInfoText: room.getInfos()) {
            System.out.println("\tLabel: " + shInfoText.getLabel() + ", Specifier " + shInfoText.getSpecifier() + ", Text: " + shInfoText.getText());
        }
        for (ShGenericDevice shGenericDevice: room.getDevices()) {
            if (shGenericDevice instanceof ShShutter) {
                System.out.println("\tShutter Name: " + shGenericDevice.getName() + ", Specifier: " + ((ShShutter) shGenericDevice).getSpecifier() + ", ButtonText: " + ((ShShutter) shGenericDevice).getSetButtonText() + ", Percentage: " + ((ShShutter) shGenericDevice).getPercentage() + ", Time: " + ((ShShutter) shGenericDevice).getTime());
            }
            if (shGenericDevice instanceof ShOpening) {
                System.out.println("\tOpening Name: " + shGenericDevice.getName() + ", Specifier: " + ((ShOpening) shGenericDevice).getSpecifier() + ", ImageUri: " + (shGenericDevice).getImageUri() + ", Type: " + ((ShOpening) shGenericDevice).getOpeningType() + ", Time: ");
            }
            if (shGenericDevice instanceof ShLight) {
                System.out.println("\tLight Name: " + shGenericDevice.getName() + ", Specifier: " + ((ShLight) shGenericDevice).getSpecifier() + ", ImageUri: " + (shGenericDevice).getImageUri() + ", OnButton: " + ((ShLight) shGenericDevice).getOnButtonText() + ", OffButton: " + ((ShLight) shGenericDevice).getOffButtonText() + ", MilliAmp: " + ((ShLight) shGenericDevice).getMilliAmp());
            }
        }
    }
}
