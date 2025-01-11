package de.christian2003.smarthome.data.model.extraction;

import android.content.Context;
import android.webkit.JavascriptInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ShWebpageInterface {
    @NonNull
    private Context context;
    @Nullable
    private Document document;

    public ShWebpageInterface (@NonNull Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void handleHtml(String html) {
        createDocument(html);
    }

    private void createDocument(String html) {
        this.document = Jsoup.parse(html);
    }

    @Nullable
    public Document getDocument() {
        return document;
    }
}
