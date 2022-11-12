package Cards;

import java.util.ArrayList;

public class SpecialAbilityMinion extends Minion{
    private String specialAbility;
    public SpecialAbilityMinion(String name, String description, int health, int attackDamage, int mana, ArrayList<String> color, String specialAbility) {
        super(name, description, health, attackDamage, mana, color);
        this.specialAbility = specialAbility;
    }
}
