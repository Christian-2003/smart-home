package de.christian2003.smarthome.model.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.Config;
import de.christian2003.smarthome.model.data.devices.ShGenericDevice;
import de.christian2003.smarthome.model.data.devices.ShOpening;
import de.christian2003.smarthome.model.data.devices.ShShutter;

/**
 * Class models the content of the smart home webpage.
 */
public class ShWebpageContent {

    /**
     * The document which contains the code of the smart home webpage.
     */
    private final Document content;

    private final ArrayList<ShRoom> rooms;

    /**
     * Constructor instantiates a new webpage content.
     *
     * @param content           Content of the read webpage.
     * @param rooms             List of all the rooms that are display on the smart home webpage.
     */
    private ShWebpageContent(Document content, ArrayList<ShRoom> rooms) {
        this.content = content;
        this.rooms = rooms;
    }

    @NonNull
    public ArrayList<ShRoom> getRooms() {
        return rooms;
    }

    /**
     * Gets the data of the webpage and loads all the rooms.
     *
     * @return          Returns a ShWebpageContent object or null if the webpage content could not be loaded.
     */
    @Nullable
    public static ShWebpageContent getWebpageHtml(Context context) throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // The code that will be used when we have access to the webpage.
        /*
        Callable<Document> task = () -> {
            try {
                // Get the code from the webpage.
                return Jsoup.connect(Config.getInstance().getServerUrl(null)).get();
            }
            catch (Exception exception) {
                System.out.println("Exception: " + exception.getMessage());
                return null;
            }
        };

        // Submit the task to the executor and get a Future representing the result.
        Future<Document> future = executor.submit(task);
        Document document = null;

        // Try to retrieve the result of the asynchronous task and shut down the executor.
        try {
            document = future.get();
        }
        catch (Exception exception) {
            System.out.println("Exception while getting document: " + exception.getMessage());
        }
        finally {
            executor.shutdown();
        }
        */
        Resources res = context.getResources();
        InputStream inputStream = res.openRawResource(R.raw.webpage_code_updated);
        String html = convertStreamToString(inputStream);
        inputStream.close();
        Document document = Jsoup.parse(html);

        if (document != null) {
            ArrayList<ShRoom> rooms = ShRoom.findAllRooms(document);
            return new ShWebpageContent(document, rooms);
        }
        else {
            return null;
        }
    }

    /**
     * Temporary method to read the html that contains the smart home webpage hrml code.
     *
     * @param is
     * @return Returns the html code of the webpage.
     * @throws IOException
     */
    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    /*
    public ArrayList<ShRoom> getDummyData() {
        // Dummy InfoTexts.
        ShInfoText infoText1 = new ShInfoText("Temperature", null,"30");
        ShInfoText infoText2 = new ShInfoText("Humidity", "Flur", "90");
        ShInfoText infoText3 = new ShInfoText("Air pressure", null,"80");
        ShInfoText infoText4 = new ShInfoText("Temperature", "WZ" ,"30");
        ShInfoText infoText5 = new ShInfoText("Temperature", "Flur","23");
        ShInfoText infoText6 = new ShInfoText("Temperature", null,"12");

        ArrayList<ShInfoText> infoTextArrayList1 = new ArrayList<>();
        infoTextArrayList1.add(infoText1);
        infoTextArrayList1.add(infoText2);
        infoTextArrayList1.add(infoText3);

        ArrayList<ShInfoText> infoTextArrayList2 = new ArrayList<>();
        infoTextArrayList2.add(infoText4);

        ArrayList<ShInfoText> infoTextArrayList3 = new ArrayList<>();
        infoTextArrayList3.add(infoText5);

        ArrayList<ShInfoText> infoTextArrayList4 = new ArrayList<>();
        infoTextArrayList4.add(infoText6);

        // Dummy devices.
        ShGenericDevice device1 = new ShOpening("Window1", null);
        ShGenericDevice device2 = new ShOpening("Window2", null);

        ShGenericDevice device3 = new ShShutter("Shutter1", null, "90", "9");

        ArrayList<ShGenericDevice> deviceArrayList1 = new ArrayList<>();
        deviceArrayList1.add(device1);
        deviceArrayList1.add(device2);

        ArrayList<ShGenericDevice> deviceArrayList2 = new ArrayList<>();
        deviceArrayList2.add(device3);

        // Dummy list of rooms for testing.
        ArrayList<ShRoom> dummyRooms = new ArrayList<>();
        dummyRooms.add(new ShRoom("Test-Room1",  null, deviceArrayList2));
        dummyRooms.add(new ShRoom("Test-Room2", infoTextArrayList1, null));
        dummyRooms.add(new ShRoom("Test-Room3", infoTextArrayList2, deviceArrayList1));
        dummyRooms.add(new ShRoom("Test-Room4", infoTextArrayList3, null));
        dummyRooms.add(new ShRoom("Test-Room5", infoTextArrayList4, null));
        return dummyRooms;
    }*/

    /**
     * Prints all the rooms and their properties that belong to the ShWebpageContent object.
     *
     * @param shWebpageContent          The object which has attribute that contains all the rooms of the smart home and their properties.
     */
    public static void printElement(@NonNull ShWebpageContent shWebpageContent) {
        System.out.println("Länge: " + shWebpageContent.rooms.size());
        for (ShRoom room: shWebpageContent.rooms) {
            ShRoom.printOutRoom(room);
        }
    }
}
