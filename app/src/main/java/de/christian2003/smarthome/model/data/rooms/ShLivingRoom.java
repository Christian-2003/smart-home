package de.christian2003.smarthome.model.data.rooms;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import de.christian2003.smarthome.model.data.devices.ShShutter;

public class ShLivingRoom {
    private double temperature;
    private List<ShShutter> shutterList;

    public ShLivingRoom(double temperature, List<ShShutter> shutterList) {
        this.temperature = temperature;
        this.shutterList = shutterList;
    }

    public static void getLivingRoomData(Document document) throws IOException {
        Elements elements = document.selectXpath("//body");

        System.out.println("Elements Länge: " + elements.size());
        System.out.println("Element Content: " + elements.get(0).text());
    }
}
