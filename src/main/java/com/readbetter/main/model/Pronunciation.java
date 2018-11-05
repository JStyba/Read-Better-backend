package com.readbetter.main.model;

public class Pronunciation {
    private String audioLink;
    private String lexicalCategory;

    public Pronunciation(String audioLink, String lexicalCategory) {
        this.audioLink = audioLink;
        this.lexicalCategory = lexicalCategory;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getLexicalCategory() {
        return lexicalCategory;
    }

    public void setLexicalCategory(String lexicalCategory) {
        this.lexicalCategory = lexicalCategory;
    }
}
