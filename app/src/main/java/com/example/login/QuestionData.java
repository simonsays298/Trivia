package com.example.login;

public class QuestionData {
    private String questionName;
    private String rightAnswer;
    private String domain;

    public QuestionData(String questionName, String rightAnswer, String domain) {
        this.questionName = questionName;
        this.rightAnswer = rightAnswer;
        this.domain = domain;
    }

    public String getQuestionName() {
        return questionName;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String getDomain() {
        return domain;
    }


}
