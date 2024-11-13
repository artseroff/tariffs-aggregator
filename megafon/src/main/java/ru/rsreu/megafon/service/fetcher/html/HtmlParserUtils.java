package ru.rsreu.megafon.service.fetcher.html;

import java.net.URI;

public final class HtmlParserUtils {
    private HtmlParserUtils() {

    }

    public static String cutEnding(String source, String ending) {
        return source.substring(0, source.length() - ending.length());
    }

    public static URI combineUrl(String baseUrl, String endpoint) {
        return URI.create(baseUrl + endpoint);
    }
}
