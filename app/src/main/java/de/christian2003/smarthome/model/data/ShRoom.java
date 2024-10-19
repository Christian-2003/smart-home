package de.christian2003.smarthome.model.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.christian2003.smarthome.model.data.devices.ShGenericDevice;
import de.christian2003.smarthome.model.data.devices.ShOpening;


/**
 * Class models a room for the smart home.
 */
public class ShRoom {

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

    public static ShRoom findAllRooms(Document document) {
        // Find all rooms of the smart home.
        Elements rooms = document.select("div > div.room");
        System.out.println("Anzahl Rooms: " + rooms.size());

        // Iterates through all rooms and get their properties and devices.
        for (Element room: rooms) {
            Element roomNameEl = findRoomName(room);
            String roomName;

            // Check if a name was found for the room.
            if (roomNameEl != null) {
                roomName = roomNameEl.text();
                System.out.println("Room name: " + roomName);

                // Get the temperature of the room.
                String temperature = findRoomTemperature(room);

                // Get the openings of the room.
                List<ShOpening> openings = ShOpening.findOpenings(room);
            }
            else {
                System.out.println("Element null");
            }
        }
        return null;
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
     * Finds the temperature of the room.
     *
     * @param room          The room element.
     * @return              Returns a String which contains the read temperature or a hyphen if no temperature was found.
     */
    @NonNull
    public static String findRoomTemperature(@NonNull Element room) {
        // Find the data cell in which the temperature is displayed.
        Element temperatureEl = room.select("div > table td[class^=tc] + td").first();

        // Check if a temperature cell could be found.
        if (temperatureEl != null) {
            // Check if multiple temperatures are displayed for different parts of the room or if there is only one temperature.
            Element multipleTemperaturesEl = temperatureEl.select("td > table").first();

            if (multipleTemperaturesEl == null) {
                return temperatureEl.text();
            }
            // If more than one temperature is displayed, the names of the parts of the room and their temperature must be extracted from the table.
            else {
                Elements temperatureRows = multipleTemperaturesEl.select("table > tr");

                if (temperatureRows != null) {
                    return null;
                }
                else {
                    // Display error that table didn´t contain any temperatures.
                    return "-";
                }
            }
        }
        else {
            return "-";
        }
    }
}
