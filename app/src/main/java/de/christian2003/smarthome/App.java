package de.christian2003.smarthome;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.christian2003.smarthome.model.data.ShWebpageContent;
import de.christian2003.smarthome.model.data.rooms.ShLivingRoom;


/**
 * This class stores the global context and executor service for the app. This allows for easier
 * access to a global context from within the model.
 */
public class App extends Application {

    /**
     * Field stores the context from this {@link Application}.
     */
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * Field stores the executor service used to asynchronously execute code.
     */
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);


    /**
     * Method is called whenever the application is created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // Get the webpage and if it the content was retrieved successfully get the information of the rooms and their devices.
        Document document = ShWebpageContent.getWebpageHtml();
        try {
            ShWebpageContent shWebpageContent = new ShWebpageContent(document);
            ShLivingRoom.getLivingRoomData(document);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
            // Display error on UI.
        }
    }

    /**
     * Method returns an executor which can be used to asynchronously execute code.
     *
     * @return  Executor for asynchronous code execution.
     */
    public static Executor getExecutor() {
        return executorService;
    }

    /**
     * Method returns the application context.
     *
     * @return  Application context.
     */
    public static Context getContext() {
        return context;
    }

}
