package de.christian2003.smarthome.model.data.devices;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


/**
 * Class models an opening in the house (i.e. a door or window).
 */
public class ShOpening extends ShGenericDevice {

    /**
     * Constructor instantiates a new door / window.
     *
     * @param name      Name for the door / window.
     * @param imageUri  URI to the image for the door / window.
     */
    public ShOpening(@NonNull String name, @Nullable Uri imageUri) {
        super(name, imageUri);
    }

    public static List<ShOpening> findOpenings(@NonNull Element element) {
        Elements openingsEl = element.select("div  > table > td:contains(Fenster), td:contains(Status)");

        for (Element openingEl: openingsEl) {
            System.out.println("Opening Name: " +  openingEl.ownText());
            String partOpeningName = element.ownText();

            if (partOpeningName.equals("Status")) {


            }
            else {

            }
        }
        return null;
    }

}
