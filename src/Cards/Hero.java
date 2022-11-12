package Cards;

import fileio.CardInput;
import java.util.ArrayList;
import static Constants.Constants.*;

public class Hero extends Card{

    public Hero(String name, String description, int health, int attackDamage, int mana, ArrayList<String> color) {
        super(name, description, health, attackDamage, mana, color);
    }

    public Hero(CardInput obj) {
        super(obj.getName(), obj.getDescription(), obj.getHealth(), obj.getAttackDamage(), obj.getMana(), obj.getColors());
        this.setHealth(HERO_HEALTH_DEFAULT_VALUE);
    }

    @Override
    public String getType() {
        return HERO;
    }
}
