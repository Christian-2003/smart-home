package de.christian2003.smarthome.data.model

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.christian2003.smarthome.data.model.cert.SslTrustResponse
import de.christian2003.smarthome.data.model.extraction.ShWebpageContent
import de.christian2003.smarthome.data.model.extraction.ShWebpageContentCallback
import de.christian2003.smarthome.data.model.room.ShRoom
import de.christian2003.smarthome.data.model.userinformation.UserInformation


/**
 * Class implements the repository through which the app access all data.
 */
class SmartHomeRepository(
    private val context: Context,
    private val callback: ShWebpageContentCallback?
) {

    /**
     * Attribute stores the shared preferences from which to source configuration data.
     */
    private val preferences = context.getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Attribute stores the callback invoked once the smart home webpage is loaded.
     */
    private val webpageContentCallback = ShWebpageContentCallback { success, sslTrustResponse ->
        onWebpageContentLoaded(success, sslTrustResponse)
    }

    /**
     * Attribute stores the webpage content through which all data is loaded.
     */
    private var webpageContent = ShWebpageContent(preferences.getString("server_url", ""), context, webpageContentCallback)

    /**
     * Attribute indicates whether the webpage content is loading.
     */
    var isLoading: Boolean by mutableStateOf(true)

    /**
     * Attribute stores the list of rooms.
     */
    var rooms: List<ShRoom> by mutableStateOf(emptyList())

    /**
     * Attribute stores the list of errors occurring when the webpage is loaded.
     */
    var infos: List<UserInformation> by mutableStateOf(emptyList())

    var sslTrustResponse: SslTrustResponse? by mutableStateOf(null)


    fun restartFetchingData() {
        isLoading = true
        this.sslTrustResponse = null
        webpageContent = ShWebpageContent(preferences.getString("server_url", ""), context, webpageContentCallback)
    }


    /**
     * Method is called once the webpage content loads.
     */
    private fun onWebpageContentLoaded(success: Boolean, sslTrustResponse: SslTrustResponse?) {
        if (success) {
            rooms = webpageContent.smartHomeData!!.toList()
            infos = webpageContent.loadingInformation.toList().distinct()
            Log.d("Smart Home Repo", "Successfully loaded data")
        }
        else {
            Log.e("Smart Home Repo", "Cannot load data")
        }
        this.sslTrustResponse = sslTrustResponse
        isLoading = false
        callback?.onPageLoadComplete(success, sslTrustResponse)
    }


    companion object {

        /**
         * Attribute stores the singleton instance for the repository.
         */
        private var INSTANCE: SmartHomeRepository? = null


        /**
         * Method returns the singleton instance of the repository.
         */
        fun getInstance(context: Context): SmartHomeRepository {
            if (INSTANCE == null) {
                INSTANCE = SmartHomeRepository(context, null)
            }
            return INSTANCE!!
        }


        /**
         * Method returns the singleton instance of the repository.
         */
        fun getInstance(context: Context, callback: ShWebpageContentCallback): SmartHomeRepository {
            if (INSTANCE == null) {
                INSTANCE = SmartHomeRepository(context, callback)
            }
            else if (!INSTANCE!!.isLoading) {
                callback.onPageLoadComplete(true, INSTANCE!!.sslTrustResponse)
            }
            return INSTANCE!!
        }

    }

}
