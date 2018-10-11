package com.readbetter.main.controller;

import com.readbetter.main.model.ScrapedWeb;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.service.ScrapeWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
public class ScrapeWebController {



    @Autowired
    private ScrapeWebService scrapeWebService;

    @RequestMapping(path = "/scrape", method = RequestMethod.GET)
    public ResponseEntity getEntry(@RequestParam(name = "url") String url) throws IOException {

        Optional<ScrapedWeb> stringContent = scrapeWebService.createScrapeToSend(scrapeWebService.getWebContentAsString(url));
        if (stringContent.isPresent()) {
            return RespFactory.result(stringContent.get());
        }
        return RespFactory.badRequest();
    }
}
