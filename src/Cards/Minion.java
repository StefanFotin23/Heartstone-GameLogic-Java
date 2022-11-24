package Cards;

import Gameplay.Game;
import Gameplay.GameManager;
import Gameplay.Player;
import JsonOutput.*;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

import java.util.ArrayList;
import static Constants.Constants.*;

public class Minion extends Card{

    private int health;
    private int attackDamage;
    private boolean frozen;
    private boolean usedThisTurn;

    public Minion(String name, String description, int health, int attackDamage, int mana, ArrayList<String> color) {
        super(name, description, mana, color);
        this.health = health;
        this.attackDamage = attackDamage;
        this.frozen = false;
        this.usedThisTurn = false;
    }

    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public boolean getUsedThisTurn() {
        return usedThisTurn;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isUsedThisTurn() {
        return usedThisTurn;
    }

    public void setUsedThisTurn(boolean usedThisTurn) {
        this.usedThisTurn = usedThisTurn;
    }

    @Override
    public String getType() {
        return MINION;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean putCardOnTable(Player player) {
            if (this.onFrontRow()) {
                if (player.getFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                    player.getFrontRow().add(this);
                    return true;
                } else {
                    //output message
                    return false;
                }
            } else {
                if (player.getBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                    player.getBackRow().add(this);
                    return true;
                } else {
                    //output message
                    return false;
                }
            }
    }

    public void useSpecialAbility(Game game, Player player, Card card, Coordinates cardAttacker, Coordinates cardAttacked, ArrayNode output) throws JsonProcessingException {
        System.out.println("Normal minions don't have special abilities");
    }

    public void useAttack(Game game, Player player, Card attackedCard, Coordinates attackerCardCoordinates,
                          Coordinates attackedCardCoordinates, ArrayNode output) throws JsonProcessingException {
            if (!GameManager.isPlayersCard(GameManager.getPlayerEnemy(game, player), attackedCardCoordinates)) {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAttack",
                        attackerCardCoordinates, attackedCardCoordinates, ATTACKED_CARD_NOT_ENEMY);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
                return;
            }
            if(this.usedThisTurn) {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAttack",
                        attackerCardCoordinates, attackedCardCoordinates, ATTACKER_CARD_USED);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
                return;
            }
            if(this.frozen) {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAttack",
                        attackerCardCoordinates, attackedCardCoordinates, ATTACKER_CARD_FROZEN);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
                return;
            }
            if (attackedCard.isTank() ||
                (!GameManager.getPlayerEnemy(game, player).hasTankOnTable())) {
                //attack the card
                attackedCard.setHealth(attackedCard.getHealth() - this.getAttackDamage());
                if (attackedCard.getHealth() <= 0) {
                    GameManager.getPlayerEnemy(game, player).removeCardFromTable(attackedCardCoordinates);
                    GameManager.getPlayerEnemy(game, player).getDeck().add(attackedCard);
                }
                this.usedThisTurn = true;
            } else {
                CardAttackerErrorMessage errorStr = new CardAttackerErrorMessage("cardUsesAttack",
                        attackerCardCoordinates, attackedCardCoordinates, ATTACKED_CARD_NOT_TANK);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
    }

    public void useAttackOnHero(Game game, Player player, Coordinates thisCard, Hero attackedHero, ArrayNode output) throws JsonProcessingException {
        if (this.frozen) {
            AttackOnHeroErrorOutput errorStr = new AttackOnHeroErrorOutput(thisCard, ATTACKER_CARD_FROZEN);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        if (this.usedThisTurn) {
            AttackOnHeroErrorOutput errorStr = new AttackOnHeroErrorOutput(thisCard, ATTACKER_CARD_USED);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        if (GameManager.getPlayerEnemy(game, player).hasTankOnTable()) {
            AttackOnHeroErrorOutput errorStr = new AttackOnHeroErrorOutput(thisCard, ATTACKED_CARD_NOT_TANK);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }
        attackedHero.setHealth(attackedHero.getHealth() - this.attackDamage);
        this.setUsedThisTurn(true);
        if (attackedHero.getHealth() <= 0) {
            GameEndedOutput gameEndedOutput = new GameEndedOutput(player);
            JsonNode jsonNode = JsonParse.parseObjectToJson(gameEndedOutput);
            output.add(jsonNode);
            game.setGameEnded(true);
        }
    }

    public boolean onFrontRow() {
        if (this.getName().equals(THE_RIPPER) ||
                this.getName().equals(MIRAJ) ||
                this.getName().equals(GOLIATH) ||
                this.getName().equals(WARDEN)) {
            return true;
        } else if (this.getName().equals(SENTINEL) ||
                this.getName().equals(BERSERKER) ||
                this.getName().equals(THE_CURSED_ONE) ||
                this.getName().equals(DISCIPLE)) {
            return false;
        } else {
            System.out.println("onFrontRow error --> card name = " + this.getName());
            return false;
        }
    }

    @Override
    public void useEnvironmentCard(Game game, int handIdx, int index, ArrayList<Minion> affectedRow, Player player,
                                   ArrayNode output) throws JsonProcessingException {
        EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                handIdx, index, NOT_ENVIRONMENT_MESSAGE);
        JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
        output.add(jsonNode);
    }

    @Override
    public void placeCard(Game game, Player currentPlayer, int handIdx, ArrayNode output) throws JsonProcessingException {
        if (this.getMana() > currentPlayer.getMana()) {
            PlaceCardErrorOutput errorStr = new PlaceCardErrorOutput("placeCard", handIdx, NOT_ENOUGH_MANA_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
            return;
        }

        if (this.onFrontRow()) {
            if (currentPlayer.getFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                //add the card
                currentPlayer.getFrontRow().add(this);
                //remove from hand
                currentPlayer.getCardsInHand().remove(handIdx);
                //remove mana
                currentPlayer.setMana(currentPlayer.getMana() - this.getMana());
            } else {
                PlaceCardErrorOutput errorStr = new PlaceCardErrorOutput("placeCard", handIdx, FULL_ROW_MESSAGE);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
        } else {
            if (currentPlayer.getBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                //add the card
                currentPlayer.getBackRow().add(this);
                //remove from hand
                currentPlayer.getCardsInHand().remove(handIdx);
                //remove mana
                currentPlayer.setMana(currentPlayer.getMana() - this.getMana());
            } else {
                PlaceCardErrorOutput errorStr = new PlaceCardErrorOutput("placeCard", handIdx, FULL_ROW_MESSAGE);
                JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
                output.add(jsonNode);
            }
        }
    }

    public boolean isDisciple() {
        if (this.getName().equals(DISCIPLE)) {
            return true;
        }
        return false;
    }

    public boolean isTank() {
        if (this.getName().equals(GOLIATH) || this.getName().equals(WARDEN)) {
            return true;
        }
        return false;
    }
}
