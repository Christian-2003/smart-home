package de.christian2003.smarthome.model.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static Document getWebpageHtml() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

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

        return document;
    }
}
