package de.christian2003.smarthome.model.user_information;

import java.io.Serializable;

/**
 * The different type of information that can be displayed for the user.
 */
public enum InformationType implements Serializable {
    INFORMATION,
    WARNING,
    ERROR;
}
