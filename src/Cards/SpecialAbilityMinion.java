package Cards;

import Gameplay.Game;
import Gameplay.GameManager;
import Gameplay.Player;
import JsonOutput.CardAttackerErrorMessage;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

import java.util.ArrayList;
import static Constants.Constants.*;

public class SpecialAbilityMinion extends Minion{

    public SpecialAbilityMinion(String name, String description, int health, int attackDamage, int mana, ArrayList<String> color) {
        super(name, description, health, attackDamage, mana, color);
    }

    public void useSpecialAbility(Game game, Player player, Card card, Coordinates thisCard, Coordinates attackedCard
            ,ArrayNode output) throws JsonProcessingException {
        if (this.isFrozen()) {
            CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAbility",
                    thisCard, attackedCard, ATTACKER_CARD_FROZEN);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        if (this.isUsedThisTurn()) {
            CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAbility",
                    thisCard, attackedCard, ATTACKER_CARD_USED);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }

        if (this.isDisciple()) {
            if (!GameManager.isPlayersCard(player, attackedCard)) {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAbility",
                        thisCard, attackedCard, ATTACKED_CARD_NOT_ALLY);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
                return;
            } else {
                card.setHealth(card.getHealth() + 2);
            }
        } else if (!this.isDisciple()) {
            if (!GameManager.isPlayersCard(GameManager.getPlayerEnemy(game, player), attackedCard)) {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAbility",
                        thisCard, attackedCard, ATTACKED_CARD_NOT_ENEMY);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
                return;
            } else {
                if (card.isTank() ||
                        !GameManager.getPlayerEnemy(game, player).hasTankOnTable()) {
                    //actually use the card
                    if (this.getName().equals(THE_RIPPER)) {
                        card.setAttackDamage(card.getAttackDamage() - 2);
                        if (card.getAttackDamage() < 0) {
                            card.setAttackDamage(0);
                        }
                    } else if (this.getName().equals(MIRAJ)) {
                        int enemyHealth = card.getHealth();
                        card.setHealth(this.getHealth());
                        this.setHealth(enemyHealth);
                    } else if (this.getName().equals(THE_CURSED_ONE)) {
                        int attackDamage = card.getAttackDamage();
                        card.setAttackDamage(card.getHealth());
                        card.setHealth(attackDamage);
                        if (card.getHealth() <= 0) {
                            GameManager.getPlayerEnemy(game, player).removeCardFromTable(attackedCard);
                        }
                    }
                    this.setUsedThisTurn(true);
                } else {
                    CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAbility",
                            thisCard, attackedCard, ATTACKED_CARD_NOT_TANK);
                    JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                    output.add(jsonNode);
                }
            }
        }
    }
}
