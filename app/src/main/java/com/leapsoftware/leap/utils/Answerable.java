package com.leapsoftware.leap.utils;

/**
 * Created by vincentrickey on 2/24/16.
 */
public interface Answerable {
    public String getCorrectAnswer();
    public void setUserAnswer(String userAnswer);
    public String getUserAnswer();
    public boolean isCorrect();
}
