package de.christian2003.smarthome.model.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import de.christian2003.smarthome.model.data.devices.ShGenericDevice;


/**
 * Class models a room for the smart home.
 */
public class ShRoom {

    /**
     * Attribute stores the name of the room.
     */
    @Nullable
    private final String name;

    /**
     * Attribute stores the temperature of the room.
     */
    @Nullable
    private final String temperature;

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
    public ShRoom(@Nullable String name, @Nullable String temperature, @Nullable ArrayList<ShInfoText> infos, @Nullable ArrayList<ShGenericDevice> devices) {
        this.name = name;
        this.temperature = temperature;
        this.infos = infos != null ? infos : new ArrayList<>();
        this.devices = devices != null ? devices : new ArrayList<>();
    }

    /**
     * Method returns the name of the room.
     *
     * @return  Name of the room.
     */
    @Nullable
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

    public static ShRoom findRoomInDivContext(Element element) {
        // Find all rooms of the div-container that was passed.
        Elements rooms = element.select("div > div[class^=c]");

        for (Element room: rooms) {
            Element roomNameEl = findRoomName(room);
            String roomName;

            // Check if a name was found for the room.
            if (roomNameEl != null) {
                roomName = roomNameEl.ownText();

                // Get the temperature of the room.
                String temperature = findRoomTemperature(room);
                System.out.println("Raum: " + roomName + ", Temperatur: " + temperature);

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
    public static Element findRoomName(Element room) {
        return room.select("div").first();
    }

    /**
     * Finds the temperature of the room.
     *
     * @param room          The room element.
     * @return              Returns a String which contains the read temperature or a hyphen if no temperature was found.
     */
    public static String findRoomTemperature(Element room) {
        Element temperatureEl = room.select("div > table td[class^=tc] + td").first();

        if (temperatureEl != null) {
            return temperatureEl.text();
        }
        else {
            return "-";
        }

    }

}
