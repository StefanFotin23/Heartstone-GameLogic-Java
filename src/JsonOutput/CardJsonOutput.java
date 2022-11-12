package JsonOutput;

import Cards.*;

import java.util.ArrayList;

public class CardJsonOutput extends EnvironmentCardJsonOutput{
    private final int attackDamage;
    private final int health;

    public CardJsonOutput(Card card) {
        super(card);
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }
}
