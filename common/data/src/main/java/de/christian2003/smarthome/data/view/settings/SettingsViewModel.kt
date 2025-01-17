package de.christian2003.smarthome.data.view.settings

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel


/**
 * Class implements the view model for the settings page.
 */
class SettingsViewModel(application: Application): AndroidViewModel(application) {

    /**
     * Attribute stores the preferences in which settings are stored.
     */
    private val preferences = getApplication<Application>().getSharedPreferences("smart_home", Context.MODE_PRIVATE)

    /**
     * Attribute stores whether to show warnings when displaying a room.
     */
    var showWarnings: Boolean by mutableStateOf(true)

    /**
     * Attribute stores whether to show errors when displaying a room.
     */
    var showErrors: Boolean by mutableStateOf(true)


    /**
     * Method initializes the view model.
     */
    fun init() {
        showWarnings = preferences.getBoolean("show_warnings", true)
        showErrors = preferences.getBoolean("show_errors", true)
    }


    /**
     * Method updates whether to show warnings when to display a room.
     *
     * @param showWarnings  Whether to display warnings.
     */
    fun updateShowWarnings(showWarnings: Boolean) {
        this.showWarnings = showWarnings
        preferences.edit().putBoolean("show_warnings", showWarnings).apply()
    }

    /**
     * Method updates whether to show errors when to display a room.
     *
     * @param showErrors    Whether to display errors.
     */
    fun updateShowErrors(showErrors: Boolean) {
        this.showErrors = showErrors
        preferences.edit().putBoolean("show_errors", showErrors).apply()
    }

}
