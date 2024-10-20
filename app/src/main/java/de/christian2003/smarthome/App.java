package de.christian2003.smarthome;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.model.data.ShWebpageContent;


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
        // Try catch only needed temporary until we have access to the smart home webpage.
        Document document = null;
        try {
            document = ShWebpageContent.getWebpageHtml(this);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ShWebpageContent shWebpageContent = new ShWebpageContent(document);
            ArrayList<ShRoom> allRooms = shWebpageContent.getAllShData();
            System.out.println("Anzahl Räume: " + allRooms.size());
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
