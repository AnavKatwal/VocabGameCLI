package com.vocabGame;

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

    public static final int GAME_CHOICES = 5;

    public static void main(String[] args) {
        QuizGame quizGame;
        Hangman hangman;

        boolean quitGame = false;

        while(!quitGame) {
            int gameChoiceInt;
            while (true) {
                try {
                    String gameChoiceString = askUser(ANSI_BOLD + ANSI_UNDERLINE + "Select a game:" + ANSI_RESET +
                            "\n 1. WORD QUIZ \n 2. HANGMAN \n 3. MARKED WORDS QUIZ \n 4. MARKED WORDS HANGMAN \n 5. " + ANSI_YELLOW + "QUIT THE GAME" + ANSI_RESET);
                    gameChoiceInt = Integer.parseInt(gameChoiceString);
                    if (gameChoiceInt > GAME_CHOICES) {
                        System.out.printf(ANSI_CYAN + "Only %s choices available" + ANSI_RESET + "\n", GAME_CHOICES);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(ENTER_INTEGER);
                }
            }

            boolean all = true;

            if (gameChoiceInt < 3) {
                while (true) {
                    String allChoice = askUser("Do you want to play with all the words? (y/n)");
                    if (allChoice.equalsIgnoreCase("y")) {
                        break;
                    } else if (allChoice.equalsIgnoreCase("n")) {
                        all = false;
                        break;
                    } else {
                        System.out.println(ANSI_CYAN + "Answer in \"y\" or \"n\" only" + ANSI_RESET);
                    }
                }
            }

            switch (gameChoiceInt) {
                case 1:
                    if (all) {
                        quizGame = new QuizGame(false);
                    } else {
                        int[] range = getRange();
                        quizGame = new QuizGame(range[0], range[1]);
                    }
                    quizGame.play();
                    break;

                case 2:
                    if (all) {
                        hangman = new Hangman(false);
                    } else {
                        int[] range = getRange();
                        hangman = new Hangman(range[0], range[1]);
                    }
                    hangman.play();
                    break;

                case 3:
                    quizGame = new QuizGame(true);
                    quizGame.play();
                    break;

                case 4:
                    hangman = new Hangman(true);
                    hangman.play();
                    break;

                case 5:
                    quitGame = true;
            }
        }
    }

    public static String askUser(String prompt) {
        System.out.println(prompt);

        return scanner.nextLine();
    }

    public static int[] getRange() {
        int[] range = new int[2];
        String prompt = "Enter the %s word in the range";

        while (true) {
            try {
                range[0] = Integer.parseInt(askUser(String.format(prompt, ANSI_BOLD + "First" + ANSI_RESET)));
                break;
            } catch (NumberFormatException e) {
                System.out.println(ENTER_INTEGER);
            }
        }

        while (true) {
            String input = askUser(String.format(prompt + " (Enter \"last\" if the you want till the end of the list)", ANSI_BOLD + "Second" + ANSI_RESET));
            try {
                range[1] = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                if(input.equalsIgnoreCase("last")){
                    range[1] = 0;
                    break;
                }
                System.out.println(ENTER_INTEGER);
            }
        }
        return range;
    }
}
