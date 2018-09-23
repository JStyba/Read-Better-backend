package com.readbetter.main.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class ScrapedWeb {
private String scrapedWebString;
@Autowired
    public ScrapedWeb(String scrapedWebString) {
        this.scrapedWebString = scrapedWebString;
    }
}
