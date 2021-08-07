package com.grevocab;

import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
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

    public static final String ENTER_INTEGER = ANSI_CYAN + "Enter an Integer (1, 2, 3...)!" + ANSI_RESET;

    public static final int GAME_CHOICES = 2;
    public static void main(String[] args) {
        QuizGame quizGame;
        Hangman hangman;

        int gameChoiceInt;
        while(true){
            try {
                String gameChoiceString = askUser(ANSI_BOLD + ANSI_UNDERLINE + "Select a game:" + ANSI_RESET +
                        "\n\t 1. WORD QUIZ \n\t 2. HANGMAN");
                gameChoiceInt = Integer.parseInt(gameChoiceString);
                if(gameChoiceInt > GAME_CHOICES){
                    System.out.printf(ANSI_CYAN + "Only %s choices available" + ANSI_RESET + "\n", GAME_CHOICES);
                } else {
                    break;
                }
            } catch(NumberFormatException e) {
                System.out.println(ENTER_INTEGER);
            }
        }

        boolean all;
        while(true){
            String allChoice = askUser("Do you want to play with all the words? (y/n)");
            if (allChoice.equalsIgnoreCase("y")){
                all = true;
                break;
            } else if(allChoice.equalsIgnoreCase("n")){
                all = false;
                break;
            } else {
                System.out.println(ANSI_CYAN + "Answer in \"y\" or \"n\" only" + ANSI_RESET);
            }
        }

        if(gameChoiceInt == 1){
            if(all){
                quizGame = new QuizGame();
            } else {
                int[] range = getRange();
                quizGame = new QuizGame(range[0], range[1]);
            }
            quizGame.play();
        }

        if(gameChoiceInt == 2){
            if(all){
                hangman = new Hangman();
            } else {
                int[] range = getRange();
                hangman = new Hangman(range[0], range[1]);
            }
            hangman.play();
        }
    }

    public static String askUser(String prompt){
        System.out.println(prompt);

        return scanner.nextLine();
    }

    public static int[] getRange(){
        int[] range = new int[2];
        String prompt = "Enter the %s word in the range";

        while(true){
            try{
                range[0] = Integer.parseInt(askUser(String.format(prompt, ANSI_BOLD + "First" + ANSI_RESET)));
                break;
            } catch (NumberFormatException e){
                System.out.println(ENTER_INTEGER);
            }
        }

        while(true){
            try{
                range[1] = Integer.parseInt(askUser(String.format(prompt, ANSI_BOLD + "Second" + ANSI_RESET)));
                break;
            } catch (NumberFormatException e){
                System.out.println(ENTER_INTEGER);
            }
        }
        return range;
    }


}
