package de.christian2003.smarthome.auto.screen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.model.extraction.ShWebpageContentCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Class implements the loading screen. This screen is displayed to the user while the data loads.
 */
class LoadingScreen(carContext: CarContext): Screen(carContext) {

    /**
     * Attribute stores the repository through which to load and access the data.
     */
    private var repository: SmartHomeRepository? = null

    /**
     * Attribute stores a callback that is invoked once the data is loaded.
     */
    private var callback = ShWebpageContentCallback { success ->
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                if (success && repository != null) {
                    screenManager.push(MainScreen(carContext, repository!!.rooms))
                }
                else {
                    carContext.finishCarApp() //Close on error
                }
            }
        }
    }


    /**
     * Method creates the template for the loading screen.
     *
     * @return  Template for the screen.
     */
    override fun onGetTemplate(): Template {
        val pane = Pane.Builder().setLoading(true).build() //Do NOT add row to pane that is set to "loading".

        val paneTemplate = PaneTemplate.Builder(pane)
            .setHeaderAction(Action.APP_ICON)
            .setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.app_name))
            .build()

        repository = SmartHomeRepository.getInstance(carContext.baseContext, callback)

        return paneTemplate
    }

}
