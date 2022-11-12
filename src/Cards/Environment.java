package Cards;

import java.util.ArrayList;
import static Constants.Constants.*;

public class Environment extends Card{

    public Environment(String name, String description, int health, int attackDamage, int mana, ArrayList<String> color) {
        super(name, description, health, attackDamage, mana, color);
    }

    @Override
    public String getType() {
        return ENVIRONMENT;
    }
}