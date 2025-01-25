package de.christian2003.smarthome.data.model.extraction;

import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.CountDownLatch;

/**
 * Handling the parsing of the code of the loaded website to a document.
 */
public class ShWebpageInterface {
    /**
     * The document created based on the html code of the loaded website.
     */
    @Nullable
    private Document document;

    /**
     * Latch to notify when the website is loaded or an error occurred.
     */
    @NonNull
    public CountDownLatch latch;

    /**
     * States if the page was loaded successfully.
     */
    private boolean loadingSuccessful;


    /**
     * Constructor for created an ShWebpageInterface which is used to get the data of the website.
     *
     * @param latch     Latch to notify when the website is loaded or an error occurred.
     */
    public ShWebpageInterface (@NonNull CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * Gets called when the website is loaded and the html is extracted.
     *
     * @param html  The html code that was read from the website.
     */
    @JavascriptInterface
    public void handleHtml(String html) {
        createDocument(html);
    }

    /**
     * Parses the html code of the website and creates a document.
     *
     * @param html  The html code that was read from the website.
     */
    private void createDocument(String html) {
        this.document = Jsoup.parse(html);
    }

    /**
     * Gets the document.
     *
     * @return  The document of the loaded website.
     */
    @Nullable
    public Document getDocument() {
        return document;
    }

    /**
     * Notification when the page and scripts are loaded.
     *
     * @param success   States the success of the loading.
     */
    @JavascriptInterface
    public void notifyPageLoadComplete(boolean success) {
        synchronized (this) {
            if (latch.getCount() > 0) {
                if (success) {
                    loadingSuccessful = true;
                } else {
                    loadingSuccessful = false;
                }
                latch.countDown();
            }
        }
    }

    /**
     * Gets the bool that states if the loading as successful.
     *
     * @return Bool that states if the loading was successful.
     */
    public boolean isLoadingSuccessful() {
        return loadingSuccessful;
    }
}
