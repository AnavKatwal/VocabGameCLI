package com.grevocab;

import java.util.*;

public class QuizGame {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";

    private static HashMap<Integer, Word> wordMap;
    private final int OPTIONS_QUANTITY = 5;
    private final String WORDS_FILE = "vocab-words.csv";
    private final FileReadWrite fileReadWrite = new FileReadWrite(WORDS_FILE);

    private Random random = new Random();

    private int firstWord = 1;
    private int lastWord;

    public QuizGame() {
        /*
        normal constructor
         */
        wordMap = fileReadWrite.readFromFile();
        lastWord = wordMap.size();
    }

    public QuizGame(int firstWord, int lastWord){
        /*
        constructor of quiz game if the player wants to set a range of words to play with
         */
        this.firstWord = firstWord;
        this.lastWord = lastWord;
        wordMap = fileReadWrite.readFromFile();
    }

    public void play(){
        boolean exit = false;

        ArrayList<Integer> randomIdList = getRandomIdList();
        int score = 0;
        int totalAnswered = 0;

        for(int wordId: randomIdList){
            if (exit) break;

            int randomSeed = random.nextInt(5000);
            random = new Random(randomSeed);

            Word word = wordMap.get(wordId);
            String ansiValue;
            if(word.isMarked()){
                ansiValue = ANSI_BOLD + ANSI_YELLOW;    // setting yellow bold font for marked words
            } else {
                ansiValue = ANSI_BOLD;  // setting bold font for unmarked words
            }
            System.out.printf(ANSI_RESET + "Word: %s \n", ansiValue + word.getWord());

            // create random list of options index
            ArrayList<Integer> optionsIndexList = getRandomIdList(word);

            printOptions(optionsIndexList);

            int answer = getAnswer();
            if (answer == 0) {   // value returned from getAnswer() if user choose to quit
                exit = true;
            } else if(answer == -1) {
                markWord(word);
            } else {
                totalAnswered++;
                answer--;   // options start from 1 but the optionIndexList starts from 0
                int optionIndex = optionsIndexList.get(answer);
                String selectedOption = wordMap.get(optionIndex).getDefinition();
                System.out.printf(ANSI_RESET + "Selected option: %s %n", selectedOption);

                if (word.getDefinition().equals(selectedOption)){
                    System.out.println(ANSI_GREEN + ANSI_BOLD + "Right answer");
                    word.increaseWeight();

                    if(word.isMarked() && word.getWeight() > 3){
                        unmarkWord(word);   // unmark a previously marked word after a weight of 4 or more is gained
                    }
                    score++;
                } else {
                    System.out.println(ANSI_RED + ANSI_BOLD + "Wrong answer");
                    word.decreaseWeight();
                }
            }
            System.out.println(ANSI_RESET + "================================================================================");
        }
        displayScore(score, totalAnswered);
        fileReadWrite.writeToFile(wordMap);
    }

    private void markWord(Word word){
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

    private void unmarkWord(Word word){
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

    private void showMarked(){
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

    private ArrayList<Integer> getRandomIdList(){
        /*
        method to get random list of wordId for the words to play
         */
        ArrayList<Integer> randomIdList = new ArrayList<>();
        int range = lastWord - firstWord + 1;

        while(randomIdList.size() < range){
            int randomId = random.nextInt(range) + firstWord; // to contain the range within firstWord and lastWord
            if(!randomIdList.contains(randomId)){
                randomIdList.add(randomId);
            }
        }
        return randomIdList;
    }

    private ArrayList<Integer> getRandomIdList(Word word){
        /*
        overloaded method to get random list of wordId for the options
         */
        ArrayList<Integer> randomIdList = new ArrayList<>();
        int range = lastWord - firstWord + 1;

        int answerId = word.getWordId();

        while(randomIdList.size() < OPTIONS_QUANTITY - 1){
            int randomId = random.nextInt(range) + firstWord; // to contain the range within firstWord and lastWord

            if(!(randomIdList.contains(randomId) || randomId == answerId)){
                randomIdList.add(randomId);
            }
        }
        randomIdList.add(answerId);
        Collections.shuffle(randomIdList);
        return randomIdList;
    }

    private void printOptions(ArrayList<Integer> optionsList) {
        /*
        displays the options for the word. uses optionsList to get the random list of wordIds
        along with answer word's id whose definitions are used as the options.
         */

        System.out.println(ANSI_RESET + "Options: (press \"q\" to quit, \"m\" to mark the word and \"u\" to unmark the word)");
        for (int i = 0; i < optionsList.size(); i++) {
            Word word = wordMap.get(optionsList.get(i));
            System.out.printf("\t%d. %s %n", i + 1, ANSI_RESET + word.getDefinition().replace('"', '\0'));
        }
    }

    private int getAnswer() {
        /*
        takes input from the user and returns the value in int.
        values:
            -1 = mark the word
            0 = quit the game
            1-5 = respective option selected
         */
        Scanner s = new Scanner(System.in);
        int input=0; // return value for quit
        boolean inputSatisfied = false;
        System.out.println(ANSI_RESET + "Choose an option");

        while (!inputSatisfied) {
            try {
                input = s.nextInt();
                if(input <= OPTIONS_QUANTITY){
                    inputSatisfied = true;
                } else {
                    System.out.println(ANSI_CYAN + "Selected option is not available, check and try again");
                }
            } catch (InputMismatchException e) {
                String prompt = s.nextLine().toLowerCase();
                switch (prompt){
                    case "q":
                        inputSatisfied = true;
                        break;
                    case "m":
                        input = -1; // return value to mark the word
                        inputSatisfied = true;
                        break;
                    default:
                        System.out.println(ANSI_CYAN + "Only integers are allowed (1,2,3,...)");
                }
            }
        }
        return input;
    }

    private void displayScore(int score, int totalAnswered){
        System.out.println("********************************** End of game *********************************");
        System.out.printf(ANSI_GREEN + "Total correct answers = %d \t" + ANSI_RED + "Total wrong answers = %d \t" + ANSI_RESET + ANSI_BOLD +
                "Total attempted = %d / %d \n", score, totalAnswered-score, totalAnswered, wordMap.size());
        showMarked();
        System.out.println("********************************************************************************");
    }
}
