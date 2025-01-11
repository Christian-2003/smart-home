package de.christian2003.smarthome.data.model.extraction.search.room;

import androidx.annotation.NonNull;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.data.model.room.ShInfoText;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;
import de.christian2003.smarthome.data.model.wrapper.RoomInfoTextWrapper;

public class ShInfoTextSearch {

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
                    return new RoomInfoTextWrapper(getInnerTableContent(innerTable, firstDataCell.text()), new ArrayList<>());
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
     * @return                  A list of info texts.
     */
    @NonNull
    public static ArrayList<ShInfoText> getInnerTableContent(@NonNull Element innerTable, @NonNull String label) {
        Elements innerTableRows = innerTable.select("tr");
        ArrayList<ShInfoText> shInfoTextsInnerTable = new ArrayList<>();

        // Get the content of the inner table and create info texts for the found information.
        for (Element innerTableRow: innerTableRows) {
            Element firstDataCell = innerTableRow.selectFirst("td");

            if (firstDataCell != null) {
                Element secondDataCell = innerTable.selectFirst("td ~ td");

                if (secondDataCell != null) {
                    shInfoTextsInnerTable.add(new ShInfoText(label, firstDataCell.text(), secondDataCell.text()));
                }
                else {

                }
            }
            else {

            }
        }
        return shInfoTextsInnerTable;
    }
}
