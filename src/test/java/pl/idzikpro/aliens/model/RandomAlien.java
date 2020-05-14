package pl.idzikpro.aliens.model;

public class RandomAlien {
    private String race;
    private String rank;

    public RandomAlien(String race, String rank) {
        this.race = race;
        this.rank = rank;
    }

    public String getRace() {
        return race;
    }

    public String getRank() {
        return rank;
    }
}
