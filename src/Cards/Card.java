package Cards;

import Gameplay.Game;
import Gameplay.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public abstract class Card {
    private final String name;
    private final String description;
    private int mana;
    private final ArrayList<String> colors;

    protected Card(String name, String description, int mana, ArrayList<String> colors) {
        this.name = new String(name);
        this.description = description;
        this.mana = mana;
        this.colors = colors;
    }

    protected Card(Card obj) {
        this.name = new String(obj.name);
        this.description = obj.description;
        this.mana = obj.mana;
        this.colors = obj.colors;
    }

    public abstract String getType();

    public abstract int getHealth();

    public abstract void setHealth(int health);

    public abstract int getAttackDamage();

    public abstract void setAttackDamage(int attackDamage);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<String> getColor() {
        return colors;
    }

    public abstract void setUsedThisTurn(boolean b);

    public abstract void setFrozen(boolean b);

    public abstract boolean isUsedThisTurn();

    public abstract boolean isFrozen();

    public abstract boolean getUsedThisTurn();

    public abstract boolean isDisciple();

    public abstract boolean isTank();

    public abstract boolean onFrontRow();

    public abstract void useEnvironmentCard(Game game, int handIdx, int index, ArrayList<Minion> affectedRow, Player player, ArrayNode output) throws JsonProcessingException;

    public abstract void placeCard(Game game, Player currentPlayer, int handIdx, ArrayNode output) throws JsonProcessingException;
}
