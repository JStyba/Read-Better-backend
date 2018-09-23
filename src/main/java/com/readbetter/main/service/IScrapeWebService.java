package com.readbetter.main.service;

import com.readbetter.main.model.ScrapedWeb;

import java.util.Optional;

public interface IScrapeWebService {

    String getWebContentAsString(String url);

    Optional<ScrapedWeb> createScrapeToSend(String webString);
}
