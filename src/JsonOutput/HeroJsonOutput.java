package JsonOutput;

import Cards.Hero;

import java.util.ArrayList;

public class HeroJsonOutput {
    private final int mana;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;
    private final int health;

    public HeroJsonOutput (Hero hero) {
        this.mana = hero.getMana();
        this.description = hero.getDescription();
        this.colors = hero.getColor();
        this.name = hero.getName();;
        this.health = hero.getHealth();
    }

    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
}


