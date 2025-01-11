package de.christian2003.smarthome.data.model.extraction;

import android.content.Context;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.CountDownLatch;

public class ShWebpageInterface {
    @NonNull
    private Context context;
    @Nullable
    private Document document;

    public CountDownLatch latch;

    public ShWebpageInterface (@NonNull Context context, CountDownLatch latch) {
        this.context = context;
        this.latch = latch;
    }

    @JavascriptInterface
    public void handleHtml(String html) {
        System.out.println("HandleHtml");
        createDocument(html);
    }

    private void createDocument(String html) {
        this.document = Jsoup.parse(html);
        latch.countDown();
    }

    @Nullable
    public Document getDocument() {
        return document;
    }
}
