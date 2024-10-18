package de.christian2003.smarthome.model.data;

import android.content.Context;
import android.content.res.Resources;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.christian2003.smarthome.R;

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
        InputStream inputStream = res.openRawResource(R.raw.webpage_code);
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

    public List<ShRoom> getAllRooms() {
        // Get all div-container which contain rooms.
        Elements elements = content.select("body > div.flex-container");

        // Find all the rooms that are located in the div and return them with all their properties.
        /*
        for (Element element: elements) {
            System.out.println("2");
            ShRoom.findRoomInDivContext(element);
        }*/
        ShRoom.findRoomInDivContext(elements.get(0));

        System.out.println("Elements Länge: " + elements.size());
        System.out.println("Element Content: " + elements.get(0).text());
        return null;
    }
}
