package de.christian2003.smarthome.model.data.devices;

import android.graphics.Path;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.data.wrapper_class.RoomDeviceWrapper;
import de.christian2003.smarthome.model.user_information.InformationTitle;
import de.christian2003.smarthome.model.user_information.InformationType;
import de.christian2003.smarthome.model.user_information.UserInformation;


/**
 * Class models an opening in the house (i.e. a door or window).
 */
public class ShOpening extends ShGenericDevice {

    /**
     * The {@link ShOpeningType} of the opening.
     */
    @NonNull
    private final ShOpeningType openingType;

    /**
     * Constructor instantiates a new door / window.
     *
     * @param name  Name for the door / window.
     * @param openingType   The type of the opening (window / door).
     * @param specifier     Specifies the opening.
     * @param imageUri      String that represents the URI to the image for the door / window.
     */
    public ShOpening(@NonNull String name, @NonNull ShOpeningType openingType, @Nullable String specifier, @Nullable String imageUri) {
        super(name, specifier, imageUri);
        this.openingType = openingType;
    }

    /**
     * Finds the openings of a room and creates devices for them.
     *
     * @param tableRow      The table row which contains the cells with the openings.
     * @param roomName      The name of the room in which the opening is.
     * @return      A RoomDeviceWrapper which contains a list of all openings that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomDeviceWrapper createOpeningDevice (@NonNull Element tableRow, @NonNull String roomName) {
        Element firstDataCell = tableRow.selectFirst("tr > td");

        // Find the data cell which contains the opening.
        if (firstDataCell != null) {
            ShOpeningType openingType = checkOpeningType(firstDataCell.ownText());

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

    /**
     * Checks the opening type of the element.
     *
     * @param firstDataCell     The first data cell which should contain the opening type.
     * @return      The {@link ShOpeningType} of the opening.
     */
    @NonNull
    public static ShOpeningType checkOpeningType(@NonNull String firstDataCell) {
        if (firstDataCell.toLowerCase().contains("fenster")) {
            return ShOpeningType.Window;
        }
        else if (firstDataCell.toLowerCase().contains("tür") || firstDataCell.toLowerCase().contains("tuer")) {
            return ShOpeningType.Door;
        }
        else {
            return ShOpeningType.Unkown;
        }
    }

    /**
     * Finds a single opening and further properties of it.
     *
     * @param secondDataCell    The second data cell of the table row which contains the opening.
     * @param openingName   The name of the opening.
     * @param openingType   The type of the opening.
     * @param specifier     The specifier of the opening.
     * @return  A {@link RoomDeviceWrapper} which contains the opening and a list of the warnings that occurred while gathering the information.
     */
    @NonNull
    public static RoomDeviceWrapper findSingleOpening(@NonNull Element secondDataCell, @NonNull String openingName, @NonNull ShOpeningType openingType, @Nullable String specifier) {
        Element image = secondDataCell.selectFirst("td  img");
        if (image != null) {
            String source = image.attr("src");

            // Check if a source could be found.
            if (!source.isEmpty()){
               return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, source))), new ArrayList<>());
            }
            // No image source found for opening but object can be created anyways.
            else {
                System.out.println("Source Empty");
                String warningDescription = "An image node could be found but not source for the image. The opening could be created anyways. Please check the website and the documentation.";
                return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
            }
        }
        // No image found for opening but object can be created anyways.
        else {
            System.out.println("keine Image node");
            String warningDescription = "No image could be found for the opening but it could be created anyways. Please check the website and the documentation.";
            return new RoomDeviceWrapper(new ArrayList<>(Collections.singletonList(new ShOpening(openingName, openingType, specifier, null))), new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
        }
    }

    /**
     * Finds multiple openings of the room.
     *
     * @param innerTable    The inner table which is present when the room contains multiple openings.
     * @param openingName   The name of the openings in the rooms combined with the room name.
     * @param openingType   The {@link ShOpeningType} of the opening.
     * @return      A RoomDeviceWrapper which contains a list of all openings that were found in the room and a list of all warning/ errors that occurred while finding them.
     */
    @NonNull
    public static RoomDeviceWrapper findMultipleOpenings(@NonNull Element innerTable, @NonNull String openingName, @NonNull ShOpeningType openingType) {
        // Get the table row which contains the specifiers of each opening and the table row with the properties of the openings.
        Element firstTableRow = innerTable.selectFirst("table > tbody >tr");

        if (firstTableRow != null) {
            Elements openingsSpecifier = firstTableRow.select("tr > td");
            Element secondTableRow = innerTable.selectFirst("table > tbody > tr + tr");

            if (secondTableRow != null) {
                Elements specifierValues = secondTableRow.select("tr > td");

                // Check if there is a opening specifier for every opening or if there are no specifiers.
                if (specifierValues.size() == openingsSpecifier.size() || openingsSpecifier.isEmpty()) {
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());
                    for (int i = 0; i < specifierValues.size(); i++) {
                        RoomDeviceWrapper temporaryWrapper;
                        if (!specifierValues.isEmpty()){
                            temporaryWrapper = findSingleOpening(specifierValues.get(i), openingName, openingType, openingsSpecifier.get(i).ownText());
                        }
                        else {
                            String warningDescription = "No specifiers for the openings were found. If opening values were found the specifiers will be incrementally increased.";
                            wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, warningDescription))));
                            temporaryWrapper = findSingleOpening(specifierValues.get(i), openingName, openingType,"Automatic Specifier " + i++);
                        }
                        wrapper.addDevices(temporaryWrapper.getDevices());
                        wrapper.addUserInformation(temporaryWrapper.getUserInformation());
                    }
                    return wrapper;
                }
                else {
                    // Different amount of opening values and opening specifier.
                    RoomDeviceWrapper wrapper = new RoomDeviceWrapper(new ArrayList<>(), new ArrayList<>());

                    // There are more openings than specifier. For each opening to which a specifier could be found it will be added. The remaining openings will get an automatically generated specifier.
                    if (specifierValues.size() > openingsSpecifier.size()) {
                        for (int i = 0; i < openingsSpecifier.size(); i++) {
                            wrapper.combineWrapper(findSingleOpening(specifierValues.get(i), openingName, openingType,openingsSpecifier.get(i).ownText()));
                        }
                        for (int i = openingsSpecifier.size(); i < specifierValues.size(); i++) {
                            wrapper.combineWrapper(findSingleOpening(specifierValues.get(i), openingName, openingType,"Automatic Specifier " + (i + 1 - openingsSpecifier.size())));
                        }
                        String descriptionWarning = "There were more openings than specifiers. All openings that could be found were extracted. For the openings to which no specifiers could be found automatic specifiers were implemented. Please check the website and the documentation.";
                        wrapper.addUserInformation(new ArrayList<>(Collections.singletonList(new UserInformation(InformationType.WARNING, InformationTitle.HtmlElementNotLocated, descriptionWarning))));
                        return wrapper;
                    }
                    else {
                        // There are more specifier than openings. No reliable mapping of openings and there specifiers possible. Every openings gets and automatically generated specifier.
                        for (int i = 0; i < specifierValues.size(); i++) {
                            wrapper.combineWrapper(findSingleOpening(specifierValues.get(i), openingName, openingType,"Automatic Specifier " + i++));
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

    /**
     * Method returns the {@link ShOpeningType} of the opening.
     *
     * @return The {@link ShOpeningType} of the opening.
     */
    @NonNull
    public ShOpeningType getOpeningType() {
        return openingType;
    }
}
