package de.christian2003.smarthome.view.url;

import android.util.Patterns;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import java.net.MalformedURLException;
import java.net.URL;
import de.christian2003.smarthome.model.Config;


/**
 * Class implements the view model for the {@link UrlActivity}.
 */
public class UrlViewModel extends ViewModel {

    /**
     * Attribute stores the URL entered by the user. This is {@code null} if no URL was entered.
     */
    @Nullable
    private URL url;


    /**
     * Constructor instantiates a new view model.
     */
    public UrlViewModel() {
        url = null;
        updateUrl(Config.getInstance().getServerUrl(null));
    }


    /**
     * Method returns the URL string entered by the user.
     *
     * @return  URL string entered by the user.
     */
    @Nullable
    public String getUrl() {
        if (url == null) {
            return null;
        }
        else {
            return url.toString();
        }
    }


    /**
     * Method changes the URL entered by the user to the passed char sequence. The URL within the
     * view model is only updated if the passed argument is valid (i.e. the method returns
     * {@code true}).
     *
     * @param url   New URL entered by the user.
     * @return      Whether the entered URL is valid.
     */
    public boolean updateUrl(@Nullable CharSequence url) {
        if (url == null) {
            return false;
        }
        if (Patterns.WEB_URL.matcher(url).matches()) {
            try {
                this.url = new URL(url.toString());
            }
            catch (MalformedURLException e) {
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Method saves the URL entered by the user to the storage.
     */
    public void saveUrl() {
        if (url != null) {
            String urlString = url.toString();
            Config.getInstance().setServerUrl(urlString);
        }
    }

}
