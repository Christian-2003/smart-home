package de.christian2003.smarthome.model.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.model.data.devices.ShOpening;
import de.christian2003.smarthome.model.data.devices.ShOpeningType;
import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;

public class ShStatus {

    public static RoomDeviceWrapper createStatus(@NonNull Element tableRow, @NonNull String roomName) {
        Element firstDataCell = tableRow.selectFirst("tr > td");

        // Find the data cell which contains the status.
        if (firstDataCell != null) {
            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");
            if (secondDataCell != null) {

                // Check if the data cell contains another table. If so the room contains multiple openings that need to be extracted from the table.
                // Otherwise the room contains only one opening which can be extracted directly from the data cell.
                Element innerTable = secondDataCell.selectFirst("table");
                if (innerTable != null) {

                    return findMultipleOpenings(secondDataCell, firstDataCell.ownText() + " " + roomName, openingType);
                }
                else {

                    return findSingleOpening(secondDataCell, firstDataCell.ownText() + " " + roomName, openingType, null);
                }
            }
            else {
                // No second data cell was found which should contain the opening(s).
                String warningDescription = "No second data cells were found in the table which should contain a single opening or a table of multiple openings. No openings could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else {
            // No table rows were found in the table.
            String warningDescription = "No table row was found in the table. The table should contain rows with the openings. No openings could be found. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    public static RoomDeviceWrapper gatherStatusContent(@NonNull Element innerTable, @NonNull String roomName) {
        // Get the table row which contains the specifiers of each status element and the table row with the properties of the elements.
        Element firstTableRow = innerTable.selectFirst("table > tbody >tr");

        if (firstTableRow != null) {
            Elements statusElementNames = firstTableRow.select("tr > td");
            Element secondTableRow = innerTable.selectFirst("table > tbody > tr + tr");

            if (secondTableRow != null) {
                Elements statusElementsContent = secondTableRow.select("tr > td");

                // Check if there is an element specifier for every status element or if there are no specifiers.
                if (statusElementsContent.size() == statusElementNames.size()) {
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());

                    for (int i = 0; i < statusElementNames.size(); i++) {
                        RoomDeviceWrapper temporaryWrapper;
                        temporaryWrapper = findSingleStatusElement(statusElementsContent.get(i), statusElementNames.get(i), roomName);

                        wrapper.addDevices(temporaryWrapper.getDevices());
                        wrapper.addUserInformation(temporaryWrapper.getUserInformation());
                    }
                    return wrapper;
                }
                else {
                    // Different amount of status element values and element specifier.
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());

                    // There are more status elements than specifier. For each element to which a specifier could be found it will be added. The remaining elements will get an automatically generated specifier.
                    if (statusElementsContent.size() > statusElementNames.size()) {
                        for (int i = 0; i < statusElementsContent.size(); i++) {
                            if (statusElementNames.size() >= i) {
                                wrapper.combineWrapper(findSingleStatusElement(statusElementsContent.get(i), statusElementNames.get(i), roomName));
                            }
                            else {

                            }

                        }
                        for (int i = statusElementNames.size(); i < statusElementsContent.size(); i++) {
                            wrapper.combineWrapper(findSingleOpening(statusElementsContent.get(i), openingName, openingType,"Automatic Specifier " + (++i - statusElementNames.size())));
                        }
                        String descriptionWarning = "There were more status elements than specifiers. All openings that could be found were extracted. For the elements to which no specifiers could be found automatic specifiers were implemented. Please check the website and the documentation.";
                        wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, descriptionWarning))));
                        return wrapper;
                    }
                    // There are more specifier than status elements contents. No reliable mapping of element contents and there specifiers possible.
                    // The first specifier will get the contents at the corresponding positions. The specifier with no corresponding content will be generated without the missing attributes.
                    else {
                        for (int i = 0; i < statusElementNames.size(); i++) {
                            if (statusElementsContent.size() >= i) {
                                wrapper.combineWrapper(findSingleStatusElement(statusElementsContent.get(i), statusElementNames.get(i), roomName));
                            }
                            else {
                                wrapper.combineWrapper(findSingleStatusElement(null, statusElementNames.get(i), roomName));
                            }
                        }
                        String descriptionWarning = "There was a different amount of openings and specifiers for them. All openings that could be found were extracted but no specifiers could be found for them. Automatic specifiers were implemented. Please check the website and the documentation.";
                        wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, descriptionWarning))));
                        return wrapper;
                    }
                }
            }
            else {
                // No second table row which contains the values of the openings.
                String warningDescription = "The second table row which contains the openings could not be found. No openings could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else{
            // No table Row found.
            String warningDescription = "No table was found in the inner table. The table should contain rows with the openings. No openings could be found. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    public static RoomDeviceWrapper findSingleStatusElement(@Nullable Element statusElementContent, Element statusElementNameNode, String roomName) {
        String statusElementName = statusElementNameNode.ownText().toLowerCase();

        if (statusElementName.contains("fenster")) {
            if (statusElementContent != null) {
                return ShOpening.findSingleOpening(statusElementContent, "Fenster " + roomName, ShOpeningType.Window, statusElementNameNode.ownText());
            }
            else {
                String warningDescription = "There was a different amount of status element contents and specifiers for them. All elements were extracted but for the element " + statusElementNameNode.ownText() + " no corresponding content could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening("Fenster " + roomName, ShOpeningType.Window, statusElementNameNode.ownText(), null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else if (statusElementName.contains("tür") || statusElementName.contains("tuer")) {
            if (statusElementContent != null) {
                return ShOpening.findSingleOpening(statusElementContent, "Tür " + roomName, ShOpeningType.Window, statusElementNameNode.ownText());
            }
            else {
                String warningDescription = "There was a different amount of status element contents and specifiers for them. All elements were extracted but for the element " + statusElementNameNode.ownText() + " no corresponding content could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening("Tür " + roomName, ShOpeningType.Door, statusElementNameNode.ownText(), null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else if (statusElementName.contains("licht")) {

        }
        else {
            
        }
    }

    public static 
}
