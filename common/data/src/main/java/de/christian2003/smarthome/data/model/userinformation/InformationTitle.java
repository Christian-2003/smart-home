package de.christian2003.smarthome.data.model.userinformation;

import android.content.Context;
import java.io.Serializable;
import de.christian2003.smarthome.data.R;


/**
 * The different titles for the warnings and errors that can occur.
 */
public enum InformationTitle implements Serializable {
    HtmlElementNotLocated,
    NoMilliAmpInformation,
    LoadingInterruption,
    NetworkError,
    HttpError,
    SslError,
    UnknownError,
    UnknownElement,
    UnknownRoom;


    /**
     * Method returns the text for the information.
     *
     * @param context   Context required to load string resources.
     * @return          String for the information title.
     */
    public String getText(Context context) {
        switch (this) {
            case HtmlElementNotLocated:
                return context.getString(R.string.info_title_html_element_not_located);
            case NoMilliAmpInformation:
                return context.getString(R.string.info_title_no_milli_amp_information);
            default:
                return context.getString(R.string.info_title_unknown);
        }
    }
}
