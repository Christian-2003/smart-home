package de.christian2003.smarthome.data.model.extraction;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import org.jsoup.nodes.Document;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import de.christian2003.smarthome.data.model.cert.CertHandler;
import de.christian2003.smarthome.data.model.extraction.search.room.ShRoomSearch;
import de.christian2003.smarthome.data.model.room.ShRoom;
import de.christian2003.smarthome.data.model.userinformation.InformationTitle;
import de.christian2003.smarthome.data.model.userinformation.InformationType;
import de.christian2003.smarthome.data.model.userinformation.UserInformation;


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
     * Contains errors that occur while loading the website.
     */
    @NonNull
    private final ArrayList<UserInformation> loadingInformation;

    /**
     * Constructor instantiates a new webpage content.
     *
     * @param url           The url of the webpage that should be read.
     */
    public ShWebpageContent(String url, Context context, ShWebpageContentCallback callback) {
        CountDownLatch latch = new CountDownLatch(1);
        ShWebpageInterface shWebpageInterface = new ShWebpageInterface(latch);
        loadingInformation = new ArrayList<>();

        new Thread(()-> {
            CertHandler certHandler = new CertHandler(context);
            SSLContext sslContext = certHandler.getSSLContext(); //This method must not be called from main thread!
            new Handler(Looper.getMainLooper()).post(() -> createWebView(url, context, shWebpageInterface, sslContext));

            try {
                latch.await();
            }
            catch (InterruptedException interruptedException) {
                String errorDescription = "There was an interruption while loading the website. \nError message:\n" + interruptedException.getMessage();
                loadingInformation.add(new UserInformation(InformationType.ERROR, InformationTitle.LoadingInterruption, errorDescription));
            }

            if (shWebpageInterface.isLoadingSuccessful()) {
                document = shWebpageInterface.getDocument();
                callback.onPageLoadComplete(true);
            }
            else {
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
     */
    private void createWebView(String url, Context context, ShWebpageInterface shWebpageInterface, SSLContext sslContext)  {
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

        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            webView.loadUrl(url);
        }
        catch (Exception e) {
            loadingInformation.add(new UserInformation(InformationType.ERROR, InformationTitle.UnknownError, "An unknown error occurred while loading the website with the certificate. \nError message:\n" + e.getMessage()));
        }
    }

    /**
     * Gathers all the data of the Smart Home.
     *
     * @return  A list of all the rooms of the Smart Home and their properties.
     */
    @Nullable
    public ArrayList<ShRoom> getSmartHomeData() {
        if (document != null) {
            ShRoomSearch shRoomSearch = new ShRoomSearch();
            ArrayList<ShRoom> rooms = shRoomSearch.findAllRooms(document);
            this.rooms = rooms;
            return rooms;
        }
        else {
            return null;
        }
    }

    /**
     * Gets the list of errors that occurred while loading the webpage.
     *
     * @return  List with the errors that occurred while loading the webpage.
     */
    @NonNull
    public ArrayList<UserInformation> getLoadingInformation() {
        return loadingInformation;
    }

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

