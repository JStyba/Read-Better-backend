package com.readbetter.main.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.readbetter.main.model.ScrapedWeb;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

@Service
public class ScrapeWebService implements IScrapeWebService {

    @Override
    public String getWebContentAsString(String url) {
        //        String searchUrlQuery = (url.replaceFirst("^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]*", ""));
        if (url.contains("-")) {
            return parseToString(filterUrl(url));
        } else return parseToString(url);
    }

    @Override
    public Optional<ScrapedWeb> createScrapeToSend(String webString) {
        ScrapedWeb scrapedWeb = new ScrapedWeb(webString);
        Optional<ScrapedWeb> optionalEntry = Optional.ofNullable(scrapedWeb);
        return optionalEntry;
    }

    private String filterUrl(String url) {
        if (url.contains("(http://(?!www))") | url.contains("(https://(?!www))")) {
            String tmpUrl = url.substring(7, url.length());
            String mainUrl = tmpUrl.substring(0, tmpUrl.indexOf("/"));
            String secodPart = URLEncoder.encode(url.replace(mainUrl, ""));
            return mainUrl.concat(secodPart);
        } else {
            String[] tmpArray = url.split("/");
            return tmpArray[0] + URLEncoder.encode(tmpArray[1]);

        }
    }

    private String parseToString(String searchedUrl) {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            HtmlPage page = client.getPage(searchedUrl);
            String stringPage = page.getWebResponse().getContentAsString("UTF-8");
            return stringPage;
        } catch (IOException e) {
            e.printStackTrace();
            return "Cannot find the page";
        }
    }
}

