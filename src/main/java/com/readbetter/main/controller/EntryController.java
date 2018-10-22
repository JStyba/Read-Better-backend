package com.readbetter.main.controller;

import com.readbetter.main.model.Definition;
import com.readbetter.main.model.Entry;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.repository.EntryRepository;
import com.readbetter.main.service.EntryService;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/entry/")
public class EntryController {


    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private EntryService entryService;

    //    @RequestMapping(path = "/get", method = RequestMethod.GET)
//    public String getEntry(@RequestParam(name = "searchedWord") String searchedWord) throws IOException {
//        String wordDefinition = entryService.findWordDefinition(searchedWord);
//        return wordDefinition;
//    }
    @RequestMapping(path = "/translate", method = RequestMethod.GET)
    public ResponseEntity getEntry(@RequestParam(name = "word") String word) throws IOException {
        String inflection = entryService.getInflection(entryService.findInflection(word));
        List<Definition> defOpt = entryService.cleanedDefinitionsFromJson(entryService.getDefinitions(entryService.getDictionaryJson(inflection)));

        Optional<Entry> entry = entryService.createEntryToSend(word, defOpt);
        if (entry.isPresent()) {
            return RespFactory.result(entry.get());
        }
        return RespFactory.badRequest();
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public List<Entry> getAllEntries() {
    return entryRepository.findAll();
    }

}
