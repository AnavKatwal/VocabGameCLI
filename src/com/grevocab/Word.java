package com.grevocab;

public class Word {
    private final int wordId;
    private final String word;
    private final String definition;
    private final String example;
    private int weight;
    private boolean quizMarked;
    private boolean hangmanMarked;

    public Word(int wordId, String word, String definition, String example) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = 0;
        this.quizMarked = false;
        this.hangmanMarked = false;
    }

    public Word(int wordId, String word, String definition, String example, int weight) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = weight;
        this.quizMarked = false;
        this.hangmanMarked = false;
    }

    public Word(int wordId, String word, String definition, String example, int weight, boolean quizMarked, boolean hangmanMarked) {
        this.wordId = wordId;
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.weight = weight;
        this.quizMarked = quizMarked;
        this.hangmanMarked = hangmanMarked;
    }

    public void increaseWeight(){
        this.weight++;
    }

    public void decreaseWeight(){
        this.weight--;
    }

    public void setQuizMarked(boolean quizMarked) {
        this.quizMarked = quizMarked;
    }

    public void setHangmanMarked(boolean hangmanMarked){
        this.hangmanMarked = hangmanMarked;
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

    public boolean isQuizMarked() {
        return quizMarked;
    }

    public boolean isHangmanMarked(){
        return hangmanMarked;
    }
}
