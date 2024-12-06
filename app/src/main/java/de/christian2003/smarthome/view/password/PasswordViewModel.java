package de.christian2003.smarthome.view.password;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;


/**
 * Class implements the view model for {@link PasswordDialog}.
 */
public class PasswordViewModel extends ViewModel {

    /**
     * Attribute stores the password entered by the user.
     */
    @NonNull
    private String password;


    /**
     * Constructor instantiates a new view model.
     */
    public PasswordViewModel() {
        password = "";
    }


    /**
     * Method returns the password entered by the user.
     *
     * @return  Password entered by the user.
     */
    @NonNull
    public String getPassword() {
        return password;
    }


    /**
     * Method changes the password entered by the user.
     *
     * @param password  Password entered by the user.
     */
    public void setPassword(@NonNull String password) {
        this.password = password;
    }

}
