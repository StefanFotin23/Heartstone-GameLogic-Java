package Cards;

import Gameplay.*;
import JsonOutput.EnvironmentErrorOutput;
import JsonOutput.HeroErrorMessage;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;
import java.util.ArrayList;
import static Constants.Constants.*;

public class Hero extends Card{

    private int health;
    private boolean usedThisTurn;

    public Hero(String name, String description, int mana, ArrayList<String> color) {
        super(name, description, mana, color);
        this.health = HERO_HEALTH_DEFAULT_VALUE;
    }

    public Hero(CardInput obj) {
        super(obj.getName(), obj.getDescription(), obj.getMana(), obj.getColors());
        this.health = HERO_HEALTH_DEFAULT_VALUE;
    }

    public void useHeroAbility(Game game, int affectedRowIdx, ArrayList<Minion> affectedRow, Player player, ArrayNode output) throws JsonProcessingException {
        if (this.getMana() > player.getMana()) {
            HeroErrorMessage errorStr = new HeroErrorMessage("useHeroAbility",
                    affectedRowIdx, HERO_NO_MANA);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        if (this.usedThisTurn) {
            HeroErrorMessage errorStr = new HeroErrorMessage("useHeroAbility",
                    affectedRowIdx, HERO_CARD_USED);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }

        if (this.getName().equals(LORD_ROYCE) || this.getName().equals(EMPRESS_THORINA)) {
            if (GameManager.getPlayerEnemy(game, player).ownsRow(affectedRowIdx)) {
                if (this.getName().equals(LORD_ROYCE)) {
                    int max = -1, max_pos = -1;
                    for (int i = 0; i < affectedRow.size(); i++) {
                        if (affectedRow.get(i).getAttackDamage() > max) {
                            max = affectedRow.get(i).getAttackDamage();
                            max_pos = i;
                        }
                    }
                    affectedRow.get(max_pos).setFrozen(true);
                } else if (this.getName().equals(EMPRESS_THORINA)) {
                    int max = -1, max_pos = -1;
                    for (int i = 0; i < affectedRow.size(); i++) {
                        if (affectedRow.get(i).getHealth() > max) {
                            max = affectedRow.get(i).getHealth();
                            max_pos = i;
                        }
                    }
                    affectedRow.remove(max_pos);
                }
                player.setMana(player.getMana() - this.getMana());
                this.setUsedThisTurn(true);
            } else {
                HeroErrorMessage errorStr = new HeroErrorMessage("useHeroAbility",
                        affectedRowIdx, SELECTED_ROW_NOT_ENEMY);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
        }

        if (this.getName().equals(KING_MUDFACE) || this.getName().equals(GENERAL_KOCIORAW)) {
            //verify if the affected row is allied
            if (player.ownsRow(affectedRowIdx)) {
                if (this.getName().equals(KING_MUDFACE)) {
                    for (int i = 0; i < affectedRow.size(); i++) {
                        affectedRow.get(i).setHealth(affectedRow.get(i).getHealth() + 1);
                    }
                } else if (this.getName().equals(GENERAL_KOCIORAW)) {
                    for (int i = 0; i < affectedRow.size(); i++) {
                        affectedRow.get(i).setAttackDamage(affectedRow.get(i).getAttackDamage() + 1);
                    }
                }
                player.setMana(player.getMana() - this.getMana());
                this.setUsedThisTurn(true);
            } else {
                HeroErrorMessage errorStr = new HeroErrorMessage("useHeroAbility",
                        affectedRowIdx, SELECTED_ROW_NOT_ALLY);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getAttackDamage() {
        return 0;
    }

    @Override
    public void setAttackDamage(int attackDamage) {

    }

    @Override
    public void setUsedThisTurn(boolean b) {
        this.usedThisTurn = b;
    }

    @Override
    public void setFrozen(boolean b) {

    }

    @Override
    public boolean isUsedThisTurn() {
        return false;
    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public boolean getUsedThisTurn() {
        return usedThisTurn;
    }

    @Override
    public boolean isDisciple() {
        return false;
    }

    @Override
    public boolean isTank() {
        return false;
    }

    @Override
    public boolean onFrontRow() {
        return false;
    }

    @Override
    public void useEnvironmentCard(Game game, int handIdx, int index, ArrayList<Minion> affectedRow, Player player, ArrayNode output) throws JsonProcessingException {
        EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
               handIdx, index, NOT_ENVIRONMENT_MESSAGE);
        JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
        output.add(jsonNode);
    }

    @Override
    public void placeCard(Game game, Player currentPlayer, int handIdx, ArrayNode output) {
        System.out.println("Can't place Hero card on table");
    }

    @Override
    public String getType() {
        return HERO;
    }
}
