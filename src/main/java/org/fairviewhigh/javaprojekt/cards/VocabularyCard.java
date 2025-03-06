package org.fairviewhigh.javaprojekt.cards;
public class VocabularyCard{
    private final String germanWord;
    private final String SpanishWord;
    private int correctcount = 0;

    public VocabularyCard(String german, String spanish){
        germanWord = german;
        SpanishWord = spanish;
    }

    public int getCorrect() {
        return correctcount;
    }

    public void incrementCorrect() {
        correctcount++;
    }

    public String getGermanWord() {
        return germanWord;
    }

    public String getSpanishWord() {
        return SpanishWord;
    }
}