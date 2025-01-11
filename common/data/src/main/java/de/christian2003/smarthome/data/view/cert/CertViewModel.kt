package de.christian2003.smarthome.data.view.cert

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.christian2003.smarthome.data.model.cert.CertHandler
import kotlinx.coroutines.launch


/**
 * Class implements the view model fot the view through which the user can select a certificate.
 */
class CertViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the cert handler through which to access / import a certificate.
     */
    private val certHandler: CertHandler = CertHandler(getApplication<Application>().applicationContext)

    /**
     * Attribute stores the URI to the certificate selected by the user.
     */
    var certUri: Uri? = null

    /**
     * Attribute stores whether a valid certificate is selected.
     */
    var isValidCertSelected: Boolean by mutableStateOf(false)

    /**
     * Attribute stores whether the dialog to enter the certificate password is visible.
     */
    var isPasswordDialogVisible: Boolean by mutableStateOf(false)


    /**
     * Method initializes the view model.
     */
    fun init() {
        isValidCertSelected = isCertAvailable()
    }


    /**
     * Method imports the certificate whose URI is stored in "certUri" with the specified password.
     *
     * @param password  Password for the certificate to import.
     */
    fun importCertificate(password: String) = viewModelScope.launch {
        if (certUri != null) {
            certHandler.importNewCertFromUri(certUri!!, password)
            isValidCertSelected = isCertAvailable()
        }
    }

    /**
     * Method removes the certificate that is currently imported within the app.
     */
    fun removeCert() = viewModelScope.launch {
        try {
            certHandler.removeCert()
        }
        catch (e: Exception) {
            //Ignore
        }
        isValidCertSelected = isCertAvailable()
    }

    /**
     * Method determines whether a certificate is currently imported within the app.
     *
     * @return  Whether a valid certificate is imported.
     */
    private fun isCertAvailable(): Boolean {
        return try {
            certHandler.sslContext != null
        }
        catch (e: Exception) {
            false
        }
    }

}
