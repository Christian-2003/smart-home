package de.christian2003.smarthome.model.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.christian2003.smarthome.R;
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

    /**
     * Constructor instantiates a new webpage content.
     *
     * @param content           Content of the read webpage.
     * @throws Exception        Exception that will be thrown if the content of the webpage is null.
     */
    public ShWebpageContent(Document content) throws Exception {
        if (content != null) {
            this.content = content;
        }
        else {
            throw new Exception();
        }
    }

    /**
     * Gets the data of the webpage.
     *
     * @return          Returns the document which contains the code of the webpage.
     *                  Null if the content could not be retrieved due to an error.
     */
    public static Document getWebpageHtml(Context context) throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // The code that will be used when we have access to the webpage.
        /*
        Callable<Document> task = () -> {
            try {
                // Get the code from the webpage.
                return Jsoup.connect("https://www.google.com").get();
            }
            catch (Exception exception) {
                System.out.println("Exception: " + exception.getMessage());
                return null;
            }
        };

        // Submit the task to the executor and get a Future representing the result
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

        // Temporary code to get the html code from the webpage_code.html file.
        Resources res = context.getResources();
        InputStream inputStream = res.openRawResource(R.raw.webpage_code_updated);
        String html = convertStreamToString(inputStream);
        inputStream.close();
        Document document = Jsoup.parse(html);

        return document;
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

    public ArrayList<ShRoom> getAllShData() {
        // Find all the rooms that are located in the body of the webpage.
        ShRoom.findAllRooms(content);

        // Dummy InfoTexts.
        ShInfoText infoText1 = new ShInfoText("Temperature", "30", null);
        ShInfoText infoText2 = new ShInfoText("Humidity", "90", Color.valueOf(78));
        ShInfoText infoText3 = new ShInfoText("Air pressure", "80", null);
        ShInfoText infoText4 = new ShInfoText("Temperature", "30", null);
        ShInfoText infoText5 = new ShInfoText("Temperature", "23", null);
        ShInfoText infoText6 = new ShInfoText("Temperature", "12", null);

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
    }
}
