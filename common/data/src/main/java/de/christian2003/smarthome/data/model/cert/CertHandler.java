package de.christian2003.smarthome.data.model.cert;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


/**
 * Class implements a handler that can save a client certificate (and password) through
 * {@link #importNewCertFromUri(Uri, String)}. Get an {@linkplain SSLContext} for all requests through
 * {@link #getSSLContext()} which includes the client certificate.
 */
public class CertHandler {

    /**
     * Field stores the tag to use for logging.
     */
    private static final String TAG = "CertHandler";


    /**
     * Attribute stores the key store helper.
     */
    private final KeyStoreHelper helper;


    /**
     * Constructor instantiates a new cert handler.
     *
     * @param context   Context for the cert handler.
     */
    public CertHandler(Context context) {
        helper = new KeyStoreHelper(context);
    }


    /**
     * Method copies the .pfx certificate of the specified URI to the internal storage and saves
     * the provided password for the certificate.
     *
     * @param uri           URI to the .pfx-file to copy to internal storage.
     * @param certPassword  The password of the certificate.
     * @throws Exception    Some error occurred.
     */
    public void importNewCertFromUri(@NonNull Uri uri, String certPassword) throws Exception {
        helper.saveCertPassword(certPassword);
        helper.saveCertToInternalStorage(uri);
    }


    /**
     * Method returns the SSLContext to use for connections using the custom client certificate. If
     * no client certificate is available or any other error occurs, an exception is thrown.
     *
     * @return              SSLContext with the client certificate to use with requests to the server.
     * @throws Exception    Some error occurred.
     */
    public SSLContext getSSLContext() throws Exception {
        File certFile = helper.getCertFile();
        String password = helper.getCertPassword();

        if (!certFile.exists() || password == null) {
            Log.e(TAG, "Certificate or password not available");
            throw new IllegalStateException("Certificate or password not available");
        }

        try (InputStream certStream = Files.newInputStream(certFile.toPath())) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certStream, password.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, password.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init((KeyStore)null); //I have no idea why I have to cast "null" to "KeyStore"...

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Unknown error occurred");
            throw e;
        }
    }


    /**
     * Method removes the certificate and the corresponding password from the app.
     *
     * @throws Exception    The file cannot be deleted.
     */
    public void removeCert() throws Exception {
        helper.removeCertPassword();
        File certFile = helper.getCertFile();
        boolean removed;
        if (certFile.exists()) {
            try {
                removed = certFile.delete();
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Unknown error occurred");
                throw e;
            }
            if (!removed) {
                Log.e(TAG, "Cannot remove certificate file");
                throw new Exception("Cannot remove certificate file");
            }
        }
    }

}
