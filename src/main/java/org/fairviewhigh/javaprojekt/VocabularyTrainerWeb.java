package org.fairviewhigh.javaprojekt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import org.fairviewhigh.javaprojekt.cards.VocabularyCard;

public class VocabularyTrainerWeb {
    private static final ArrayList<VocabularyCard> vocabList = new ArrayList();
    private static String filePath = "basic.csv";
    private static final ArrayList<VocabularyCard> actualList = vocabList;

    public static void addVocabCard(String de, String span) {
        if (filePath.equals("")) {
            filePath = "basic.csv";
        }
        vocabList.add(new VocabularyCard(de, span));
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath(filePath), true))) {
            writer.write(de + "," + span);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //1 = German-Spanish
    //2 = Spanish-German
    public String getWord(int selec) {
        readFromCsv(getFilePath(filePath));
        //vocabList.add(new VocabularyCard("test", "hs"));
        if (vocabList.isEmpty()) {
            return "{\"error\": \"No words in list\"}";
        }
        int wordRan = new Random().nextInt(vocabList.size());
        if (selec == 1) 
            return vocabList.get(wordRan).getGermanWord();
        else if (selec == 2) 
            return vocabList.get(wordRan).getSpanishWord();
        else return "wrong id";
    }
    //1 = German-Spanish
    //2 = Spanish-German
    
    public String isCorrect(String word, int id, int selec){
        if(selec == 1){
            if(word.equals(actualList.get(id).getSpanishWord())){
                actualList.remove(id);
                return "correct";
            }else
                return "Wrong " + actualList.get(id).getSpanishWord();
        }else if(selec == 2){
            if(word.equals(actualList.get(id).getGermanWord())){
                actualList.remove(id);
                return "correct";
            }else
                return "Wrong " + actualList.get(id).getGermanWord();
        }else{
            return "wrong id";
        }
    }

    public void deleteWord(int id){
        actualList.remove(id);
        vocabList.remove(id);
        removeWordFromFile();
    }
    
    public static void readFromCsv(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String english = parts[0].trim();
                    String spanish = parts[1].trim();

                    vocabList.add(new VocabularyCard(english, spanish));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilePath(String fileName) {
        return Objects.requireNonNull(VocabularyTrainerWeb.class.getClassLoader().getResource(fileName)).getPath();
    }

    public static void removeWordFromFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (VocabularyCard card : vocabList) {
                writer.write(card.getGermanWord() + "," + card.getSpanishWord());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}