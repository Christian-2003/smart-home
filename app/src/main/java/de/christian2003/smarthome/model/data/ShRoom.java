package de.christian2003.smarthome.model.data;

import android.telephony.mbms.MbmsErrors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.christian2003.smarthome.model.data.devices.ShGenericDevice;
import de.christian2003.smarthome.model.data.devices.ShShutter;
import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.data.wrapper_class.RoomInfoTextWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


/**
 * Class models a room for the smart home.
 */
public class ShRoom implements Serializable {

    /**
     * Attribute stores the name of the room.
     */
    @NonNull
    private final String name;

    /**
     * Attribute stores a list of info texts for the room. Exemplary info texts could include
     * temperature, humidity, air pressure, ...
     */
    @NonNull
    private final ArrayList<ShInfoText> infos;

    /**
     * Attribute stores a list of smart home devices for the room, e.g. outlets, openings or lights.
     */
    @NonNull
    private final ArrayList<ShGenericDevice> devices;

    /**
     * Attribute stores a list of all the user information belonging to the room.
     */
    @NonNull
    private final ArrayList<UserInformation> userInformation;


    /**
     * Constructor instantiates a new room.
     *
     * @param name              Name for the room.
     * @param infos             List of info texts for the room.
     * @param devices           List of smart home devices for the room.
     * @param userInformation   List of warnings and errors about the room that should be displayed for the user.
     */
    public ShRoom(@NonNull String name, @Nullable ArrayList<ShInfoText> infos, @Nullable ArrayList<ShGenericDevice> devices, @Nullable ArrayList<UserInformation> userInformation) {
        this.name = name;
        this.infos = infos != null ? infos : new ArrayList<>();
        this.devices = devices != null ? devices : new ArrayList<>();
        this.userInformation = userInformation != null ? userInformation : new ArrayList<>();
    }

    /**
     * Method returns the name of the room.
     *
     * @return  Name of the room.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Method returns a list of info texts for the room.
     *
     * @return  List of info texts for the room.
     */
    @NonNull
    public ArrayList<ShInfoText> getInfos() {
        return infos;
    }

    /**
     * Method returns a list of smart home devices for the room.
     *
     * @return  List of smart home devices for the room.
     */
    @NonNull
    public ArrayList<ShGenericDevice> getDevices() {
        return devices;
    }

    /**
     * Finds all the rooms of the smart home and returns a list containing all of them.
     *
     * @param document          The document with the source code of the webpage.
     * @return                  Returns a list with all the rooms of the smart home. If no rooms were found an empty list will be returned.
     */

    @NonNull
    public static ArrayList<ShRoom> findAllRooms(@NonNull Document document) {
        ArrayList<ShRoom> shRoomList = new ArrayList<>();

        // Find all rooms of the smart home.
        Elements rooms = document.select("div > div.room");
        //System.out.println("Anzahl Rooms: " + rooms.size());

        // Iterates through all rooms and get their properties and devices.
        for (Element room: rooms) {
            Element roomNameEl = findRoomName(room);

            // Check if a name was found for the room.
            if (roomNameEl != null) {
                String roomName = roomNameEl.text();
                //System.out.println("Room name: " + roomName);

                shRoomList.add(parseContentTable(room, roomName));
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
    public static Element findRoomName(@NonNull Element room) {
        return room.select("div > span.roomName").first();
    }

    /**
     * Parses
     *
     * @param room      The element node which contains the name of the room.
     * @return              A list of all the info texts of the room and a list with all warnings/ errors that occurred while gathering the information.
     */
    @NonNull
    public static ShRoom parseContentTable(@NonNull Element room, @NonNull String roomName) {
        Element contentTable = room.selectFirst("span.roomName ~ table");

        if (contentTable != null) {
            //System.out.println("Content Table gefunden:");
            Elements tableRows = contentTable.select("tr");

            if (!tableRows.isEmpty()) {
                //System.out.println("0");
                ArrayList<ShInfoText> shInfoTexts = new ArrayList<>();
                ArrayList<UserInformation> userInformation = new ArrayList<>();
                ArrayList<ShGenericDevice> shGenericDevices = new ArrayList<>();

                for (Element tableRow: tableRows) {
                    Set<String> classNames = tableRow.classNames();
                    if (classNames.contains("temperature")) {
                        RoomInfoTextWrapper roomInformationWrapper = createTemperatureInfoText(tableRow);
                        shInfoTexts.addAll(roomInformationWrapper.getInfoTexts());
                        userInformation.addAll(roomInformationWrapper.getUserInformation());
                    }
                    else if (classNames.contains("shutter")) {
                        System.out.println("Shutter class gefunden");
                        RoomDeviceWrapper roomDeviceWrapper = ShShutter.createShutterDevice(tableRow, roomName);
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

    @NonNull
    public static RoomInfoTextWrapper createTemperatureInfoText(Element tableRow) {
        Element firstDataCell = tableRow.selectFirst("tr > td");
        //System.out.println("1");

        if (firstDataCell != null) {
            //System.out.println("FirstDataCell: " + firstDataCell.html());
            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");
            //System.out.println("2");

            if (secondDataCell != null) {
                Element innerTable = secondDataCell.selectFirst("table");
                //System.out.println("3");

                if (innerTable != null) {
                    //System.out.println("4");
                    return new RoomInfoTextWrapper(getInnerTableContent(innerTable, firstDataCell.text()), new ArrayList<>());
                }
                else {
                    //System.out.println("5");
                    return new RoomInfoTextWrapper(new ArrayList<>(Collections.singletonList(new ShInfoText(firstDataCell.text(), null, secondDataCell.text()))), new ArrayList<>());
                }
            }
            else {
                String warningDescription = "A table row that contains the temperature of the room should be present but could not be found. Please check the website and the documentation.";
                return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else {
            String warningDescription = "";
            return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }


    public static ArrayList<ShInfoText> getInnerTableContent(Element innerTable, String label) {
        Elements innerTableRows = innerTable.select("tr");
        ArrayList<ShInfoText> shInfoTextsInnerTable = new ArrayList<>();

        for (Element innerTableRow: innerTableRows) {
            Element firstDataCell = innerTableRow.selectFirst("td");

            if (firstDataCell != null) {
                Element secondDataCell = innerTable.selectFirst("td ~ td");

                if (secondDataCell != null) {
                    shInfoTextsInnerTable.add(new ShInfoText(label, firstDataCell.text(), secondDataCell.text()));
                }
            }
        }
        return shInfoTextsInnerTable;
    }

    /**
     * Method prints the properties of a ShRoom object.
     *
     * @param room      The room object which properties should be printed.
     */
    public static void printOutRoom(ShRoom room) {
        System.out.println("Room name: " + room.name + "Länge Infos: " + room.infos.size());
        for (ShInfoText shInfoText: room.infos) {
            System.out.println("\tLabel: " + shInfoText.getLabel() + ", Specifier " + shInfoText.getSpecifier() + ", Text: " + shInfoText.getText());
        }
        for (ShGenericDevice shGenericDevice: room.devices) {
            if (shGenericDevice instanceof ShShutter) {
                System.out.println("\tShutter Name: " + shGenericDevice.getName() + ", Specifier: " + ((ShShutter) shGenericDevice).getSpecifier() + ", ButtonText: " + ((ShShutter) shGenericDevice).getSetButtonText() + ", Percentage: " + ((ShShutter) shGenericDevice).getPercentage() + ", Time: " + ((ShShutter) shGenericDevice).getTime());
            }
        }
    }
}
