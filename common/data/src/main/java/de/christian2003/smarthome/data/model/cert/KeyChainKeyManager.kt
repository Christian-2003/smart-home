package de.christian2003.smarthome.data.model.cert

import android.content.Context
import android.security.KeyChain
import java.net.Socket
import java.security.Principal
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.net.ssl.X509KeyManager


/**
 * Class implements a key manager for the key chain that is required for web requests with a custom
 * client certificate.
 */
class KeyChainKeyManager(

    /**
     * Context for the key manager.
     */
    private val context: Context,

    /**
     * Alias of the certificate to use.
     */
    private val alias: String

): X509KeyManager {

    override fun getClientAliases(keyType: String?, issuers: Array<Principal>?): Array<String>? {
        return arrayOf(alias)
    }

    override fun chooseClientAlias(keyType: Array<out String>?, issuers: Array<out Principal>?, socket: Socket?): String? {
        return alias
    }

    override fun getCertificateChain(alias: String?): Array<X509Certificate>? {
        return KeyChain.getCertificateChain(context, alias!!)
    }

    override fun getPrivateKey(alias: String?): PrivateKey? {
        return KeyChain.getPrivateKey(context, alias!!)
    }

    override fun getServerAliases(keyType: String?, issuers: Array<Principal>?): Array<String>? {
        return null
    }

    override fun chooseServerAlias(keyType: String?, issuers: Array<Principal>?, socket: Socket?): String? {
        return null
    }

}
