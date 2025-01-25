package de.christian2003.smarthome.data.model.extraction.search.devices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.data.model.devices.ShGenericDevice;
import de.christian2003.smarthome.data.model.devices.ShShutter;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;
import de.christian2003.smarthome.data.model.wrapper.RoomDeviceWrapper;

/**
 * Class models a search for a shutter for the smart home device.
 */
public class ShShutterSearch {
    /**
     *Finds the shutters of a room and creates devices for them.
     *
     * @param tableRow      The table row which contains the cells with the shutters.
     * @param name          The name of the room in which the shutter is.
     * @return      A RoomDeviceWrapper which contains a list of all shutters that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomDeviceWrapper createShutterDevice(@NonNull Element tableRow, @NonNull String name) {

        // Find the data cell which contains the shutter.
        Element firstDataCell = tableRow.selectFirst("tr > td");
        if (firstDataCell != null) {

            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");
            if (secondDataCell != null) {

                // Check if the data cell contains another table. If so the room contains multiple shutters that need to be extracted from the table.
                // Otherwise the room contains only one shutter which can be extracted directly from the data cell.
                Element innerTable = secondDataCell.selectFirst("table");
                if (innerTable != null) {
                    return findMultipleShutter(secondDataCell, firstDataCell.ownText() + " " + name);
                }
                else {
                    return findSingleShutter(secondDataCell, firstDataCell.ownText() + " " + name, null);
                }
            }
            else {
                // No second data cell was found which should contain the shutter(s).
                String warningDescription = "No second data cells were found in the table which should contain a single shutter or a table of shutters. No shutters could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else {
            // No table rows were found in the table.
            String warningDescription = "No table row was found in the table. The table should contain rows with the shutters. No shutters could be found. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Finds multiple shutters, creates devices for them and returns them with all warnings/ errors that occurred while finding them.
     *
     * @param innerTable        The inner table which is present when the room contains multiple shutters.
     * @param name              The name of the shutter in the rooms combined with the room name.
     * @return                  A RoomDeviceWrapper which contains a list of all shutters that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    private static RoomDeviceWrapper findMultipleShutter(@NonNull Element innerTable, @NonNull String name) {

        // Get the table row which contains the specifiers of each shutter and the table row with the properties of the shutters.
        Element firstTableRow = innerTable.selectFirst("table > tbody >tr");
        if (firstTableRow != null) {
            Elements shutterSpecifier = firstTableRow.select("tr > td");
            Element secondTableRow = innerTable.selectFirst("table > tbody > tr + tr");

            if (secondTableRow != null) {
                Elements shutterValues = secondTableRow.select("tr > td");

                // Check if there is a shutter specifier for every shutter or if there are no specifiers.
                if (shutterValues.size() == shutterSpecifier.size() || shutterSpecifier.isEmpty()) {
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());
                    for (int i = 0; i < shutterValues.size(); i++) {
                        RoomDeviceWrapper temporaryWrapper;
                        if (!shutterValues.isEmpty()){
                            temporaryWrapper = findSingleShutter(shutterValues.get(i), name, shutterSpecifier.get(i).ownText());
                        }
                        else {
                            String warningDescription = "No specifiers for the shutters were found. If shutter values were found the specifiers will be incrementally increased.";
                            wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
                            temporaryWrapper = findSingleShutter(shutterValues.get(i), name, "Automatic Specifier " + i++);
                        }
                        wrapper.addDevices(temporaryWrapper.getDevices());
                        wrapper.addUserInformation(temporaryWrapper.getUserInformation());
                    }
                    return wrapper;
                }
                else {
                    // Different amount of shutter values and shutter specifier.
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());

                    // There are more shutter than specifier. For each shutter to which a specifier could be found it will be added. The remaining shutter will get an automatically generated specifier.
                    if (shutterValues.size() > shutterSpecifier.size()) {
                        for (int i = 0; i < shutterSpecifier.size(); i++) {
                            wrapper.combineWrapper(findSingleShutter(shutterValues.get(i), name, shutterSpecifier.get(i).ownText()));
                        }
                        for (int i = shutterSpecifier.size(); i < shutterValues.size(); i++) {
                            wrapper.combineWrapper(findSingleShutter(shutterValues.get(i), name, "Automatic Specifier " + (i + 1 - shutterSpecifier.size())));
                        }
                        String descriptionWarning = "There were more shutters than specifiers. All shutters that could be found were extracted. For the shutters to which no specifiers could be found automatic specifiers were implemented. Please check the website and the documentation.";
                        wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, descriptionWarning))));
                        return wrapper;
                    }
                    else {
                        // There are more specifier than shutters. No reliable mapping of shutter and there specifiers possible. Every shutter gets and automatically generated specifier.
                        for (int i = 0; i < shutterValues.size(); i++) {
                            wrapper.combineWrapper(findSingleShutter(shutterValues.get(i), name, "Automatic Specifier " + i++));
                        }
                        String descriptionWarning = "There was a different amount of shutters and specifiers for them. All shutters that could be found were extracted but no specifiers could be found for them. Automatic specifiers were implemented. Please check the website and the documentation.";
                        wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, descriptionWarning))));
                        return wrapper;
                    }

                }
            }
            else {
                // No second table row which contains the values of the shutters.
                String warningDescription = "The second table row which contains the shutter could not be found. No shutters could be found. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        else{
            // No table Row found.
            String warningDescription = "No table was found in the inner table. The table should contain rows with the shutters. No shutters could be found. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Gets all the data for a single shutter.
     *
     * @param secondDataCell        The second data cell of the table row which contains the shutter.
     * @param name                  The name of the shutter.
     * @param specifier             The specifier of the shutter.
     * @return                      A Wrapper which contains the shutter device and user information that occurred during the process.
     */
    @NonNull
    private static RoomDeviceWrapper findSingleShutter(@NonNull Element secondDataCell, @NonNull String name, @Nullable String specifier) {
        Element shutterFrom = secondDataCell.selectFirst("td > form");
        if (shutterFrom != null) {
            // Get the text of the shutter's button.
            String buttonText;
            Element button = shutterFrom.selectFirst("form > button");
            if (button != null) {
                buttonText = button.text();
            }
            else {
                buttonText = null;
            }

            // Get the percentage and the time of the shutter.
            String[] formInformation = findShutterFormInformation(shutterFrom.ownText());

            return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShShutter(name, specifier, buttonText, formInformation[0], formInformation[1]))), new ArrayList<>());
        }
        else {
            // Shutter form could not be found.
            String warningDescription = "The form element which should contain further information about the shutter of the room could not be found. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Gathers the information of the shutter form and splits the String which contains the percentage of the shutter and the time into two different variables.
     *
     * @param form     The text of the form field.
     * @return         An array with two Strings that represent the percentage of the shutter and the time.
     */
    @NonNull
    private static String[] findShutterFormInformation(@NonNull String form) {
        String[] formInformation = new String[2];

        // Get the percentage of the shutter from the form.
        int positionOfFirstPercentage = form.indexOf('%');

        if (positionOfFirstPercentage >= 0) {
            String afterFirstPercentage = form.substring(positionOfFirstPercentage + 1);

            int positionOfSecondPercentage = afterFirstPercentage.indexOf('%');

            if (positionOfSecondPercentage >= 0) {
                formInformation[0] = afterFirstPercentage.substring(0, positionOfSecondPercentage);
                System.out.println("FormInfo 0: " + formInformation[0] + "; Position: " + positionOfSecondPercentage + "; form: " + form);
            }
            else {
                formInformation[0] = null;
            }
        }
        else {
            formInformation[0] = null;
        }

        // Get the time of the shutter.
        int positionOfFirstBracket = form.indexOf('(');
        int positionOfSecondBracket = form.indexOf(')');
        if (positionOfFirstBracket >= 0 && positionOfSecondBracket >= 0) {
            formInformation[1] = form.substring(positionOfFirstBracket, positionOfSecondBracket + 1);
        }
        else {
            formInformation[1] = null;
        }
        return formInformation;
    }
}
