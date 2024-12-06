package de.christian2003.smarthome.view.cert;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import de.christian2003.smarthome.model.cert.CertHandler;


/**
 * Class implements the view model for the {@link CertActivity}.
 */
public class CertViewModel extends ViewModel {

    /**
     * Attribute stores the URI of the .pfx-file selected by the user. This is {@code null} if no
     * URI is entered.
     */
    @Nullable
    private Uri certUri;

    /**
     * Attribute stores the handler for certificates.
     */
    private final CertHandler handler;


    /**
     * Constructor instantiates a new view model.
     */
    public CertViewModel() {
        handler = new CertHandler();
    }


    /**
     * Method changes the URI for the .pfx-file selected by the user.
     *
     * @param certUri   URI for the .pfx-file selected by the user.
     */
    public void setCertUri(@Nullable Uri certUri) {
        this.certUri = certUri;
    }


    /**
     * Method saves the certificate whose uri was passed beforehand using {@link #setCertUri(Uri)}
     * with the specified password. If the certificate cannot be saved, an existing certificate
     * will be deleted.
     *
     * @param password  Password for the certificate.
     */
    public void saveCertificate(String password) {
        boolean saved = false;
        if (certUri != null && password != null && !password.isEmpty()) {
            try {
                handler.importNewCertFromUri(certUri, password);
                certUri = null;
                saved = true;
            }
            catch (Exception e) {
                //Ignore...
            }
        }
        if (!saved) {
            try {
                handler.removeCert();
            }
            catch (Exception e) {
                //Ignore...
            }
        }
    }


    /**
     * Method removes the certificate.
     *
     * @return  Whether the certificate was removed.
     */
    public boolean removeCert() {
        try {
            handler.removeCert();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * Method returns whether a valid certificate exists.
     *
     * @return  Whether a certificate exists.
     */
    public boolean certExists() {
        try {
            return handler.getSSLContext() != null;
        }
        catch (Exception e) {
            return false;
        }
    }

}
