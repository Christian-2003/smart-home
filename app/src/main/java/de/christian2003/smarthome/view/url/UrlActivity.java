package de.christian2003.smarthome.view.url;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeActivity;


/**
 * Class implements the activity through which to enter the URL for the server.
 * Once the URL is set successfully, the activity returns {@link #RESULT_OK} as result.
 * Pass a boolean as extra with key {@link #EXTRA_CLOSEABLE} to indicate whether the activity should
 * display a back button within the app bar.
 */
public class UrlActivity extends SmartHomeActivity<UrlViewModel> {

    /**
     * Field stores the key with which to pass the boolean extra indicating whether the app bar
     * should contain a button to close the activity.
     */
    public static final String EXTRA_CLOSEABLE = "extra_closeable";


    /**
     * Attribute stores the button to save the entered URL.
     */
    private Button saveButton;

    /**
     * Attribute stores the container for the edit text used to enter the URL.
     */
    private TextInputLayout urlContainer;

    /**
     * Attribute stores the edit text used to enter the URL.
     */
    private TextInputEditText urlEditText;


    /**
     * Constructor instantiates a new activity.
     */
    public UrlActivity() {
        super(UrlViewModel.class, R.layout.activty_url);
    }


    /**
     * Method is called whenever the activity is created.
     *
     * @param savedInstanceState    Previously saved state of the instance.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_CLOSEABLE) && extras.getBoolean(EXTRA_CLOSEABLE)) {
            MaterialToolbar appBar = findViewById(R.id.app_bar);
            appBar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));
            appBar.setNavigationOnClickListener(view -> finish());
        }

        saveButton = findViewById(R.id.button_save);
        urlContainer = findViewById(R.id.container_url);
        urlEditText = findViewById(R.id.input_url);

        findViewById(R.id.button_cancel).setOnClickListener(view -> finish());

        saveButton.setOnClickListener(this::onSaveClicked);

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (viewModel.updateUrl(charSequence)) {
                    saveButton.setEnabled(true);
                }
                else {
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        urlEditText.setText(viewModel.getUrl() == null ? "" : viewModel.getUrl());
    }


    /**
     * Method is called whenever the button to save the URL is clicked.
     *
     * @param view  Button that was clicked.
     */
    private void onSaveClicked(View view) {
        if (viewModel.updateUrl(urlEditText.getText())) {
            //Entered URL valid:
            viewModel.saveUrl();
            setResult(RESULT_OK);
            finish();
        }
        else {
            //Entered URL invalid:
            urlContainer.setError(getString(R.string.url_error));
        }
    }

}
