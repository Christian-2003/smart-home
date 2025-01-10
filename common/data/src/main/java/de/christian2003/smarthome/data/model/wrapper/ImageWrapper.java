package de.christian2003.smarthome.data.model.wrapper;

import android.net.Uri;
import androidx.annotation.Nullable;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


/**
 * Wrapper for returning a Uri of an image of a device and a user information.
 */
public class ImageWrapper {
    /**
     * Attribute stores the Uri of the image.
     */
    @Nullable
    private final Uri imageUri;

    /**
     * Attribute stores a user information.
     */
    @Nullable
    private final UserInformation userInformation;

    /**
     * Constructor instantiates a new {@link ImageWrapper} to return the Uri and user information form the methods that gather the images.
     *
     * @param imageUri      The Uri of the image.
     * @param userInformation       The user information that might occur while gathering the properties.
     */
    public ImageWrapper(@Nullable Uri imageUri, @Nullable UserInformation userInformation) {
        this.imageUri = imageUri;
        this.userInformation = userInformation;
    }

    /**
     * Method returns the Uri of the image.
     *
     * @return      A Uri object.
     */
    @Nullable
    public Uri getImageUri() {
        return imageUri;
    }

    /**
     * Method returns the user information.
     *
     * @return      A {@link UserInformation} object.
     */
    @Nullable
    public UserInformation getUserInformation() {
        return userInformation;
    }

}
