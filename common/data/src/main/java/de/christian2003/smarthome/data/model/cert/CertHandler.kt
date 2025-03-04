package de.christian2003.smarthome.data.model.cert

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.security.MessageDigest
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext


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


    fun validateCert(cert: X509Certificate): SslTrustResponse {
        if (preferences.getBoolean("unsafe_cert_validation", false)) {
            return SslTrustResponse(SslTrustStatus.Trusted, cert) //Skip validation if unsafe cert validation is enabled.
        }
        val fingerprint = getCertFingerprint(cert)
        return if (isCertTrusted(fingerprint)) {
            SslTrustResponse(SslTrustStatus.Trusted, cert)
        } else {
            SslTrustResponse(SslTrustStatus.Untrusted, cert)
        }
    }


    fun saveCert(cert: X509Certificate) {
        val trustedCerts: MutableSet<String> = preferences.getStringSet("trusted_certs", emptySet())!!.toMutableSet()
        trustedCerts.add(getCertFingerprint(cert))
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putStringSet("trusted_certs", trustedCerts)
        editor.apply()
    }


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


    private fun isCertTrusted(fingerprint: String): Boolean {
        val trustedCerts: Set<String>? = preferences.getStringSet("trusted_certs", emptySet())
        trustedCerts!!.forEach { cert ->
            Log.d("CertHandler", "Trusted Fingerprint: $cert")
        }
        Log.d("CertHandler", "Cert Fingerprint: $fingerprint")
        return trustedCerts!!.contains(fingerprint)
    }


    private fun getCertFingerprint(cert: X509Certificate): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val publicKey = md.digest(cert.encoded)

            val hexString = StringBuilder()
            for (b in publicKey) {
                hexString.append(String.format("%02X:", b))
            }
            return hexString.substring(0, hexString.length - 1)
        }
        catch (e: Exception) {
            return ""
        }
    }

}
