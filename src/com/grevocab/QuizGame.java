package com.grevocab;

import java.util.*;

public class QuizGame extends Game{

    private final int OPTIONS_QUANTITY = 5;

    private Random random = new Random();

    public QuizGame() {
        super();
    }

    public QuizGame(int firstWord, int lastWord){
        super(firstWord, lastWord);
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
        ArrayList<Integer> optionsIndexList = getRandomIdList(word, OPTIONS_QUANTITY);

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

    private void printOptions(ArrayList<Integer> optionsList) {
        /*
        displays the options for the word. uses optionsList to get the random list of wordIds
        along with answer word's id whose definitions are used as the options.
         */

        System.out.println(ANSI_RESET + "Options: (press \"q\" to quit, \"m\" to mark the word");
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
                    default:
                        System.out.println(ANSI_CYAN + "Only integers are allowed (1,2,3,...)");
                }
            }
        }
        return input;
    }
}
