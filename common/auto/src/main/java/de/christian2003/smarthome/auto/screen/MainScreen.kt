package de.christian2003.smarthome.auto.screen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.annotations.ExperimentalCarApi
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Tab
import androidx.car.app.model.TabContents
import androidx.car.app.model.TabTemplate
import androidx.car.app.model.TabTemplate.TabCallback
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import de.christian2003.smarthome.auto.model.TabInfo
import de.christian2003.smarthome.data.model.room.ShInfoText
import de.christian2003.smarthome.data.model.room.ShRoom


/**
 * Class implements the main screen which displays two tabs: The 'home'-tab displays all general
 * stats whereas the 'rooms'-tab displays all rooms.
 */
@ExperimentalCarApi
class MainScreen(
    carContext: CarContext,
    private val rooms: List<ShRoom>
): Screen(carContext), TabCallback {

    /**
     * Attribute stores info for the home tab displaying the general stats.
     */
    private val homeTab = TabInfo("home", de.christian2003.smarthome.data.R.string.car_tab_home, de.christian2003.smarthome.data.R.drawable.ic_home)

    /**
     * Attribute stores info for the rooms tab displaying a list of all rooms.
     */
    private val roomsTab = TabInfo("rooms", de.christian2003.smarthome.data.R.string.car_tab_rooms, de.christian2003.smarthome.data.R.drawable.ic_rooms)

    /**
     * Attribute stores the ID of the tab currently active.
     */
    private var activeTabId: String = homeTab.tabId


    /**
     * Method creates the template for the screen.
     *
     * @return  Template for the screen.
     */
    override fun onGetTemplate(): Template {
        val builder = TabTemplate.Builder(this)
        builder.setHeaderAction(Action.APP_ICON)
        builder.addTab(getTab(homeTab))
        builder.addTab(getTab(roomsTab))
        builder.setTabContents(getActiveTabContents())
        builder.setActiveTabContentId(activeTabId)
        return builder.build()
    }


    /**
     * Method implements the callback invoked once the selected tab changes.
     */
    override fun onTabSelected(tabContentId: String) {
        activeTabId = tabContentId
        invalidate()
    }


    /**
     * Method returns the tab contents for the tab that is currently active.
     *
     * @return  Tab contents for the tab currently active.
     */
    private fun getActiveTabContents(): TabContents {
        return if (activeTabId == homeTab.tabId) {
            TabContents.Builder(getHomeTabTemplate()).build()
        }
        else {
            TabContents.Builder(getRoomsTabTemplate()).build()
        }
    }


    /**
     * Method returns the tab for the tab info specified.
     *
     * @param tabInfo   Tab info for which to return the tab.
     * @return          Created tab for the tab info specified.
     */
    private fun getTab(tabInfo: TabInfo): Tab {
        val iconCompat: IconCompat = IconCompat.createWithResource(carContext.baseContext, tabInfo.tabIcon)
        val carIcon = CarIcon.Builder(iconCompat).build()

        val builder = Tab.Builder()
        builder.setIcon(carIcon)
        builder.setTitle(carContext.baseContext.getString(tabInfo.tabTitle))
        builder.setContentId(tabInfo.tabId)
        return builder.build()
    }


    /**
     * Method returns the tab template for the home tab. This tab displays the general stats.
     *
     * @return  Template for the home tab.
     */
    private fun getHomeTabTemplate(): Template {
        val builder = ItemList.Builder()

        if (rooms.isNotEmpty()) {
            rooms[0].infos.forEach { infoText ->
                builder.addItem(buildRow(infoText))
            }
        }

        return ListTemplate.Builder()
            .setSingleList(builder.build())
            .setHeaderAction(Action.APP_ICON)
            .setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.app_name))
            .build()
    }


    /**
     * Method returns the tab template for the rooms tab. This tab displays a list of all rooms.
     *
     * @return  Template for the rooms tab.
     */
    private fun getRoomsTabTemplate(): Template {
        val builder = ItemList.Builder()

        if (rooms.size >= 2) {
            rooms.subList(1, rooms.size).forEach { room ->
                builder.addItem(buildRow(room))
            }
        }

        return ListTemplate.Builder()
            .setSingleList(builder.build())
            .setHeaderAction(Action.APP_ICON)
            .setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.app_name))
            .build()
    }


    /**
     * Method creates a row for a room.
     *
     * @param room  Room for which to create the row.
     * @return      Row for the room specified.
     */
    private fun buildRow(room: ShRoom): Row {
        val builder = Row.Builder()
        builder.setTitle(room.name)
        builder.addText(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.car_details_subtext))
        builder.setOnClickListener {
            screenManager.push(RoomScreen(carContext, room))
        }
        return builder.build()
    }


    /**
     * Method creates a row for an info text.
     *
     * @param infoText  Info text for which to create the row.
     * @return          Row for the info text specified.
     */
    private fun buildRow(infoText: ShInfoText): Row {
        var title = carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.car_main_text)
        title = title.replace("{label}", infoText.label)
        title = title.replace("{text}", if (infoText.text != null) { infoText.text!! } else { "" })

        val builder = Row.Builder()
        builder.setTitle(title)
        return builder.build()
    }

}
