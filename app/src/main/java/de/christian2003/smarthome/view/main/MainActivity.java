package de.christian2003.smarthome.view.main;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeActivity;


/**
 * Class implements the main activity for the app.
 */
public class MainActivity extends SmartHomeActivity<MainViewModel> implements NavigationBarView.OnItemSelectedListener {

    /**
     * Constructor instantiates a new main activity.
     */
    public MainActivity() {
        super(MainViewModel.class, R.layout.activity_main);
    }


    /**
     * Method is called whenever the activity is created.
     *
     * @param savedInstanceState    Previously saved state of the instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView navigationBarView = findViewById(R.id.menu_bar);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.setSelectedItemId(viewModel.getSelectedFragmentId());
    }


    /**
     * Method is called whenever a menu item in the {@linkplain NavigationBarView} is selected and
     * changes the view that is displayed within this MainActivity to the corresponding fragment.
     *
     * @param item  Menu item that was selected.
     * @return      Whether clicked menu item could change the fragment successfully.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, viewModel.getHomeFragment()).commit();
            viewModel.setSelectedFragmentId(id);
            return true;
        }
        else if (id == R.id.menu_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, viewModel.getSettingsFragment()).commit();
            viewModel.setSelectedFragmentId(id);
            return true;
        }
        return false;
    }

}
