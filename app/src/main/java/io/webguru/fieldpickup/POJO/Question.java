package io.webguru.fieldpickup.POJO;

/**
 * Created by mahto on 4/3/17.
 */

public class Question {

    private String question;

    private String type;

    private String value;

    private String description;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Question() {
    }

    public Question(String question, String type, String value, String description) {
        this.question = question;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                "question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
