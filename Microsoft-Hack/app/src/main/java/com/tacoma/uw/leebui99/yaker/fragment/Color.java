package com.tacoma.uw.leebui99.yaker.fragment;

/**
 * Created by lebui on 1/30/2016.
 */
public class Color {
    public String mColor;
    public int correct;
    public String mFinalColor;

    public static final String COLOR = "word", CORRECT = "correct",
                FINALCOLOR = "result";
    public Color(String theColor, int theCorrect, String theFinalColor){

        setColor(theColor);
        setCorrect(theCorrect);
        setFinalColor(theFinalColor);
    }

    public void setColor(String color) {
        mColor = color;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setFinalColor(String finalColor) {
        mFinalColor = finalColor;
    }

    public String getColor() {
        return mColor;
    }

    public int getCorrect() {
        return correct;
    }

    public String getFinalColor() {
        return mFinalColor;
    }

    @Override
    public String toString() {
        return mColor;
    }
}
