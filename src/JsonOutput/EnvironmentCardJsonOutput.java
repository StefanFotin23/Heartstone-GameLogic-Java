package JsonOutput;

import Cards.*;

import java.util.ArrayList;

public class EnvironmentCardJsonOutput {
    private final int mana;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;

    public EnvironmentCardJsonOutput(Card card) {
        this.mana = card.getMana();
        this.description = card.getDescription();
        this.colors = card.getColor();
        this.name = card.getName();
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
}
