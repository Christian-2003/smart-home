package de.christian2003.smarthome.view.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.model.data.ShWebpageContent;


/**
 * Class implements the view model for the {@link HomeFragment}.
 */
public class HomeViewModel extends ViewModel {

    /**
     * Field stores the state indicating that the view model has not been initialized.
     */
    private static final int STATE_WAITING = 0;

    /**
     * Field stores the state indicating that the view model is currently instantiating.
     */
    private static final int STATE_LOADING = 1;

    /**
     * Field stores the state indicating that the view model has been instantiated.
     */
    private static final int STATE_FINISHED = 2;


    /**
     * Attribute stores the stat of the view model. This is always one of the following:
     * {@link #STATE_WAITING}, {@link #STATE_LOADING} or {@link #STATE_FINISHED}.
     */
    private int state;

    /**
     * Attribute stores the webpage content from which to source the data.
     */
    @Nullable
    private ShWebpageContent webpageContent;

    /**
     * Attribute stores a list of all rooms.
     */
    @NonNull
    private final List<ShRoom> rooms;


    /**
     * Constructor instantiates a new view model.
     */
    public HomeViewModel() {
        state = STATE_WAITING;
        webpageContent = null;
        rooms = new ArrayList<>();
    }


    /**
     * Method returns a list of all rooms.
     *
     * @return  List of all rooms.
     */
    @NonNull
    public List<ShRoom> getRooms() {
        if (rooms.isEmpty() && webpageContent != null) {
            rooms.addAll(webpageContent.getRooms());
        }
        return rooms;
    }


    /**
     * Method instantiates the view model only if it was not already instantiated. Afterwards, the
     * passed callback is invoked.
     *
     * @param callback                      Callback to invoke once the view model is instantiated.
     * @param invokeCallbackIfInstantiated  Whether to invoke the callback if the view model is
     *                                      already instantiated when this method is called.
     */
    public void initIfRequired(@Nullable Runnable callback, boolean invokeCallbackIfInstantiated) {
        if (state == STATE_WAITING) {
            state = STATE_LOADING;
            Thread thread = new Thread(() -> {
                webpageContent = ShWebpageContent.getWebpageHtml();
                state = STATE_FINISHED;
                if (callback != null) {
                    callback.run();
                }
            });
            thread.start();
        }
        else if (state == STATE_FINISHED && invokeCallbackIfInstantiated && callback != null) {
            callback.run();
        }
    }

}
