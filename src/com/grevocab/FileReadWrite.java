package com.grevocab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FileReadWrite {
    public final String WORDS_FILE;
    private String headerLine;

    private HashMap<Integer, Word> wordMap = new HashMap<>();

    private ArrayList<Integer> quizMarkedWordsIdList = new ArrayList<>();
    private ArrayList<Integer> hangmanMarkedWordsIdList = new ArrayList<>();

    public FileReadWrite(String wordsFile) {
        this.WORDS_FILE = wordsFile;
    }

    public HashMap<Integer, Word> readFromFile(){
        try(BufferedReader vocabFile = new BufferedReader(new InputStreamReader(new FileInputStream(WORDS_FILE), StandardCharsets.UTF_8))){
            String line;

            headerLine = vocabFile.readLine() + "\n";   // to skip the header line of csv file
            while((line = vocabFile.readLine()) != null){
                Word wordObj = readWord(line);
                wordMap.put(wordObj.getWordId(), wordObj);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return wordMap;
    }

    public void writeToFile(HashMap<Integer, Word> wordMap){
        try(BufferedWriter vocabFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(WORDS_FILE), StandardCharsets.UTF_8))){
            vocabFile.write(headerLine);

            for(int id: wordMap.keySet()){
                Word word = wordMap.get(id);
                vocabFile.write(getLine(word));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getQuizMarkedWordsIdList(){
        Collections.shuffle(quizMarkedWordsIdList);
        return quizMarkedWordsIdList;
    }

    public ArrayList<Integer> getHangmanMarkedWordsIdList(){
        Collections.shuffle(hangmanMarkedWordsIdList);
        return hangmanMarkedWordsIdList;
    }

    private Word readWord(String line){
        String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        int wordId = Integer.parseInt(data[0]);
        String word = data[1].replace(" ", "");
        String definition = data[2].replace("  ", " ");
        String example = data[3].replace("  ", " ");
        int weight = Integer.parseInt(data[4]);
        boolean quizMarked = Boolean.parseBoolean(data[5]);
        boolean hangmanMarked = Boolean.parseBoolean((data[6]));

        if(quizMarked){
            quizMarkedWordsIdList.add(wordId);
        }

        if(hangmanMarked){
            hangmanMarkedWordsIdList.add(wordId);
        }

        return new Word(wordId, word, definition, example, weight, quizMarked, hangmanMarked);
    }

    private String getLine(Word word){
        String line = String.join(",",String.valueOf(word.getWordId()), word.getWord(), word.getDefinition(),
                word.getExample(), String.valueOf(word.getWeight()), String.valueOf(word.isQuizMarked()), String.valueOf(word.isHangmanMarked()));
         return line + "\n";
    }
}
