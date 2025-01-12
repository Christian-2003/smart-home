package de.christian2003.smarthome.data.model.extraction;

/**
 *Callback to signal that the website has been loaded.
 */
public interface ShWebpageContentCallback {
    /**
     * Gives information if the website was loaded successfully.
     * @param success       States if the website was loaded successfully.
     */
    void onPageLoadComplete(boolean success);
}
