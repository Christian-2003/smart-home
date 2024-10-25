package de.christian2003.smarthome.model.data;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;


/**
 * Class models an info text for the smart home.
 */
public class ShInfoText implements Serializable {

    /**
     * Attribute stores the label for the info text.
     */
    @NonNull
    private final String label;

    /**
     * Attribute specifies the properties of the label.
     */
    @Nullable
    private final String specifier;

    /**
     * Attribute stores the text for the info text.
     */
    @Nullable
    private String text;

    /**
     * Constructor instantiates a new info text.
     *
     * @param label             Label for the info text.
     * @param text              Text for the info text.
     */
    public ShInfoText(@NonNull String label, @Nullable String specifier ,@Nullable String text) {
        this.label = label;
        this.specifier = specifier;
        this.text = text;
    }


    /**
     * Method returns the label of the info text.
     *
     * @return  Label of the info text.
     */
    @NonNull
    public String getLabel() {
        return label;
    }

    /**
     * Method returns the specifier of the info text.
     *
     * @return Specifier of the info text.
     */
    @Nullable
    public String getSpecifier() {return specifier;}

    /**
     * Method returns the text of the info text.
     *
     * @return  Text of the info text.
     */
    @Nullable
    public String getText() {
        return text;
    }
}
