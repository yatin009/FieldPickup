package io.webguru.fieldpickup.POJO;

import java.io.Serializable;

/**
 * Created by mahto on 16/4/17.
 */

public class QcQuestionDetails implements Serializable {

    private String question;

    private String answer;

    public QcQuestionDetails() {
    }

    public QcQuestionDetails(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
