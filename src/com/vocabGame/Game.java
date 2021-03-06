package com.vocabGame;

import java.util.*;

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

    private Random random = new Random();

    final String WORDS_FILE = "vocab-words.csv";
    HashMap<Integer, Word> wordMap;

    final FileReadWrite fileReadWrite = new FileReadWrite(WORDS_FILE);

    int firstWord;
    int lastWord;

    ArrayList<Integer> incorrectWordsIdList;

    ArrayList<Integer> quizMarkedWordsIdList;
    ArrayList<Integer> hangmanMarkedWordsIdList;

    boolean onlyMarked; // true if to play with marked words only

    public Game(boolean onlyMarked){
        wordMap = fileReadWrite.readFromFile();
        firstWord = 1;
        lastWord = wordMap.size();

        this.quizMarkedWordsIdList = fileReadWrite.getQuizMarkedWordsIdList();
        this.hangmanMarkedWordsIdList = fileReadWrite.getHangmanMarkedWordsIdList();

        this.incorrectWordsIdList = new ArrayList<>();
        this.onlyMarked = onlyMarked;
    }

    public Game(int firstWord, int lastWord){
        this(false);

        this.firstWord = firstWord;
        if(!(lastWord == 0)) {
            this.lastWord = lastWord;
        }
    }

    abstract void addToMarkedList(int wordId);

    abstract void markWord(Word word);

    abstract void unmarkWord(Word word);

    ArrayList<Integer> getRandomIdList(ArrayList<Integer> markedWordsIdList){
        /*
        method to get random list of wordId for the words to play
         */
        ArrayList<Integer> randomIdList = new ArrayList<>();

        if(onlyMarked){
            // play with marked words only
            randomIdList = new ArrayList<>(markedWordsIdList);
        } else{
            int range = lastWord - firstWord + 1;

            while (randomIdList.size() < range) {
                int randomId = random.nextInt(range) + firstWord; // to contain the range within firstWord and lastWord
                if (!randomIdList.contains(randomId)) {
                    randomIdList.add(randomId);
                }
            }
        }
        return randomIdList;
    }

    ArrayList<Integer> getRandomIdList(Word word, int optionsQuantity){
        /*
        overloaded method to get random list of wordId for the options
         */
        ArrayList<Integer> randomIdList = new ArrayList<>();
        int range = lastWord - firstWord + 1;

        int answerId = word.getWordId();

        while(randomIdList.size() < optionsQuantity - 1){
            int randomId = random.nextInt(range) + firstWord; // to contain the range within firstWord and lastWord

            if(!(randomIdList.contains(randomId) || randomId == answerId)){
                randomIdList.add(randomId);
            }
        }
        randomIdList.add(answerId);
        Collections.shuffle(randomIdList);
        return randomIdList;
    }


    void showMarked(ArrayList<Integer> markedWordsIdList){
        /*
        displays the words whose marked field is set to true. the word along with the definition is displayed.
         */
        if(!markedWordsIdList.isEmpty()) {

            System.out.printf(ANSI_BOLD + ANSI_UNDERLINE + "Marked words: (%d/%s)\n" + ANSI_RESET + "\n", markedWordsIdList.size(), wordMap.size() );

            for(int id: markedWordsIdList){
                Word word = wordMap.get(id);
                System.out.printf("\t%s : %s \n \n", ANSI_RESET + ANSI_YELLOW + ANSI_BOLD + word.getWord(), ANSI_RESET + word.getDefinition().replace("\"", ""));
            }

        }
    }

    void displayScore(int score, int totalAnswered, ArrayList<Integer> markedWordsIdList){
        System.out.println("********************************** End of game *********************************");
        System.out.printf(ANSI_GREEN + "Total correct answers = %d \t" + ANSI_RED + "Total wrong answers = %d \t" + ANSI_RESET + ANSI_BOLD +
                "Total attempted = %d / %d \n", score, totalAnswered-score, totalAnswered, wordMap.size());
        showIncorrect();
        System.out.println("********************************************************************************");
        showMarked(markedWordsIdList);
        System.out.println("********************************************************************************");
    }

    void showIncorrect(){
        /*
        displays the words that the player gave wrong or no answers in the round
         */
        if(!incorrectWordsIdList.isEmpty()){
            System.out.println(ANSI_RED + ANSI_BOLD + "Wrong answers / Didn't attempt:" + ANSI_RESET + "\n");
            for(int id: incorrectWordsIdList){
                Word word = wordMap.get(id);

                System.out.printf("\t%s : %s \n \n", ANSI_PURPLE + word.getWord() + ANSI_RESET, word.getDefinition().replace("\"", ""));
            }
        }
    }

    void addToIncorrectList(int wordId){
        if(!incorrectWordsIdList.contains(wordId)){
            incorrectWordsIdList.add(wordId);
        }
    }
}
