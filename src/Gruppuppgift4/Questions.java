package Gruppuppgift4;

public class Questions {
    String category;
    String question;
    String answer;
    String wrong1;
    String wrong2;
    String wrong3;
    boolean unused = true;

    public Questions(String category, String question, String answer, String wrong1, String wrong2, String wrong3) {
        this.category = category;
        this.question = question;
        this.answer = answer;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.wrong3 = wrong3;
    }
    public Questions() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getWrong1() {
        return wrong1;
    }

    public void setWrong1(String wrong1) {
        this.wrong1 = wrong1;
    }

    public String getWrong2() {
        return wrong2;
    }

    public void setWrong2(String wrong2) {
        this.wrong2 = wrong2;
    }

    public String getWrong3() {
        return wrong3;
    }

    public void setWrong3(String wrong3) {
        this.wrong3 = wrong3;
    }
}