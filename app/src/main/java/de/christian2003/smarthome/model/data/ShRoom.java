package de.christian2003.smarthome.model.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import de.christian2003.smarthome.model.data.devices.ShGenericDevice;


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
     * Constructor instantiates a new room.
     *
     * @param name      Name for the room.
     * @param infos     List of info texts for the room.
     * @param devices   List of smart home devices for the room.
     */
    public ShRoom(@NonNull String name, @Nullable ArrayList<ShInfoText> infos, @Nullable ArrayList<ShGenericDevice> devices) {
        this.name = name;
        this.infos = infos != null ? infos : new ArrayList<>();
        this.devices = devices != null ? devices : new ArrayList<>();
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
     * Finads all the rooms of the smart home and returns a list containing all of them.
     *
     * @param document          The document with the source code of the webpage.
     * @return                  Returns a list with all the rooms of the smart home. If no rooms were found an empty list will be returned.
     */

    @NonNull
    public static ArrayList<ShRoom> findAllRooms(@NonNull Document document) {
        ArrayList<ShRoom> shRoomList = new ArrayList<>();

        // Find all rooms of the smart home.
        Elements rooms = document.select("div > div.room");
        System.out.println("Anzahl Rooms: " + rooms.size());

        // Iterates through all rooms and get their properties and devices.
        for (Element room: rooms) {
            Element roomNameEl = findRoomName(room);

            // Check if a name was found for the room.
            if (roomNameEl != null) {
                String roomName = roomNameEl.text();
                System.out.println("Room name: " + roomName);

                ArrayList<ShInfoText> shInfoTexts = parseContentTable(room);
                shRoomList.add(new ShRoom(roomName, shInfoTexts, null));
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
     * @param roomName
     * @return
     */
    @Nullable
    public static ArrayList<ShInfoText> parseContentTable(@NonNull Element roomName) {
        Element contentTable = roomName.selectFirst("span.roomName ~ table");

        if (contentTable != null) {
            System.out.println("Content Table gefunden:");
            Elements tableRows = contentTable.select("tr");

            if (!tableRows.isEmpty()) {
                System.out.println("0");
                ArrayList<ShInfoText> shInfoTexts = new ArrayList<>();

                for (Element tableRow: tableRows) {
                    shInfoTexts.addAll(createInfoTexts(tableRow));
                }
                return shInfoTexts;
            }
            else {
                return null;
            }
        }
        else {
            // Room doesn´t contain a content table.
            return null;
        }
    }

    @NonNull
    public static ArrayList<ShInfoText> createInfoTexts(Element tableRow) {
        Element firstDataCell = tableRow.selectFirst("tr > td.temperature");
        System.out.println("1");

        if (firstDataCell != null) {
            System.out.println("FirstDataCell: " + firstDataCell.html());
            Element secondDataCell = tableRow.selectFirst("tr > td[class^=tc] ~ td");
            System.out.println("2");

            if (secondDataCell != null) {
                Element innerTable = secondDataCell.selectFirst("table");
                System.out.println("3");

                if (innerTable != null) {
                    System.out.println("4");
                    return getInnerTableContent(innerTable, firstDataCell.text());
                }
                else {
                    System.out.println("5");
                    return new ArrayList<>(Collections.singletonList(new ShInfoText(firstDataCell.text(), null, secondDataCell.text())));
                }
            }
            else {
                return new ArrayList<>();
            }
        }
        else {
            return new ArrayList<>();
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
    }
}
