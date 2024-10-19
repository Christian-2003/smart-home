package de.christian2003.smarthome.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeFragment;


/**
 * Class implements the home fragment for the app.
 */
public class HomeFragment extends SmartHomeFragment<HomeViewModel> {

    /**
     * Constructor instantiates a new fragment.
     */
    public HomeFragment() {
        super(HomeViewModel.class, R.layout.fragment_home);
    }


    /**
     * Method is called whenever the fragment view is created.
     *
     * @param inflater              Layout inflater to use to inflate the fragment layout.
     * @param container             Parent for the fragment view.
     * @param savedInstanceState    Previously saved state of the instance.
     * @return                      Created fragment view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        return view;
    }

}
