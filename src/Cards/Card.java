package Cards;

import java.util.ArrayList;

public abstract class Card {
    private final String name;
    private final String description;
    private int health;
    private int attackDamage;
    private int mana;
    private final ArrayList<String> colors;
    private boolean frozen = false;
    private boolean usedThisTurn = false;

    protected Card(String name, String description, int health, int attackDamage, int mana, ArrayList<String> colors) {
        this.name = new String(name);
        this.description = description;
        this.health = health;
        this.attackDamage = attackDamage;
        this.mana = mana;
        this.colors = colors;
    }

    protected Card(Card obj) {
        this.name = new String(obj.name);
        this.description = obj.description;
        this.health = obj.health;
        this.attackDamage = obj.attackDamage;
        this.mana = obj.mana;
        this.colors = obj.colors;
    }

    public boolean isTank() {
        if (this.name.equals("Goliath") || this.name.equals("Warden")) {
            return true;
        }
        return false;
    }

    public boolean isDisciple() {
        if (this.name.equals("Disciple")) {
            return true;
        }
        return false;
    }

    public boolean isUsedThisTurn() {
        return usedThisTurn;
    }

    public void setUsedThisTurn(boolean usedThisTurn) {
        this.usedThisTurn = usedThisTurn;
    }

    public abstract String getType();

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
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

    public boolean onFrontRow() {
        if (this.getName().equals("The Ripper") ||
        this.getName().equals("Miraj") ||
        this.getName().equals("Goliath") ||
        this.getName().equals("Warden")) {
            return true;
        } else if (this.getName().equals("Sentinel") ||
                this.getName().equals("Berserker") ||
                this.getName().equals("The Cursed One") ||
                this.getName().equals("Disciple")) {
            return false;
        } else {
            System.out.println("onFrontRow error --> card name = " + this.getName());
            return false;
        }
    }
}
