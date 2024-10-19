package de.christian2003.smarthome.view.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.view.home.HomeFragment;
import de.christian2003.smarthome.view.settings.SettingsFragment;


/**
 * Class implements the view model for the {@link MainActivity}.
 */
public class MainViewModel extends ViewModel {

    /**
     * Attribute stores the home fragment.
     */
    @NonNull
    private final HomeFragment homeFragment;

    /**
     * Attribute stores the settings fragment.
     */
    @NonNull
    private final SettingsFragment settingsFragment;

    /**
     * Attribute stores the ID of the fragment that is currently selected within the main navigation
     * menu.
     */
    private int selectedFragmentId;


    /**
     * Constructor instantiates a new view model.
     */
    public MainViewModel() {
        homeFragment = new HomeFragment();
        settingsFragment = new SettingsFragment();
        selectedFragmentId = R.id.menu_home;
    }


    /**
     * Method returns the home fragment.
     *
     * @return  Home fragment.
     */
    @NonNull
    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    /**
     * Method returns the settings fragment.
     *
     * @return  Settings fragment.
     */
    @NonNull
    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }

    /**
     * Method returns the ID of the fragment selected in the main navigation menu.
     *
     * @return  ID of the fragment selected.
     */
    public int getSelectedFragmentId() {
        return selectedFragmentId;
    }

    /**
     * Method changes the ID of the fragment seleczed in the main navigation menu.
     *
     * @param selectedFragmentId    ID of the new selected fragment.
     */
    public void setSelectedFragmentId(int selectedFragmentId) {
        this.selectedFragmentId = selectedFragmentId;
    }

}
