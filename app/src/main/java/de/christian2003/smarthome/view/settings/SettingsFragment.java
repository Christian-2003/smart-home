package de.christian2003.smarthome.view.settings;

import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeFragment;


/**
 * Class implements the settings fragment.
 */
public class SettingsFragment extends SmartHomeFragment<SettingsViewModel> {

    /**
     * Constructor instantiates a new fragment.
     */
    public SettingsFragment() {
        super(SettingsViewModel.class, R.layout.fragment_settings);
    }

}
