package com.grevocab;

import java.util.*;

public class QuizGame extends Game{

    private final int OPTIONS_QUANTITY = 5;

    public QuizGame(boolean onlyMarked) {
        super(onlyMarked);
    }

    public QuizGame(int firstWord, int lastWord){
        super(firstWord, lastWord);
    }

    public void play(){
        ArrayList<Integer> randomIdList = getRandomIdList(quizMarkedWordsIdList);

        if(randomIdList.isEmpty()){
            System.out.println(ANSI_CYAN + "No words in the list !!!" + ANSI_RESET);
            return;
        }

        boolean quit = false;

        int count = 0;
        int score = 0;
        int totalAnswered = 0;

        for(int wordId: randomIdList) {
            if (quit) break;

            Word word = wordMap.get(wordId);
            String ansiValue;

            if (word.isQuizMarked()) {
                ansiValue = ANSI_BOLD + ANSI_YELLOW;    // setting yellow bold font for marked words
            } else {
                ansiValue = ANSI_BOLD;  // setting bold font for unmarked words
            }

            count++;
            System.out.printf(ANSI_RESET + "%d. Word: %s \n", count, ansiValue + word.getWord());

            // create random list of options index
            ArrayList<Integer> optionsIndexList = getRandomIdList(word, OPTIONS_QUANTITY);

            printOptions(optionsIndexList);
            boolean reAsk;
            do {
                int answer = getAnswer();
                if(answer == -2){
                    printExample(word);
                    reAsk = true;
                } else {
                    if (answer == 0) {   // value returned from getAnswer() if user choose to quit
                        addToIncorrectList(wordId);
                        quit = true;
                    } else if (answer == -1) {
                        markWord(word);
                        addToIncorrectList(wordId);
                    } else {
                        totalAnswered++;
                        answer--;   // options start from 1 but the optionIndexList starts from 0
                        int optionIndex = optionsIndexList.get(answer);
                        String selectedOption = wordMap.get(optionIndex).getDefinition();
                        System.out.printf(ANSI_RESET + "Selected option: %s %n", selectedOption);

                        if (word.getDefinition().equals(selectedOption)) {
                            System.out.println(ANSI_GREEN + ANSI_BOLD + "Right answer");
                            word.increaseWeight();

                            if (word.isQuizMarked() && word.getWeight() > 3) {
                                unmarkWord(word);   // unmark a previously marked word after a weight of 4 or more is gained
                            }
                            score++;
                        } else {
                            System.out.println(ANSI_RED + ANSI_BOLD + "Wrong answer");
                            word.decreaseWeight();
                            addToIncorrectList(wordId);
                            markWord(word);
                        }
                    }
                    reAsk = false;
                }
            } while(reAsk);
            System.out.println(ANSI_RESET + "================================================================================");
        }
        displayScore(score, totalAnswered, quizMarkedWordsIdList);
        fileReadWrite.writeToFile(wordMap);
    }

    private void printOptions(ArrayList<Integer> optionsList) {
        /*
        displays the options for the word. uses optionsList to get the random list of wordIds
        along with answer word's id whose definitions are used as the options.
         */

        System.out.println(ANSI_RESET + "Options: (press \"q\" to quit, \"m\" to mark the word or \"e\" to see the example sentence)");
        for (int i = 0; i < optionsList.size(); i++) {
            Word word = wordMap.get(optionsList.get(i));
            System.out.printf("\t%d. %s %n", i + 1, ANSI_RESET + word.getDefinition().replace('"', '\0'));
        }
    }

    private int getAnswer() {
        /*
        takes input from the user and returns the value in int.
        values:
            -2 = show example sentence
            -1 = mark the word
            0 = quit the game
            1-5 = respective option selected
         */
        Scanner s = new Scanner(System.in);
        int input = 0; // return value for quit
        boolean inputSatisfied = false;
        System.out.println(ANSI_RESET + "Choose an option");

        while (!inputSatisfied) {
            try {
                input = s.nextInt();
                if (input <= OPTIONS_QUANTITY) {
                    inputSatisfied = true;
                } else {
                    System.out.println(ANSI_CYAN + "Selected option is not available, check and try again");
                }
            } catch (InputMismatchException e) {
                String prompt = s.nextLine().toLowerCase();
                switch (prompt) {
                    case "q":
                        inputSatisfied = true;
                        break;
                    case "m":
                        input = -1; // return value to mark the word
                        inputSatisfied = true;
                        break;
                    case "e":
                        input = -2; // return value to print example
                        inputSatisfied = true;
                        break;
                    default:
                        System.out.println(ANSI_CYAN + "Only integers are allowed (1,2,3,...)");
                }
            }
        }
        return input;
    }

    private void printExample(Word word){
        System.out.printf(ANSI_BOLD + "Example:" + ANSI_RESET + " %s \n", word.getExample().replace("\"", ""));
    }

    @Override
    void markWord(Word word) {
        /*
        set the quizMarked field of a word to true if not already. The quiz game and the hangman game have
        separate marked fields. Marked words are displayed at the end of the game to see which words the
        player struggles with. the marked fields are saved in the csv file as well.
         */
        if (word.isQuizMarked()) {
            System.out.println(ANSI_RESET + "Word has already been marked.");
        } else {
            word.setQuizMarked(true);
            word.setWeight(0);  // marking the word also sets the weight to 0.
            addToMarkedList(word.getWordId());
            System.out.printf(ANSI_RESET + "Word %s has been marked \n", word.getWord());
        }
    }

    @Override
    void unmarkWord(Word word){
        /*
        reset the quizMarked field of a word to false if true previously. a marked word is unmarked when
        the weight of 4 is gained on the word. the marked field is saved in the csv file as well
         */
        if(!word.isQuizMarked()) {
            System.out.println(ANSI_RESET + "Word has not been marked. Press \"m\" and then enter to mark");
        }
        else {
            word.setQuizMarked(false);
            quizMarkedWordsIdList.remove(Integer.valueOf(word.getWordId()));
            System.out.printf(ANSI_RESET + "Word %s has been unmarked \n", word.getWord());
        }
    }


    @Override
    void addToMarkedList(int wordId) {
        if(!quizMarkedWordsIdList.contains(wordId)){
            quizMarkedWordsIdList.add(wordId);
        }
    }
}
