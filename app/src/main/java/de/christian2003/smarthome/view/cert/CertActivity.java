package de.christian2003.smarthome.view.cert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeActivity;
import de.christian2003.smarthome.utils.framework.SmartHomeDialog;
import de.christian2003.smarthome.view.password.PasswordDialog;


/**
 * Class implements the activity through which the user can select a client certificate for
 * authentication with the smart home servers.
 */
public class CertActivity extends SmartHomeActivity<CertViewModel> implements SmartHomeDialog.Callback {

    /**
     * Attribute stores the result launcher to select a .pfx-file.
     */
    private final ActivityResultLauncher<Intent> selectCertResultLauncher;

    /**
     * Attribute stores the container displaying that a valid cert is selected.
     */
    private CardView certContainer;

    /**
     * Attribute stores the container displaying an error message if no certificate is selected.
     */
    private CardView errorContainer;


    /**
     * Constructor instantiates a new activity.
     */
    public CertActivity() {
        super(CertViewModel.class, R.layout.activity_cert);

        selectCertResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    viewModel.setCertUri(uri);
                    PasswordDialog dialog = new PasswordDialog();
                    dialog.show(getSupportFragmentManager(), null);
                }
            }
        });
    }


    /**
     * Method is invoked as dialog callback.
     *
     * @param dialog        Dialog that was closed.
     * @param resultCode    Result code is either {@link #RESULT_SUCCESS} or {@link #RESULT_CANCEL}
     *                      and indicates how the dialog is closed.
     */
    @Override
    public void onCallback(SmartHomeDialog<? extends ViewModel> dialog, int resultCode) {
        if (resultCode == SmartHomeDialog.Callback.RESULT_SUCCESS) {
            String password = ((PasswordDialog)dialog).getPassword();
            viewModel.saveCertificate(password);
            certAvailabilityChanged();
        }
    }


    /**
     * Method is called whenever the activity is created.
     *
     * @param savedInstanceState    Previously saved state of the instance.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaterialToolbar appBar = findViewById(R.id.app_bar);
        appBar.setNavigationOnClickListener(view -> finish());

        certContainer = findViewById(R.id.container_cert);
        errorContainer = findViewById(R.id.container_error);

        certAvailabilityChanged();

        findViewById(R.id.button_select_cert).setOnClickListener(this::startSelectCert);
        findViewById(R.id.button_change_cert).setOnClickListener(this::startSelectCert);
        findViewById(R.id.button_remove).setOnClickListener(this::removeCert);
    }


    /**
     * Method opens a file explorer to select a .pfx-file.
     *
     * @param view  View that was clicked.
     */
    private void startSelectCert(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/x-pkcs12");
        selectCertResultLauncher.launch(intent);
    }

    /**
     * Method removes the certificate from the app.
     *
     * @param view  View that was clicked.
     */
    private void removeCert(View view) {
        if (!viewModel.removeCert()) {
            Toast.makeText(this, R.string.cert_error_remove, Toast.LENGTH_SHORT).show();
        }
        else {
            certAvailabilityChanged();
        }
    }


    /**
     * Method can be called to update the UI after the availability of a certificate changes.
     */
    private void certAvailabilityChanged() {
        boolean certExists = viewModel.certExists();
        Log.d("CertActivity", "certExists=" + certExists);
        if (certExists) {
            certContainer.setVisibility(View.VISIBLE);
            errorContainer.setVisibility(View.GONE);
        }
        else {
            certContainer.setVisibility(View.GONE);
            errorContainer.setVisibility(View.VISIBLE);
        }
    }

}
