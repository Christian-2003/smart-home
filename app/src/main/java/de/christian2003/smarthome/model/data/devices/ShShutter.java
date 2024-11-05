package de.christian2003.smarthome.model.data.devices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


/**
 * Class models a shutter for the smart home device.
 */
public class ShShutter extends ShGenericDevice {

    /**
     * Attribute specifies the device.
     */
    @Nullable
    private final String specifier;

    /**
     * Attribute stores the text for the button to set the percentage to which to close the
     * shutter.
     */
    @Nullable
    private final String setButtonText;

    /**
     * Attribute stores the percentage to which the shutter is closed. This is {@code null} if no
     * percentage is provided.
     */
    @Nullable
    private String percentage;

    /**
     * Attribute stores the time at which the shutter was closed? This is {@code null} if no time
     * is provided.
     */
    @Nullable
    private String time;


    /**
     * Constructor instantiates a new shutter for the smart home.
     *
     * @param name          Name for the shutter.
     * @param specifier     Specifies the device.
     * @param setButtonText Text for the button through which to set the percentage to which the
     *                      shutter is closed.
     * @param percentage    Percentage to which the shutter is closed.
     * @param time          Time at which the shutter was closed?
     */
    public ShShutter(@NonNull String name, @Nullable String specifier, @Nullable String setButtonText, @Nullable String percentage, @Nullable String time) {
        super(name, null);
        this.specifier = specifier;
        this.setButtonText = setButtonText;
        this.percentage = percentage;
        this.time = time;
    }

    /**
     *Finds the shutters of a room and creates devices for them.
     *
     * @param tableRow      The table row which contains the cells with the shutters.
     * @param name          The name of the room in which the shutter is.
     * @return              A RoomDeviceWrapper which contains a list of all shutters that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomDeviceWrapper createShutterDevice(Element tableRow, String name) {
        Element firstDataCell = tableRow.selectFirst("tr > td");

        if (firstDataCell != null) {
            System.out.println("First data cell gefunden");
            Element secondDataCell = tableRow.selectFirst("tr > td ~ td");
            if (secondDataCell != null) {
                System.out.println("Second data cell gefunden");
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
     * Fins multiple shutters, creates devices for them and returns them with all warnings/ errors that occurred while finding them.
     *
     * @param innerTable        The inner table which is present when the room contains multiple shutters.
     * @param name              The name of the shutter in the rooms combined with the room name.
     * @return                  A RoomDeviceWrapper which contains a list of all shutters that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomDeviceWrapper findMultipleShutter(Element innerTable, String name) {
        Element firstTableRow = innerTable.selectFirst("table > tr");

        if (firstTableRow != null) {
            Elements shutterSpecifier = firstTableRow.select("tr > td");
            Element secondTableRow = innerTable.selectFirst("table > tr + tr");

            if (secondTableRow != null) {
                Elements shutterValues = secondTableRow.select("tr > td");

                if (shutterValues.size() == shutterSpecifier.size() || shutterSpecifier.isEmpty()) {
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());
                    for (int i = 0; i < shutterValues.size(); i++) {
                        RoomDeviceWrapper temporaryWrapper;
                        if (shutterValues.isEmpty()){
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
                    RoomDeviceWrapper wrapper;
                    // Implement method that finds shutters.
                    if (shutterValues.size() > shutterSpecifier.size())
                    for (int i = 0; i < shutterSpecifier.size(); i++){
                        wrapper = findSingleShutter(shutterValues.get(i), name, shutterSpecifier.get(i).ownText());
                    }
                    for (int i = shutterSpecifier.size(); i < shutterValues.size(); i++){
                        wrapper.combineWrapper(findSingleShutter(shutterValues.get(i), name, "Automatic Specifier " + ( ++i - shutterSpecifier.size())));
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
    public static RoomDeviceWrapper checkSpecifier(Element innerTable, Elements shutterSpecifier, String name) {
        Element secondTableRow = innerTable.selectFirst("table > tr + tr");

        if (secondTableRow != null) {
            Elements shutterValues = secondTableRow.select("tr > td");

            if (shutterValues.size() == shutterSpecifier.size() || shutterSpecifier.isEmpty()) {
                RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());
                for (int i = 0; i <shutterValues.size(); i++) {
                    RoomDeviceWrapper temporaryWrapper;
                    if (shutterValues.isEmpty()){
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
            }
        }
        else {
            // No second table row which contains the values of the shutters.
        }
    }**/

    /**
     * Gets all the data for a single shutter.
     *
     * @param secondDataCell        The second data cell of the table row which contains the shutter.
     * @param name                  The name of the shutter.
     * @param specifier             The specifier of the shutter.
     * @return                      A Wrapper which contains the shutter device and user information that occurred during the process.
     */
    @NonNull
    public static RoomDeviceWrapper findSingleShutter(Element secondDataCell, String name, String specifier) {
        Element shutterFrom = secondDataCell.selectFirst("td > form");
        if (shutterFrom != null) {
            System.out.println("Shutter Form gefunden");

            // Get the text of the shutter's button.
            String buttonText;
            Element button = shutterFrom.selectFirst("form > button");
            if (button != null) {
                System.out.println("Shutter input gefunden");
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
     * @return         Returns an array with two Strings that represent the percentage of the shutter and the time.
     */
    @NonNull
    public static String[] findShutterFormInformation(String form) {
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

    /**
     * Method returns the specifier of the info text.
     *
     * @return Specifier of the info text.
     */
    @Nullable
    public String getSpecifier() {
        return specifier;
    }

    /**
     * Method returns the text for the button through which to set the percentage to which the
     * shutter is closed.
     *
     * @return  Percentage to which the shutter is closed.
     */
    @Nullable
    public String getSetButtonText() {
        return setButtonText;
    }

    /**
     * Method returns the percentage to which the shutter is closed. This returns {@code null} if no
     * percentage is provided.
     *
     * @return  Percentage to which the shutter is closed.
     */
    @Nullable
    public String getPercentage() {
        return percentage;
    }

    /**
     * Method returns the time at which the shutter was closed. This returns {@code null} if no time
     * is provided.
     *
     * @return  Time at which the shutter was closed
     */
    @Nullable
    public String getTime() {
        return time;
    }

}
