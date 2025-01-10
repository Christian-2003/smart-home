package de.christian2003.smarthome.data.model.userinformation;

import java.io.Serializable;


/**
 * The different type of information that can be displayed for the user.
 */
public enum InformationType implements Serializable {
    INFORMATION,
    WARNING,
    ERROR;
}
