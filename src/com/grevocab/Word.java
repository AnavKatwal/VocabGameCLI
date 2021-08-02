package com.grevocab;

public class Word {
    private final int wordId;
    private final String word;
    private final String definition;
    private final String example;
    private int weight;
    private boolean marked;

    public Word(int wordId, String word, String definition, String example) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = 0;
        this.marked = false;
    }

    public Word(int wordId, String word, String definition, String example, int weight) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = weight;
        this.marked = false;
    }

    public Word(int wordId, String word, String definition, String example, int weight, boolean marked) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = weight;
        this.marked = marked;
    }

    public void increaseWeight(){
        this.weight++;
    }

    public void decreaseWeight(){
        this.weight--;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isMarked() {
        return marked;
    }

}
