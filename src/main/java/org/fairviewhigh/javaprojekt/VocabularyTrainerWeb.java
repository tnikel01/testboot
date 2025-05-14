package org.fairviewhigh.javaprojekt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.fairviewhigh.javaprojekt.cards.VocabularyCard;

public class VocabularyTrainerWeb {
    private static final ArrayList<VocabularyCard> vocabList = new ArrayList<VocabularyCard>();
    private static String filePath = "/workspaces/javaprojekt/javaprojekt/basic.csv";
    private static ArrayList<VocabularyCard> actualList = vocabList;

    public static boolean setFilePath(String name) {
    try {
        if (name == null || name.contains("..") || name.contains("/") || name.contains("\\")) {
            return false;
        }

        Path baseDir = Paths.get("/workspaces/javaprojekt/javaprojekt");
        Path newPath = baseDir.resolve(name).normalize();

        if (!Files.exists(newPath)) {
            Files.createFile(newPath);
        }
        filePath = newPath.toString();
        readFromCsv(filePath);
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}


    public static void addVocabCard(String de, String span) {
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        if (filePath.equals("")) {
            filePath = "/workspaces/javaprojekt/javaprojekt/basic.csv";
        }
        vocabList.add(new VocabularyCard(de, span));
        actualList.add(new VocabularyCard(de, span));
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println(filePath);
            writer.write(de + "," + span);
            writer.newLine();
            writer.close();
            System.out.println("Written");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //1 = German-Spanish
    //2 = Spanish-German
    public String getWord(int selec) {
        readFromCsv(filePath);
        if (actualList.isEmpty()) {
            if(vocabList.isEmpty())
                return "{\"error\": \"No words in list\"}";
            else
                actualList = vocabList;
        }
        int wordRan = new Random().nextInt(vocabList.size());
        if (selec == 1) 
            return actualList.get(wordRan).getGermanWord() + "," + wordRan;
        else
            return actualList.get(wordRan).getSpanishWord() + "," + wordRan;
    }
    //1 = German-Spanish
    //2 = Spanish-German
    
    public static String isCorrect(String word, int id, int selec){
        if(selec == 1){
            if(word.toUpperCase().equals(actualList.get(id).getSpanishWord().toUpperCase())){
                actualList.remove(id);
                return "correct";
            }else
                return "Wrong " + actualList.get(id).getSpanishWord();
        }else if(selec == 2){
            if(word.toUpperCase().equals(actualList.get(id).getGermanWord().toUpperCase())){
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
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println(file.getAbsolutePath());
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String english = parts[0].trim();
                    String spanish = parts[1].trim();

                    vocabList.add(new VocabularyCard(english, spanish));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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