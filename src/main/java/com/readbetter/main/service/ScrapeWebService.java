package com.readbetter.main.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.readbetter.main.model.ScrapedWeb;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ScrapeWebService implements IScrapeWebService {

    @Override
    public String getWebContentAsString(String url) {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        String searchUrl = verifyUrl(url);
        try {
            HtmlPage page = client.getPage(searchUrl);
            String stringPage = page.getWebResponse().getContentAsString("UTF-8");
            return stringPage;
        } catch (IOException e) {
            e.printStackTrace();
            return "Cannot find the page";
        }
    }

    @Override
    public Optional<ScrapedWeb> createScrapeToSend(String webString) {
        ScrapedWeb scrapedWeb = new ScrapedWeb(webString);
        Optional<ScrapedWeb> optionalEntry = Optional.ofNullable(scrapedWeb);
        return optionalEntry;
    }

    public String verifyUrl(String urlToVerify) {
        if (urlToVerify.startsWith("www.")) {
            return "https://" + urlToVerify;
        } else return urlToVerify;
    }

}

