package de.christian2003.smarthome.data.model.cert

import android.content.Context
import android.security.KeyChain
import android.util.Log
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class CertHandler(

    /**
     * Attribute stores the context to use for all operations.
     */
    val context: Context

) {

    /**
     * Attribute stores the preferences in which the certificate alias is stored.
     */
    private val preferences = context.getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Method returns the SSL context to use for client authentication. This can return null if no
     * certificate has been selected from the Android key store.
     *
     * @return  SSL context to use for client authentication.
     */
    fun getSSLContext(): SSLContext? {
        try {
            val alias = preferences.getString("cert_alias", null) ?: return null
            val keyManager = KeyChainKeyManager(context, alias)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(arrayOf(keyManager), null, null)
            return sslContext
        } catch (e: Exception) {
            Log.e("Certificates", "Cannot create SSLContext object: ${e.message}")
        }
        return null


        /*
        try {
            val alias: String? = preferences.getString("cert_alias", null)
            if (alias != null) {
                val key: PrivateKey? = KeyChain.getPrivateKey(context, alias)
                if (key != null) {
                    val chain: Array<X509Certificate>? = KeyChain.getCertificateChain(context, alias)
                    if (chain != null) {
                        val keyStore: KeyStore? = KeyStore.getInstance("PKCS12")
                        keyStore?.load(null, null)
                        keyStore?.setKeyEntry(alias, key, null, chain)

                        val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                        keyManagerFactory.init(keyStore, null)

                        val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                        trustManagerFactory.init(null as KeyStore?)

                        val sslContext: SSLContext = SSLContext.getInstance("TLS")
                        sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
                        return sslContext
                    }
                }
            }
        }
        catch (e: Exception) {
            val message: String = if (e.message == null) { "Unknown error" } else { e.message!! }
            Log.e("Certificates", "Cannot create SSLContext object: $message")
        }

        return null
        */
    }

}
