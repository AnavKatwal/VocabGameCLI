package com.grevocab;

import java.util.*;

public class Hangman extends Game{

    private final Random random = new Random();

    private final String RE_ANSWER = ANSI_CYAN + "Answer in \"y\" or \"n\" only";

    public Hangman() {
        super();
    }

    public Hangman(int firstWord, int lastWord) {
        super(firstWord, lastWord);
    }

    public void play() {

        ArrayList<Integer> randomIdList = getRandomIdList();
        int score = 0;
        int totalAnswered = 0;

        for(int wordId: randomIdList){
            Word word = wordMap.get(wordId);
            char[] charArray = word.getWord().toCharArray();

            System.out.printf(ANSI_RESET + ANSI_BOLD + "Definition: %s \n", ANSI_RESET + word.getDefinition());

            ArrayList<Character> selectedCharacters = new ArrayList<>();
            showBlanks(charArray, selectedCharacters);

            int remainingLives = 3;
            boolean completed;
            do {

                char alphabet = getAlphabet();
                if (selectedCharacters.contains(alphabet)) {
                    System.out.printf(ANSI_CYAN + "\"%s\" has been tried already!!! \n", alphabet);
                } else {
                    if (!checkAlphabet(alphabet, charArray)) {
                        remainingLives--;
                        System.out.printf(ANSI_RED + "Remaining lives: %d \n", remainingLives);
                    }
                    selectedCharacters.add(alphabet);
                    showBlanks(charArray, selectedCharacters);
                }

                completed = checkIfAllCompleted(charArray, selectedCharacters);
            } while (remainingLives >= 1 && !completed);

            if (completed){
                System.out.printf(ANSI_GREEN + "Answer is: %s\n", ANSI_RESET + word.getWord());
                score++;
            } else {
                System.out.printf(ANSI_BOLD + ANSI_RED + "Answer is: %s\n", ANSI_RESET + ANSI_BOLD + word.getWord());
            }
            totalAnswered++;

            if(wantToMark(word)){
                markWord(word);
            }
            if(!stillWantToPlay()) break;
        }
        displayScore(score, totalAnswered);
        fileReadWrite.writeToFile(wordMap);
    }

    private boolean wantToMark(Word word){
        Scanner s = new Scanner(System.in);
        System.out.printf(ANSI_RESET + "Do you want to mark the word %s (y/n)\n", ANSI_BOLD + word.getWord());

        while (true){
            String input = s.nextLine().toLowerCase();

            if(input.equals("y")){
                return true;
            } else if(input.equals("n")){
                return false;
            } else {
                System.out.println(RE_ANSWER);
            }
        }
    }

    private boolean stillWantToPlay(){
        Scanner s = new Scanner(System.in);
        System.out.println(ANSI_RESET + "Do you want to play again? (y/n)");

        while(true){
            String input = s.nextLine().toLowerCase();
            if(input.equals("y")){
                return true;
            } else if(input.equals("n")){
                return false;
            } else {
                System.out.println(RE_ANSWER);
            }
        }

    }

    private boolean checkIfAllCompleted(char[] charArray, ArrayList<Character> selectedCharacters){
        for(char character : charArray){
            if(!selectedCharacters.contains(character)){
                return false;
            }
        }
        return true;
    }

    private boolean checkAlphabet(char alphabet, char[] charArray){
        for(char character: charArray){
            if (character == alphabet){
                return true;
            }
        }
        return false;
    }

    private void showBlanks(char[] charArray, ArrayList<Character> selectedCharacters){
        StringBuilder sb = new StringBuilder();
        for(char character: charArray){
            if(selectedCharacters.contains(character)){
                sb.append(character).append(" ");
            } else {
                sb.append("- ");
            }
        }

        System.out.println(ANSI_RESET + sb.toString());
    }

    private char getAlphabet() {
        Scanner s = new Scanner(System.in);

        System.out.print(ANSI_RESET + "Enter an alphabet: ");
        while (true) {
            try {
                String input = s.nextLine().toLowerCase();
                if (input.length() > 1) {
                    System.out.println(ANSI_CYAN + "Enter only one alphabet");
                } else {
                    char alphabet = input.charAt(0);
                    if (alphabet > 122 || alphabet < 97) {
                        System.out.println(ANSI_CYAN + "Only alphabets are allowed");
                    } else {
                        return alphabet;
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
    }
}
