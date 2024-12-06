package de.christian2003.smarthome.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeFragment;
import de.christian2003.smarthome.view.cert.CertActivity;
import de.christian2003.smarthome.view.url.UrlActivity;


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


    /**
     * Method is called whenever the view for the fragment is created.
     *
     * @param inflater              Layout inflater to use to inflate the fragment layout.
     * @param container             Parent for the fragment view.
     * @param savedInstanceState    Previously saved state of the instance.
     * @return                      Created view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        view.findViewById(R.id.container_url).setOnClickListener(this::startUrlActivity);
        view.findViewById(R.id.container_cert).setOnClickListener(this::startCertActivity);

        return view;
    }


    /**
     * Method starts the activity through which to edit the server URL, once the corresponding view
     * is clicked.
     *
     * @param view  View that was clicked.
     */
    private void startUrlActivity(View view) {
        Intent intent = new Intent(getActivity(), UrlActivity.class);
        intent.putExtra(UrlActivity.EXTRA_CLOSEABLE, true);
        startActivity(intent);
    }

    /**
     * Method starts the activity through which to select the client certificate, once the
     * corresponding view is clicked.
     *
     * @param view  View that was clicked.
     */
    private void startCertActivity(View view) {
        Intent intent = new Intent(getActivity(), CertActivity.class);
        startActivity(intent);
    }

}
