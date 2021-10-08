package com.vocabGame;

import java.util.*;

public class Hangman extends Game{

    public Hangman(boolean onlyMarked) {
        super(onlyMarked);
    }

    public Hangman(int firstWord, int lastWord) {
        super(firstWord, lastWord);
    }

    public void play() {
        ArrayList<Integer> randomIdList = getRandomIdList(hangmanMarkedWordsIdList);

        if(randomIdList.isEmpty()){
            System.out.println(ANSI_CYAN + "No words in the list!!!" + ANSI_RESET);
            return;
        }

        int score = 0;
        int totalAnswered = 0;

        boolean quit = false;

        for(int wordId: randomIdList){

            Word word = wordMap.get(wordId);
            char[] charArray = word.getWord().toCharArray();

            System.out.printf(ANSI_RESET + ANSI_BOLD + "Definition: %s \n", ANSI_RESET + word.getDefinition());

            ArrayList<Character> selectedCharacters = new ArrayList<>();
            showBlanks(charArray, selectedCharacters);

            int remainingLives = 3;
            boolean completed = false;
            do {

                char alphabet = getAlphabet();
                if(alphabet == 0){
                    quit = true;
                    break;
                } else if (selectedCharacters.contains(alphabet)) {
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
                word.increaseWeight();
                if(word.isHangmanMarked() && word.getWeight() > 3){    // unmark the word if score more than 3 is gained
                    unmarkWord(word);
                }

            } else {
                incorrectWordsIdList.add(wordId);
                word.setWeight(0);
                System.out.printf(ANSI_BOLD + ANSI_RED + "Answer is: %s\n", ANSI_RESET + ANSI_BOLD + word.getWord());
            }
            totalAnswered++;

            if(wantToMark(word)){
                markWord(word);
            }
            if(quit) break;
            System.out.println("================================================================================");
        }
        displayScore(score, totalAnswered, hangmanMarkedWordsIdList);
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
                System.out.println(ANSI_CYAN + "Answer in \"y\" or \"n\" only" + ANSI_RESET);
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
        char alphabet;

        System.out.print(ANSI_RESET + "Enter an alphabet (or \"quit\" to QUIT): ");
        while (true) {
            try {
                String input = s.nextLine().toLowerCase();
                if (input.length() != 1) {
                    if(input.equalsIgnoreCase("quit")){
                        alphabet = 0;
                        break;
                    }
                    System.out.println(ANSI_CYAN + "Enter only one alphabet" + ANSI_RESET);
                } else {
                    alphabet = input.charAt(0);
                    if (alphabet > 122 || alphabet < 97) {
                        System.out.println(ANSI_CYAN + "Only alphabets are allowed" + ANSI_RESET);
                    } else {
                        return alphabet;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println(ANSI_CYAN + "No such element, try again" + ANSI_RESET);
            }
        }
        return alphabet;
    }

    @Override
    void markWord(Word word) {
        /*
        sets the hangmanMarked field of a word to true if not already. The quiz game and the hangman game have
        separate marked fields. Marked words are displayed at the end of the game to see which words the
        player struggles with. the marked fields are saved in the csv file as well.
         */
        if (word.isHangmanMarked()) {
            System.out.println(ANSI_RESET + "Word has already been marked.");
        } else {
            word.setHangmanMarked(true);
            word.setWeight(0);  // marking the word also sets the weight to 0.
            addToMarkedList(word.getWordId());
            System.out.printf(ANSI_RESET + "Word %s has been marked \n", word.getWord());
        }
    }

    @Override
    void unmarkWord(Word word){
        /*
        reset the hangmanMarked field of a word to false if true previously. a hangmanMarked word is unmarked when
        the weight of 4 is gained on the word. the marked fields are saved in the csv file as well
         */
        if(!word.isHangmanMarked()) {
            System.out.println(ANSI_RESET + "Word has not been marked. Press \"m\" and then enter to mark");
        }
        else {
            word.setHangmanMarked(false);
            hangmanMarkedWordsIdList.remove(Integer.valueOf(word.getWordId()));
            System.out.printf(ANSI_RESET + "Word %s has been unmarked \n", word.getWord());
        }
    }

    @Override
    void addToMarkedList(int wordId) {
        if(!hangmanMarkedWordsIdList.contains(wordId)){
            hangmanMarkedWordsIdList.add(wordId);
        }
    }
}
