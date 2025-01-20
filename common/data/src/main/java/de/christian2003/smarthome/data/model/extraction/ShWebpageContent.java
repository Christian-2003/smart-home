package de.christian2003.smarthome.data.model.extraction;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
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
    @Nullable
    private  Document document;

    /**
     * The rooms of the Smart Home.
     */
    @Nullable
    private ArrayList<ShRoom> rooms;


    /**
     * Constructor instantiates a new webpage content.
     *
     * @param url           The url of the webpage that should be read.
     */
    public ShWebpageContent(String url, Context context, ShWebpageContentCallback callback) {
        CountDownLatch latch = new CountDownLatch(1);
        ShWebpageInterface shWebpageInterface = new ShWebpageInterface(latch);

        new Thread(()-> {

            new Handler(Looper.getMainLooper()).post(() -> createWebView(url, context, shWebpageInterface, latch));

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shWebpageInterface.isLoadingSuccessful()) {
                document = shWebpageInterface.getDocument();
                callback.onPageLoadComplete(true);
            }
            else {
                System.out.println("Fehler beim Laden der Seite.");
                callback.onPageLoadComplete(false);
            }
        }).start();
    }

    /**
     * Creates a Web View, loads the webpage with the given url and gets the html code of the given webpage.
     *
     * @param url   The url of the webpage that should be loaded.
     * @param context   The current context.
     * @param shWebpageInterface    Handling the parsing of the code of the loaded website to a document.
     * @param latch     Latch to notify when the website is loaded or an error occurred.
     */
    private void createWebView(String url, Context context, ShWebpageInterface shWebpageInterface, CountDownLatch latch) {
        System.out.println("CearetWebView");
        WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.clearCache(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Provide the data of the website that was loaded in the webView in the java code.
        webView.addJavascriptInterface(shWebpageInterface, "Android");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Get the HTML of the website and give it to the handleHtml method.
                System.out.println("fin");

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    view.loadUrl("javascript:(function() {" +
                            "    if (document.readyState === 'complete') {" +
                            "        console.log('Alle Ressourcen sind geladen.');" +
                            "        window.Android.handleHtml(document.documentElement.outerHTML);" +
                            "        Android.notifyPageLoadComplete(true);" +
                            "    } else {" +
                            "        window.addEventListener('load', function() {" +
                            "            console.log('Alle Ressourcen sind geladen (mit onload Event).');" +
                            "            window.Android.handleHtml(document.documentElement.outerHTML);" +
                            "            Android.notifyPageLoadComplete(true);" +
                            "        });" +
                            "    }" +
                            "})();");
                }, 5000);
            }
        });
        webView.loadUrl(url);
    }

    /**
     * Gathers all the data of the Smart Home.
     *
     * @return  A list of all the rooms of the Smart Home and their properties.
     */
    @Nullable
    public ArrayList<ShRoom> getSmartHomeData() {
        if (document != null) {
            System.out.println("Funktioniert");
            //System.out.println("HTML: " + document.html());
            ShRoomSearch shRoomSearch = new ShRoomSearch();
            ArrayList<ShRoom> test = shRoomSearch.findAllRooms(document);
            this.rooms = test;
            printElement(this);
            return test;
        }
        else {
            System.out.println("Dok null");
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

