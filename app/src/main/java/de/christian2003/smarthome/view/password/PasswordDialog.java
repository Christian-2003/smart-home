package de.christian2003.smarthome.view.password;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeDialog;


/**
 * Class implements a dialog through which the user can enter the password for the client certificate.
 */
public class PasswordDialog extends SmartHomeDialog<PasswordViewModel> {

    /**
     * Class implements a text watcher for the password input.
     */
    private class PasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            viewModel.setPassword(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    /**
     * Constructor instantiates a new dialog.
     */
    public PasswordDialog() {
        super(PasswordViewModel.class, R.layout.dialog_password);
    }


    /**
     * Method returns the password entered by the user.
     *
     * @return  Password entered by the user.
     */
    @NonNull
    public String getPassword() {
        return viewModel.getPassword();
    }


    /**
     * Method is called whenever the view for the dialog is created.
     *
     * @param inflater              Layout inflater to use in order to inflate the dialog view.
     * @param container             Parent view of the dialog.
     * @param savedInstanceState    Previously saved state of the instance.
     * @return                      Inflated view for the dialog.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        TextInputEditText passwordEditText = view.findViewById(R.id.input_password);
        TextInputLayout passwordContainer = view.findViewById(R.id.container_password);
        passwordEditText.setText(viewModel.getPassword());
        passwordEditText.addTextChangedListener(new PasswordTextWatcher());

        //Configure okay button:
        view.findViewById(R.id.button_ok).setOnClickListener(v -> {
            if (viewModel.getPassword().isEmpty()) {
                passwordContainer.setError(getString(R.string.password_error_empty));
                return;
            }
            if (callback != null) {
                callback.onCallback(this, Callback.RESULT_SUCCESS);
            }
            dismiss();
        });

        //Configure cancel button:
        view.findViewById(R.id.button_cancel).setOnClickListener(v -> {
            if (callback != null) {
                callback.onCallback(this, Callback.RESULT_CANCEL);
            }
            dismiss();
        });

        return view;
    }

}
