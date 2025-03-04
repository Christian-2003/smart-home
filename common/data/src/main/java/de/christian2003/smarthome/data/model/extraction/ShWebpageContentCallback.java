package de.christian2003.smarthome.data.model.extraction;

import androidx.annotation.Nullable;

import de.christian2003.smarthome.data.model.cert.SslTrustResponse;

/**
 *Callback to signal that the website has been loaded.
 */
public interface ShWebpageContentCallback {
    /**
     * Gives information if the website was loaded successfully.
     * @param success           States if the website was loaded successfully.
     * @param sslTrustResponse  SSL trust response.
     */
    void onPageLoadComplete(boolean success, @Nullable SslTrustResponse sslTrustResponse);
}
