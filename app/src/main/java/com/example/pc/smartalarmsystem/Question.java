package com.example.pc.smartalarmsystem;

import java.util.Collections;
import java.util.List;

/**
 * Created by PC on 4/4/2018.
 */

public class Question {
    private String question;
    private List<String> options;

    public Question(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        Collections.shuffle(options);
        return options;
    }
}
