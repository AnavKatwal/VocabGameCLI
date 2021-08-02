package com.grevocab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FileReadWrite {
    public final String WORDS_FILE;
    private String headerLine;

    private HashMap<Integer, Word> wordMap = new HashMap<>();

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

    private Word readWord(String line){
        String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        int wordId = Integer.parseInt(data[0]);
        String word = data[1].replace(" ", "");
        String definition = data[2];
        String example = data[3];
        int weight = Integer.parseInt(data[4]);
        boolean marked = Boolean.parseBoolean(data[5]);

        return new Word(wordId, word, definition, example, weight, marked);
    }

    private String getLine(Word word){
        String line = String.join(",",String.valueOf(word.getWordId()), word.getWord(), word.getDefinition(),
                word.getExample(), String.valueOf(word.getWeight()), String.valueOf(word.isMarked()));
         return line + "\n";
    }


}