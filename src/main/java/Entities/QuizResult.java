package Entities;

public class QuizResult {
    private Quiz quiz;
    private int score;
    private boolean passed;

    public QuizResult(Quiz quiz, int score, boolean passed) {
        this.quiz = quiz;
        this.score = score;
        this.passed = passed;
    }


    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
