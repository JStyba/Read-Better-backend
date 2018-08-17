package com.readbetter.main.controller;

import com.readbetter.main.model.Entry;
import com.readbetter.main.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
//@RequestMapping("/entry/")
public class EntryController {



    @Autowired
    private EntryService entryService;

    //    @RequestMapping(path = "/get", method = RequestMethod.GET)
//    public String getEntry(@RequestParam(name = "searchedWord") String searchedWord) throws IOException {
//        String wordDefinition = entryService.findWordDefinition(searchedWord);
//        return wordDefinition;
//    }
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Entry getEntry(@RequestParam(name = "word") String word) throws IOException {

       List<String> def = entryService.cleanedDefinitionsFromJson(entryService.getDefinitions(entryService.getDictionaryJson(word)));
       return entryService.createEntryToSend(word, def);

    }


}
