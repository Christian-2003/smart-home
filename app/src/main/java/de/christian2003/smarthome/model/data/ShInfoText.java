package de.christian2003.smarthome.model.data;

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
    private final String text;

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
     * Finds the info text of a room and creates an object for it.
     *
     * @param tableRow      The table row which contains the cells with the info text.
     * @return              A RoomInfoTextWrapper which contains a list of info texts and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomInfoTextWrapper createInfoText(@NonNull Element tableRow) {
        Element firstDataCell = tableRow.selectFirst("tr > td");

        // Find the data cell containing the info.
        if (firstDataCell != null) {
            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");

            if (secondDataCell != null) {
                Element innerTable = secondDataCell.selectFirst("table");

                // If an inner table element was found there are multiple info texts for the room and they have to be extracted from the table. Otherwise there is only one info text which is directly located in the data cell.
                if (innerTable != null) {
                    return getInnerTableContent(innerTable, firstDataCell.text());
                }
                else {
                    return new RoomInfoTextWrapper(new ArrayList<>(Collections.singletonList(new ShInfoText(firstDataCell.text(), null, secondDataCell.text()))), new ArrayList<>());
                }
            }
            else {
                String warningDescription = "A table row that contains an info text of the room should be present but could not be found. Please check the website and the documentation.";
                return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else {
            // No table rows were found in the table.
            String warningDescription = "No table row was found in the table. The table should contain rows with the info texts. No info text could be found in this table row. Please check the website and the documentation.";
            return new RoomInfoTextWrapper(new ArrayList<>(),  new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Gets the content of the inner table. That is only the case if there are multiple info texts for a specific information (e.g. temperature).
     *
     * @param innerTable        The table element which contains the specifiers for the different info texts.
     * @param label             The label of the info text.
     * @return                  A {@link RoomInfoTextWrapper} object with a list of info texts and a list of the warnings that occurred it.
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
                    String warningDescription = "No data cell could be found for the info text element \" " + firstDataCell.text() + " \" in the inner table which should contain further information. Please check the website and the documentation. ";
                    userInformation.add(new UserInformation(InformationType.WARNING ,InformationTitle.HtmlElementNotLocated, warningDescription));
                }
            }
            else {
                String warningDescription = "No data cell could be found for a info text element in the inner table which should contain further information. Please check the website and the documentation. ";
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
