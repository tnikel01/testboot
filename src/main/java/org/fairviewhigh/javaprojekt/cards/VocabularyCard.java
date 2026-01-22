package org.fairviewhigh.javaprojekt.cards;
public class VocabularyCard{
    private final String germanWord;
    private final String SpanishWord;
    private int correctcount = 0;
    private int count = 0;

    public VocabularyCard(String german, String spanish){
        germanWord = german;
        SpanishWord = spanish;
    }

    public double getCorrectPercentage() {
        return (double)correctcount / count;
    }

    public void incrementCorrect() {
        correctcount++;
    }

    public void incrementCount() {
        count++;
    }

    public String getGermanWord() {
        return germanWord;
    }

    public String getSpanishWord() {
        return SpanishWord;
    }
}