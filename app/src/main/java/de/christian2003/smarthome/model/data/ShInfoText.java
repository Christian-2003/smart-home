package de.christian2003.smarthome.model.data;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.model.data.wrapper_class.RoomInfoTextWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


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
     * Finds the temperature of a room and creates an info text form them.
     *
     * @param tableRow      The table row which contains the cells with the temperature.
     * @return              A RoomInfoTextWrapper which contains a list of info texts and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomInfoTextWrapper createTemperatureInfoText(@NonNull Element tableRow) {
        Element firstDataCell = tableRow.selectFirst("tr > td");

        // Find the data cell containing the temperature.
        if (firstDataCell != null) {
            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");

            if (secondDataCell != null) {
                Element innerTable = secondDataCell.selectFirst("table");

                // If an inner table element was found there are multiple temperatures for the room and they have to be extracted from the table. Otherwise there is only one temperature which is directly located in the data cell.
                if (innerTable != null) {
                    return getInnerTableContent(innerTable, firstDataCell.text());
                }
                else {
                    return new RoomInfoTextWrapper(new ArrayList<>(Collections.singletonList(new ShInfoText(firstDataCell.text(), null, secondDataCell.text()))), new ArrayList<>());
                }
            }
            else {
                String warningDescription = "A table row that contains the temperature of the room should be present but could not be found. Please check the website and the documentation.";
                return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else {
            // No table rows were found in the table.
            String warningDescription = "No table row was found in the table. The table should contain rows with the temperature. No temperature could be found. Please check the website and the documentation.";
            return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Gets the content of the inner table. That is only the case if there are multiple temperatures for the room.
     *
     * @param innerTable        The table element which contains the specifiers for the different temperatures.
     * @param label             The label of the info text.
     * @return                  A {@link RoomInfoTextWrapper} object with a list of the temperature info texts and a list of the warnings that occurred it.
     */
    @NonNull
    public static RoomInfoTextWrapper getInnerTableContent(@NonNull Element innerTable, @NonNull String label) {
        Elements innerTableRows = innerTable.select("tr");
        ArrayList<ShInfoText> shInfoTextsInnerTable = new ArrayList<>();
        ArrayList<UserInformation> userInformation = new ArrayList<>();

        // Get the content of the inner table and create info texts for the found information.
        for (Element innerTableRow: innerTableRows) {
            Element firstDataCell = innerTableRow.selectFirst("td");

            if (firstDataCell != null) {
                Element secondDataCell = innerTable.selectFirst("td ~ td");

                if (secondDataCell != null) {
                    shInfoTextsInnerTable.add(new ShInfoText(label, firstDataCell.text(), secondDataCell.text()));
                }
                else {
                    String warningDescription = "No data cell could be found for the temperature element \" " + firstDataCell.text() + " \" in the inner table which should contain further temperature information. Please check the website and the documentation. ";
                    userInformation.add(new UserInformation(InformationType.WARNING ,InformationTitle.HtmlElementNotLocated, warningDescription));
                }
            }
            else {
                String warningDescription = "No data cell could be found for a temperature element in the inner table which should contain further temperature information. Please check the website and the documentation. ";
                userInformation.add(new UserInformation(InformationType.WARNING ,InformationTitle.HtmlElementNotLocated, warningDescription));
            }
        }
        return new RoomInfoTextWrapper(shInfoTextsInnerTable, userInformation);
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
