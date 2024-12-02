package de.christian2003.smarthome.model.user_information;

import java.io.Serializable;

import de.christian2003.smarthome.App;
import de.christian2003.smarthome.R;

/**
 * The different titles for the warnings and errors that can occur.
 */
public enum InformationTitle implements Serializable {
    HtmlElementNotLocated,
    NoMilliAmpInformation;


    /**
     * Method returns a localized title for the information title.
     *
     * @return  Localized title.
     */
    public String getLocalizedTitle() {
        switch (this) {
            case HtmlElementNotLocated:
                return App.getContext().getString(R.string.info_title_html_element_not_located);
            case NoMilliAmpInformation:
                return App.getContext().getString(R.string.info_title_no_milli_amp_information);
            default:
                return App.getContext().getString(R.string.info_title_unknown);
        }
    }

}
