package com.funixproductions.api.google.gmail.client.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

public class GmailUtils {

    public static String minifyHtml(final String html) {
        final Document document = Jsoup.parse(html);

        document.outputSettings().prettyPrint(false);
        document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        return document.html();
    }

}
