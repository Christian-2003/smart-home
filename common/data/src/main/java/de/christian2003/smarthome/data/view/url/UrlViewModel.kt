package de.christian2003.smarthome.data.view.url

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import java.net.URL


/**
 * Class implements the view model for the view through which to edit the URL.
 */
class UrlViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the preferences in which to store the URL.
     */
    private val preferences = getApplication<Application>().getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Attribute stores the URL to edit.
     */
    var url: String by mutableStateOf("")

    /**
     * Attribute indicates whether the URL is valid.
     */
    var isUrlValid: Boolean by mutableStateOf(true)


    /**
     * Method instantiates the view model.
     */
    fun init() {
        url = preferences.getString("server_url", "")!!
    }


    /**
     * Method determines whether the URL entered is valid.
     */
    fun isUrlValid() {
        if (Patterns.WEB_URL.matcher(url).matches()) {
            try {
                URL(this.url)
                isUrlValid = true
            }
            catch (e: Exception) {
                isUrlValid = false
            }
        }
        else {
            isUrlValid = false
        }
    }


    /**
     * Method saves the URL entered.
     *
     * @return  Whether the URL entered was saved successfully.
     */
    fun saveUrl(): Boolean {
        if (isUrlValid) {
            val editor = preferences.edit()
            editor.putString("server_url", url)
            editor.apply()
            return true
        }
        else {
            return false
        }
    }

}
