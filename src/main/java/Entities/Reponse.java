package Entities;

public class Reponse {
    private int id;
    private ChoixPossible choixPossible;
    private boolean correct;

    public Reponse(int id, ChoixPossible choixPossible, boolean correct) {
        this.id = id;
        this.choixPossible = choixPossible;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChoixPossible getChoixPossible() {
        return choixPossible;
    }
    public void setChoixPossible(ChoixPossible choixPossible) {
        this.choixPossible = choixPossible;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}