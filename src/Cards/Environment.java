package Cards;

import Gameplay.*;
import JsonOutput.EnvironmentErrorOutput;
import JsonOutput.HeartHoundErrorMessage;
import JsonOutput.PlaceCardErrorOutput;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import static Constants.Constants.*;

public class Environment extends Card{

    public Environment(String name, String description, int mana, ArrayList<String> color) {
        super(name, description, mana, color);
    }

    public void useEnvironmentCard(Game game, int thisCardIndex, int affectedRowIdx, ArrayList<Minion> affectedRow,
                                   Player player, ArrayNode output) throws JsonProcessingException {
        if (this.getMana() > player.getMana()) {
            EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                thisCardIndex, affectedRowIdx, NOT_MANA_ENVIRONMENT_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        if (!GameManager.getPlayerEnemy(game, player).ownsRow(affectedRowIdx)) {
            EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                    thisCardIndex, affectedRowIdx, CHOSEN_ROW_NOT_ENEMY);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }

        //actually use the environment card
        if (this.getName().equals(FIRESTORM)) {
            for (int i = 0; i < affectedRow.size(); i++) {
                affectedRow.get(i).setHealth(affectedRow.get(i).getHealth() - 1);
            }
            for (int i = 0; i < affectedRow.size(); i++) {
                if (affectedRow.get(i).getHealth() <= 0) {
                    affectedRow.remove(i);
                }
            }
            player.getCardsInHand().remove(thisCardIndex);
            player.setMana(player.getMana() - this.getMana());
        } else if (this.getName().equals(WINTERFELL)) {
            for (int i = 0; i < affectedRow.size(); i++) {
                affectedRow.get(i).setFrozen(true);
            }
            player.getCardsInHand().remove(thisCardIndex);
            player.setMana(player.getMana() - this.getMana());
        } else if (this.getName().equals(HEART_HOUND)) {
            int max = -1, max_pos = -1;
            for (int i = 0; i < affectedRow.size(); i++) {
                if (affectedRow.get(i).getHealth() > max) {
                    max = affectedRow.get(i).getHealth();
                    max_pos = i;
                }
            }
            Minion minion = affectedRow.get(max_pos);
            boolean done = minion.putCardOnTable(player);
            if (done) {
                affectedRow.remove(max_pos);
                player.getCardsInHand().remove(thisCardIndex);
                player.setMana(player.getMana() - this.getMana());
            } else {
                HeartHoundErrorMessage errorStr = new HeartHoundErrorMessage(affectedRowIdx,
                        "useEnvironmentCard", HEART_HOUND_NO_SPACE_ON_ROW, thisCardIndex);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
        }
    }

    @Override
    public void placeCard(Game game, Player currentPlayer, int handIdx, ArrayNode output) throws JsonProcessingException {
        PlaceCardErrorOutput errorStr =
                new PlaceCardErrorOutput("placeCard", handIdx, NOT_PLACE_ENVIRONMENT_MESSAGE);
        JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
        output.add(jsonNode);
    }

    @Override
    public String getType() {
        return ENVIRONMENT;
    }

    @Override
    public int getHealth() {
        System.out.println("Environment cards don't have health");
        return 0;
    }

    @Override
    public void setHealth(int health) {}

    @Override
    public int getAttackDamage() {
        System.out.println("Environment cards don't have attackDamage");
        return 0;
    }

    @Override
    public void setAttackDamage(int attackDamage) {}

    @Override
    public void setUsedThisTurn(boolean b) {

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
        return false;
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
}