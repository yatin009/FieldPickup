package io.webguru.fieldpickup.ApiHandler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by mahto on 12/4/17.
 */
public class QcQuestionDTO implements Serializable {

    @JsonProperty(value = "question")
    private String question;

    @JsonProperty(value = "expected_answer")
    private String expectedAnswer;

    @JsonProperty(value = "is_madatory")
    private String isMandatory;

    @JsonProperty(value = "answer")
    private String answer;

    @JsonProperty(value = "question_id")
    private Integer questionId;

    @JsonProperty(value = "question_data_type")
    private String questionDataType;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public void setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDataType() {
        return questionDataType;
    }

    public void setQuestionDataType(String questionDataType) {
        this.questionDataType = questionDataType;
    }

    public QcQuestionDTO() {
    }

    public QcQuestionDTO(String question, String expectedAnswer, String isMandatory) {
        this.question = question;
        this.expectedAnswer = expectedAnswer;
        this.isMandatory = isMandatory;
    }

    @Override
    public String toString() {
        return "{" +
            "question='" + question + '\'' +
            ", expectedAnswer='" + expectedAnswer + '\'' +
            ", isMandatory='" + isMandatory + '\'' +
            ", questionId='" + questionId + '\'' +
            ", answer='" + answer + '\'' +
            '}';
    }
}
