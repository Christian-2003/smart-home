package de.christian2003.smarthome.model.data;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Class models an info text for the smart home.
 */
public class ShInfoText {

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
     * Attribute stores the background color for the info text.
     */
    @Nullable
    private Color backgroundColor;


    /**
     * Constructor instantiates a new info text.
     *
     * @param label             Label for the info text.
     * @param text              Text for the info text.
     * @param backgroundColor   Background color for the info text.
     */
    public ShInfoText(@NonNull String label, @Nullable String specifier ,@Nullable String text, @Nullable Color backgroundColor) {
        this.label = label;
        this.specifier = specifier;
        this.text = text;
        this.backgroundColor = backgroundColor;
    }


    /**
     * Method returns the label for the info text.
     *
     * @return  Label for the info text.
     */
    @NonNull
    public String getLabel() {
        return label;
    }

    /**
     * Method returns the text for the info text.
     *
     * @return  Text for the info text.
     */
    @Nullable
    public String getText() {
        return text;
    }

    /**
     * Method returns the background color for the info text.
     *
     * @return  Background color for the info text.
     */
    @Nullable
    public Color getBackgroundColor() {
        return backgroundColor;
    }

}
