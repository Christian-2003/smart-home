package de.christian2003.smarthome.data.view.cert

import android.app.Application
import android.content.Context
import android.security.KeyChain
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.PrivateKey
import java.security.cert.X509Certificate


/**
 * Class implements the view model fot the view through which the user can select a certificate.
 */
class CertViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the preferences in which to store the URL.
     */
    private val preferences = getApplication<Application>().getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Attribute stores whether a certificate is selected.
     */
    var isCertSelected: Boolean? by mutableStateOf(null)


    /**
     * Method initializes the view model.
     */
    fun init() {
        isCertAvailable()
    }


    /**
     * Method imports a new certificate with the alias specified.
     *
     * @param alias Alias of the certificate to import.
     */
    fun importCert(alias: String) = viewModelScope.launch(Dispatchers.IO) {
        val context = getApplication<Application>().applicationContext
        val key: PrivateKey? = KeyChain.getPrivateKey(context, alias)
        if (key != null) {
            val chain: Array<X509Certificate>? = KeyChain.getCertificateChain(context, alias)
            if (chain != null) {
                val editor = preferences.edit()
                editor.putString("cert_alias", alias)
                editor.apply()
                isCertAvailable()
            }
            else {
                Log.e("Certificates", "Chain is null")
            }
        }
        else {
            Log.e("Certificates", "Key is null")
        }
    }


    /**
     * Method removes the certificate from the app.
     */
    fun removeCert() {
        val editor = preferences.edit()
        editor.remove("cert_alias")
        editor.apply()
        isCertSelected = false
    }


    /**
     * Method determines whether a certificate is available.
     */
    private fun isCertAvailable() = viewModelScope.launch(Dispatchers.IO) {
        val alias: String? = preferences.getString("cert_alias", null)
        val context: Context = getApplication<Application>().applicationContext
        var result: Boolean
        if (alias != null) {
            try {
                val privateKey: PrivateKey? = KeyChain.getPrivateKey(context, alias)
                val chain: Array<X509Certificate>? = KeyChain.getCertificateChain(context, alias)

                result = privateKey != null && chain != null
            }
            catch(e: Exception) {
                Log.e("Certificates", "Cert is unavailable: ${e.message}")
                result = false
            }
        }
        else {
            result = false
        }
        isCertSelected = result
    }

}
