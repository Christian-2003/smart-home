package de.christian2003.smarthome.auto.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


/**
 * Class models contains information on a single tab.
 */
data class TabInfo (

    /**
     * Attribute stores the ID of the tab.
     */
    val tabId: String,

    /**
     * Attribute stores the ID of the string resource containing the title of the tab.
     */
    @StringRes
    val tabTitle: Int,

    /**
     * Attribute stores the ID of the drawable resource containing the icon of the tab.
     */
    @DrawableRes
    val tabIcon: Int

)
