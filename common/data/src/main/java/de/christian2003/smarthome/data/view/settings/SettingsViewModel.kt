package de.christian2003.smarthome.data.view.settings

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.color.utilities.DynamicColor


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
     * Attribute stores whether to allow unsafe SSL connections.
     */
    var allowUnsafeSsl: Boolean by mutableStateOf(false)

    /**
     * Attribute stores whether to use dynamic colors.
     */
    var useDynamicTheme: Boolean by mutableStateOf(false)


    /**
     * Method initializes the view model.
     */
    fun init() {
        showWarnings = preferences.getBoolean("show_warnings", true)
        showErrors = preferences.getBoolean("show_errors", true)
        allowUnsafeSsl = preferences.getBoolean("unsafe_cert_validation", false)
        useDynamicTheme = preferences.getBoolean("dynamic_theme", false)
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

    /**
     * Method updates whether to allow unsafe SSL connections.
     *
     * @param allowUnsafeSsl    Whether to allow unsafe SSL connections.
     */
    fun updateAllowUnsafeSsl(allowUnsafeSsl: Boolean) {
        this.allowUnsafeSsl = allowUnsafeSsl
        preferences.edit().putBoolean("unsafe_cert_validation", allowUnsafeSsl).apply()
    }

    /**
     * Method updates whether to use dynamic theme colors.
     *
     * @param useDynamicTheme   Whether to use dynamic theme colors.
     */
    fun updateUseDynamicTheme(useDynamicTheme: Boolean) {
        this.useDynamicTheme = useDynamicTheme
        preferences.edit().putBoolean("dynamic_theme", useDynamicTheme).apply()
    }

}
