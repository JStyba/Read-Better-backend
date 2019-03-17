package com.readbetter.main.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.readbetter.main.exceptions.ElementNotFound;
import com.readbetter.main.model.*;
import com.readbetter.main.model.dto.RespFactory;
import com.readbetter.main.model.dto.Response;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.EntryRepository;
import com.readbetter.main.service.EntryService;
import com.readbetter.main.service.LocalDictService;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/entry/")
public class EntryController {


    private EntryRepository entryRepository;
    private EntryService entryService;
    private AppUserRepository appUserRepository;
    private LocalDictService localDictService;

    @Autowired
    public void setLocalDictService(LocalDictService localDictService) {
        this.localDictService = localDictService;
    }

    @Autowired
    public void setEntryRepository(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Autowired
    public void setEntryService(EntryService entryService) {
        this.entryService = entryService;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

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

    @RequestMapping(path = "/translate-polish", method = RequestMethod.GET)
    public ResponseEntity getEntryPl(@RequestParam(name = "word") String word) throws IOException {
        String inflection = entryService.getInflection(entryService.findInflection(word));
        List<Definition> defOpt = (entryService.getTranslationsPl(entryService.getDictionaryJsonPl(inflection)));
        Optional<Entry> entry = entryService.createEntryToSend(word, defOpt);
        if (entry.isPresent()) {
            return RespFactory.result(entry.get());
        }
        return RespFactory.badRequest();
    }

    @RequestMapping(path = "/get-entries", method = RequestMethod.GET)
    public List<Entry> getAllEntriesByUser(@RequestParam(name = "username") String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Entry> entryList = entryService.getAllEntries(user.get().getId());
            return entryList;
        }
        return null;
    }

    @RequestMapping(path = "/get-entries-with-def", method = RequestMethod.GET)
    public List<Entry> getAllEntriesWithDefByUser(@RequestParam(name = "username") String username) throws IOException {


        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Entry> entryList = entryService.getAllEntries(user.get().getId());
            for (Entry e :
                    entryList) {
                String inflection = entryService.getInflection(entryService.findInflection(e.getWord()));
                List<Definition> defOpt = entryService.cleanedDefinitionsFromJson(entryService.getDefinitions(entryService.getDictionaryJson(inflection)));
                e.setDefinitions(defOpt);
            }
            return entryList;
        }
        return null;
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    @RequestMapping(path = "/def", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> deff(
            @RequestParam(name = "username") String username,
            @RequestBody JsonNode entryList) {

        Optional<AppUser> user = appUserRepository.findByUsername(username);
        for (JsonNode n : entryList) {
            Entry entry = new Entry();
            entry.setWord(n.get("word").asText());
            entry.setAppUser(user.get());
            entry.setEntryUrl(n.get("entryUrl").asText());
            entry.setTimestamp(n.get("timestamp").asText());
            entry.setLanguage(n.get("language").asText());
            if (!entryRepository.findByWord(entry.getWord()).isPresent()) {

                entryService.addEntryToDatabse(entry);
            }
        }

        return RespFactory.created();
    }

    @RequestMapping(path = "/remove-entry", method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<Response> removeEntry(@RequestParam(name = "word") String word, @RequestParam(name = "username") String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        Optional<Entry> entry = entryRepository.findByWord(word);
        try {
            entryService.deleteEntry(word, user.get());
        } catch (ElementNotFound e) {
            return RespFactory.badRequest();
        }
        return RespFactory.ok("Entry deleted");
    }

    //    @RequestMapping(path = "/pronunciation", method = RequestMethod.GET)
//    public List<Pronunciation> getPronunciation(@RequestParam(name = "word") String word) throws IOException {
//        String inflection = entryService.getInflection(entryService.findInflection(word));
//        return entryService.getPronunciationBrE(entryService.getDictionaryPronunciation(inflection));
//    }
    @RequestMapping(path = "/read-dict", method = RequestMethod.GET)
    public ResponseEntity<String> sendDict() throws IOException {
        String dict = localDictService.readFile();
        return RespFactory.result(dict);
    }
    @RequestMapping(path = "/learn", method = RequestMethod.GET)
    public ResponseEntity<Response> setIntervals (@RequestParam(name = "word") String word, @RequestParam(name = "username") String username, @RequestParam(name = "correct") boolean correct) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        Optional<Entry> entry = entryRepository.findByWord(word);
        if (correct) {
           entry.get().setRepMap(entryService.returnNextInterval(entry.get(), true));
            entryRepository.saveAndFlush(entry.get());
        } else if (!correct) {
            entry.get().setRepMap(entryService.returnNextInterval(entry.get(), false));
            entryRepository.saveAndFlush(entry.get());
        } else return RespFactory.badRequest();
        return RespFactory.ok("Interval updated");
    }
    
    @RequestMapping(path = "/get-words-to-learn", method = RequestMethod.GET)
    public List<Entry> getWordsToLearn(@RequestParam(name = "username") String username) {
        Optional<AppUser> user = appUserRepository.findByUsername(username);
        if (user.isPresent()) {
            List<Entry> entryList = entryService.getAllEntries(user.get().getId());

            return entryService.getEntriesToLearn(entryList);
        }
        return null;
    } 
}
