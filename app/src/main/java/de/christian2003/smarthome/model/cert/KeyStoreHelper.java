package de.christian2003.smarthome.model.cert;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import de.christian2003.smarthome.App;


/**
 * Class implements a few helper methods to load and save the certificate and password to use for
 * client authentication.
 */
public class KeyStoreHelper {

    /**
     * Attribute stores the tag to use for logging.
     */
    private static final String TAG = "KeyStoreHelper";

    /**
     * Attribute stores the alias for the password. This alias is used to store the AES key for
     * password encryption / decryption.
     */
    private static final String PASSWORD_ALIAS = "cert_password_alias";

    /**
     * Attribute stores the name of the keystore.
     */
    private static final String KEYSTORE = "AndroidKeyStore";

    /**
     * Attribute stores the key with which to store the IV within shared preferences.
     */
    private static final String KEY_CERT_IV = "cert_iv";

    /**
     * Attribute stores the key with which to store the password within shared preferences.
     */
    private static final String KEY_CERT_PASSWORD = "cert_password";

    /**
     * Attribute stores the name of the shared preferences.
     */
    private static final String PREFERENCES = "smart_home";

    /**
     * Attribute stores the name of the user certificate within the internal app storage.
     */
    private static final String CERT_NAME = "user_cert.pfx";


    /**
     * Method saves the specified password to use with the client certificate for authentication.
     *
     * @param password      Password to save.
     * @throws Exception    Some error occurred.
     */
    public void saveCertPassword(@NonNull String password) throws Exception {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);

            //Create new AES key to encrypt password, if none already exists within the key store:
            if (!keyStore.containsAlias(PASSWORD_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(PASSWORD_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());
                keyGenerator.generateKey();
            }

            //Encrypt password:
            SecretKey secretKey = (SecretKey)keyStore.getKey(PASSWORD_ALIAS, null);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getIV();
            byte[] encryptedPassword = cipher.doFinal(password.getBytes());

            //Save password:
            SharedPreferences preferences = App.getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_CERT_IV, Base64.encodeToString(iv, Base64.DEFAULT));
            editor.putString(KEY_CERT_PASSWORD, Base64.encodeToString(encryptedPassword, Base64.DEFAULT));
            editor.apply();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Unknown error occurred while saving password");
            throw e;
        }
    }


    /**
     * Method returns the password to use with the client certificate for authentication.
     *
     * @return              Password. This is {@code null} if no password was saved beforehand.
     * @throws Exception    Some error occurred.
     */
    @Nullable
    public String getCertPassword() throws Exception {
        try {
            //Load password and IV:
            SharedPreferences preferences = App.getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String ivString = preferences.getString(KEY_CERT_IV, null);
            String encryptedPasswordString = preferences.getString(KEY_CERT_PASSWORD, null);

            if (ivString == null || encryptedPasswordString == null) {
                return null;
            }

            byte[] iv = Base64.decode(ivString, Base64.DEFAULT);
            byte[] encryptedPassword = Base64.decode(encryptedPasswordString, Base64.DEFAULT);

            KeyStore keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);

            //Load key for decryption and decrypt password:
            SecretKey secretKey = (SecretKey)keyStore.getKey(PASSWORD_ALIAS, null);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            byte[] decryptedPassword = cipher.doFinal(encryptedPassword);

            return new String(decryptedPassword);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Unknown error occurred while getting password");
            throw e;
        }
    }


    /**
     * Method removes the certificate password.
     */
    public void removeCertPassword() {
        SharedPreferences preferences = App.getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_CERT_IV);
        editor.remove(KEY_CERT_PASSWORD);
        editor.apply();
    }


    /**
     * Method copies the .pfx certificate from the specified URI to the internal app storage.
     *
     * @param uri           URI of the .pfx-file to copy.
     * @throws Exception    Some error occurred.
     */
    public void saveCertToInternalStorage(@NonNull Uri uri) throws Exception {
        File certFile = getCertFile();
        try (InputStream is = App.getContext().getContentResolver().openInputStream(uri); OutputStream os = Files.newOutputStream(certFile.toPath())) {
            Log.d(TAG, "Uri=" + uri);
            Log.d(TAG, "is==null? " + (is == null));
            Log.d(TAG, "os==null? " + (os == null));
            if (is == null) {
                Log.e(TAG, "Input stream cannot be constructed from URI " + uri);
                throw new IllegalStateException("Input stream cannot be constructed from URI " + uri);
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                Log.d(TAG, "Read " + bytesRead + "bytes from pfx file");
                os.write(buffer, 0, bytesRead);
            }
            Log.d(TAG, "Finished copying pfx file");
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Unknown error occurred while saving certificate");
            throw e;
        }
    }


    /**
     * Method returns the file in which the certificate is stored within the internal app storage.
     * If no certificate was stored beforehand, the file returned does not exist.
     *
     * @return  File containing the certificate.
     */
    public File getCertFile() {
        return new File(App.getContext().getFilesDir(), CERT_NAME);
    }

}
