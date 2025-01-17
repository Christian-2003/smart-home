package de.christian2003.smarthome.auto.screen

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.christian2003.smarthome.data.model.SmartHomeRepository
import de.christian2003.smarthome.data.model.extraction.ShWebpageContentCallback

class LoadingScreen(carContext: CarContext): Screen(carContext) {

    private var repository: SmartHomeRepository? = null

    private var callback = ShWebpageContentCallback { success ->
        if (success && repository != null) {
            screenManager.push(MainScreen(carContext, repository!!.rooms))
        }
        else {
            carContext.finishCarApp() //Close on error
        }
        /*
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

         */
    }


    override fun onGetTemplate(): Template {
        val pane = Pane.Builder().setLoading(true).addRow(buildRow()).build()

        val paneTemplate = PaneTemplate.Builder(pane)
            .setHeaderAction(Action.APP_ICON)
            .setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.app_name))
            .build()

        repository = SmartHomeRepository.getInstance(carContext.baseContext, callback)

        return paneTemplate
    }


    private fun buildRow(): Row {
        val builder = Row.Builder()
        builder.setTitle(carContext.baseContext.getString(de.christian2003.smarthome.data.R.string.car_loading))
        return builder.build()
    }

}
