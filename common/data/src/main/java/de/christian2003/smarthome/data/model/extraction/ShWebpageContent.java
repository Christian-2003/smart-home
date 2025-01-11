package de.christian2003.smarthome.data.model.extraction;


import android.content.Context;
import android.content.res.Resources;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.christian2003.smarthome.data.model.extraction.search.room.ShRoomSearch;
import de.christian2003.smarthome.data.model.room.ShRoom;


/**
 * Class models the content of the smart home webpage.
 */
public class ShWebpageContent {

    /**
     * The document which contains the code of the smart home webpage.
     */
    private final Document document;

    private ArrayList<ShRoom> rooms;

    private boolean loadingSuccessful;

    /**
     * Constructor instantiates a new webpage content.
     *
     * @param url           The url of the webpage that should be read.
     */
    public ShWebpageContent(String url, Context context) {
        CountDownLatch latch = new CountDownLatch(1);
        ShWebpageInterface shWebpageInterface = new ShWebpageInterface(context);
        createWebView(url, context, shWebpageInterface, latch);

        try {
            latch.await();
        }
        catch (InterruptedException ex) {
            // EX
        }
        if (loadingSuccessful) {
            document = shWebpageInterface.getDocument();
        }
        else {
            // EX
            document = null;
        }
    }

    private void createWebView(String url, Context context, ShWebpageInterface shWebpageInterface, CountDownLatch latch) {
        WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        // Provide the data of the website that was loaded in the webView in the java code.
        webView.addJavascriptInterface(shWebpageInterface, "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Get the HTML of the website and give it to the handleHtml method.
                loadingSuccessful = true;
                // Achtung
                view.loadUrl("javascript:window.Android.handleHtml(document.documentElement.outerHTML);");
                latch.countDown();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadingSuccessful = false;
                latch.countDown();
            }
        });
        webView.loadUrl(url);
    }


    @NonNull
    public ArrayList<ShRoom> getRooms() {
        return rooms;
    }

    public List<ShRoom> getSmartHomeData() {
        if (document != null) {
            return ShRoomSearch.findAllRooms(document);
        }
        else {
            return null;
        }
    }

    /*
    public ArrayList<ShRoom> getDummyData() {
        // Dummy InfoTexts.
        ShInfoText infoText1 = new ShInfoText("Temperature", null,"30");
        ShInfoText infoText2 = new ShInfoText("Humidity", "Flur", "90");
        ShInfoText infoText3 = new ShInfoText("Air pressure", null,"80");
        ShInfoText infoText4 = new ShInfoText("Temperature", "WZ" ,"30");
        ShInfoText infoText5 = new ShInfoText("Temperature", "Flur","23");
        ShInfoText infoText6 = new ShInfoText("Temperature", null,"12");

        ArrayList<ShInfoText> infoTextArrayList1 = new ArrayList<>();
        infoTextArrayList1.add(infoText1);
        infoTextArrayList1.add(infoText2);
        infoTextArrayList1.add(infoText3);

        ArrayList<ShInfoText> infoTextArrayList2 = new ArrayList<>();
        infoTextArrayList2.add(infoText4);

        ArrayList<ShInfoText> infoTextArrayList3 = new ArrayList<>();
        infoTextArrayList3.add(infoText5);

        ArrayList<ShInfoText> infoTextArrayList4 = new ArrayList<>();
        infoTextArrayList4.add(infoText6);

        // Dummy devices.
        ShGenericDevice device1 = new ShOpening("Window1", null);
        ShGenericDevice device2 = new ShOpening("Window2", null);

        ShGenericDevice device3 = new ShShutter("Shutter1", null, "90", "9");

        ArrayList<ShGenericDevice> deviceArrayList1 = new ArrayList<>();
        deviceArrayList1.add(device1);
        deviceArrayList1.add(device2);

        ArrayList<ShGenericDevice> deviceArrayList2 = new ArrayList<>();
        deviceArrayList2.add(device3);

        // Dummy list of rooms for testing.
        ArrayList<ShRoom> dummyRooms = new ArrayList<>();
        dummyRooms.add(new ShRoom("Test-Room1",  null, deviceArrayList2));
        dummyRooms.add(new ShRoom("Test-Room2", infoTextArrayList1, null));
        dummyRooms.add(new ShRoom("Test-Room3", infoTextArrayList2, deviceArrayList1));
        dummyRooms.add(new ShRoom("Test-Room4", infoTextArrayList3, null));
        dummyRooms.add(new ShRoom("Test-Room5", infoTextArrayList4, null));
        return dummyRooms;
    }*/

    /**
     * Prints all the rooms and their properties that belong to the ShWebpageContent object.
     *
     * @param shWebpageContent          The object which has attribute that contains all the rooms of the smart home and their properties.
     */
    public static void printElement(@NonNull ShWebpageContent shWebpageContent) {
        System.out.println("Länge: " + shWebpageContent.rooms.size());
        for (ShRoom room: shWebpageContent.rooms) {
            ShRoomSearch.printOutRoom(room);
        }
    }
}

