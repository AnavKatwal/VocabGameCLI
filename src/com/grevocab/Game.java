package com.grevocab;

import java.util.HashMap;

public abstract class Game {
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    static final String ANSI_BOLD = "\u001B[1m";
    static final String ANSI_UNDERLINE = "\u001B[4m";

    final String WORDS_FILE = "vocab-words.csv";
    HashMap<Integer, Word> wordMap;

    final FileReadWrite fileReadWrite = new FileReadWrite(WORDS_FILE);

    int firstWord;
    int lastWord;

    public Game(){
        wordMap = fileReadWrite.readFromFile();
        firstWord = 1;
        lastWord = wordMap.size();
    }

    public Game(int firstWord, int lastWord){
        this();
        this.firstWord = firstWord;
        this.lastWord = lastWord;
    }

    void markWord(Word word){
        /*
        set the marked field of a word to true if not already. marked words are displayed at the end of the game
        to see which words the player struggles with. the marked field is saved in the csv file as well.
         */
        if(word.isMarked()) {
            System.out.println(ANSI_RESET + "Word has already been marked. Press \"u\" and then enter to unmark");
        } else {
            word.setMarked(true);
            word.setWeight(0);  // marking the word also sets the weight to 0.
            System.out.printf(ANSI_RESET + "Word %s has been marked \n", word.getWord());
        }
    }

    void unmarkWord(Word word){
        /*
        reset the marked field of a word to false if true previously. a marked word is unmarked when
        the weight of 4 is gained on the word. the marked field is saved in the csv file as well
         */
        if(!word.isMarked()) {
            System.out.println(ANSI_RESET + "Word has not been marked. Press \"m\" and then enter to mark");
        }
        else {
            word.setMarked(false);
            System.out.printf(ANSI_RESET + "Word %s has been unmarked \n", word.getWord());
        }
    }

    void showMarked(){
        /*
        displays the words whose marked field is set to true. the word along with the definition is displayed.
         */
        int count=0;
        System.out.println(ANSI_BOLD + ANSI_UNDERLINE + "Marked words:" + ANSI_RESET);
        for(int id : wordMap.keySet()){
            Word word = wordMap.get(id);
            if(word.isMarked()){
                System.out.printf("\t%s : %s \n",ANSI_RESET + ANSI_YELLOW + ANSI_BOLD + word.getWord(), ANSI_RESET + word.getDefinition());
            }
            count ++;
        }
        if(count == 0){
            System.out.println("No words have been marked.");
        }
    }

    void displayScore(int score, int totalAnswered){
        System.out.println("********************************** End of game *********************************");
        System.out.printf(ANSI_GREEN + "Total correct answers = %d \t" + ANSI_RED + "Total wrong answers = %d \t" + ANSI_RESET + ANSI_BOLD +
                "Total attempted = %d / %d \n", score, totalAnswered-score, totalAnswered, wordMap.size());
        showMarked();
        System.out.println("********************************************************************************");
    }
}
