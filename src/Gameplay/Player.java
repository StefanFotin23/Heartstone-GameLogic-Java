package Gameplay;

import Cards.*;
import JsonOutput.EnvironmentErrorOutput;
import JsonOutput.ErrorStringJsonOutput;
import JsonOutput.PlaceCardErrorOutput;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import static Constants.Constants.*;

public class Player {
    private final String name;
    private Deck deck;
    private final Hero hero;
    private ArrayList<Card> cardsInHand = new ArrayList<>();
    private int mana;
    private final Random rand = new Random();

    public Player(String name, Deck deck, Hero hero, int shuffleSeed) {
        this.name = name;
        rand.setSeed(shuffleSeed);
        Collections.shuffle(deck.getCards(), rand);
        this.deck = deck;
        this.hero = hero;
        this.mana = 0;
    }

    void addMana(int manaBonus) {
        this.setMana(this.getMana() + manaBonus);
    }

    public String getName() {
        return name;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public Player getEnemy(GameTable gameTable) {
        if (this.getName().equals(PLAYER1)) {
            if (gameTable.getSecondPlayer().getName().equals(PLAYER2)) {
                return gameTable.getSecondPlayer();
            } else {
                return gameTable.getFirstPlayer();
            }
        } else {
            if (gameTable.getFirstPlayer().getName().equals(PLAYER1)) {
                return gameTable.getFirstPlayer();
            } else {
                return gameTable.getSecondPlayer();
            }
        }
    }

    public ArrayList<Card> getEnvironmentalCardsInHand() {
        ArrayList<Card> environmentalCards = new ArrayList<>();
        for (int i = 0; i < this.getCardsInHand().size(); i++) {
            if (this.getCardsInHand().get(i).getType().equals(ENVIRONMENT)) {
                environmentalCards.add(this.getCardsInHand().get(i));
            }
        }
        return environmentalCards;
    }

    public void putCardOnTable(Card card, GameTable gameTable) {
        if (this.name.equals(gameTable.getFirstPlayer().getName())) {
            if (card.onFrontRow()) {
                gameTable.getPlayerOneFrontRow().add(card);
            } else {
                gameTable.getPlayerOneBackRow().add(card);
            }
        } else if (this.name.equals(gameTable.getSecondPlayer().getName())) {
            if (card.onFrontRow()) {
                gameTable.getPlayerTwoFrontRow().add(card);
            } else {
                gameTable.getPlayerTwoBackRow().add(card);
            }
        }
        this.setMana(this.getMana() - card.getMana());
    }

    //returns true if there is space on the table row
    public boolean hasSpaceOnTableRow(GameTable gameTable, Card card) {
        if (this.name.equals(gameTable.getFirstPlayer().getName())) {
            if (card.onFrontRow() && gameTable.getPlayerOneFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
            if (!card.onFrontRow() && gameTable.getPlayerOneBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
        } else if (this.name.equals(gameTable.getSecondPlayer().getName())) {
            if (card.onFrontRow() && gameTable.getPlayerTwoFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
            if (!card.onFrontRow() && gameTable.getPlayerTwoBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTankOnTable(GameTable gameTable) {
        if (this.name.equals("player1")) {
            for (int i = 0; i < gameTable.getPlayerOneBackRow().size(); i++) {
                if (gameTable.getPlayerOneBackRow().get(i).isTank()) {
                    return true;
                }
            }
            for (int i = 0; i < gameTable.getPlayerOneFrontRow().size(); i++) {
                if (gameTable.getPlayerOneFrontRow().get(i).isTank()) {
                    return true;
                }
            }
        } else if (this.name.equals("player2")) {
            for (int i = 0; i < gameTable.getPlayerTwoBackRow().size(); i++) {
                if (gameTable.getPlayerTwoBackRow().get(i).isTank()) {
                    return true;
                }
            }
            for (int i = 0; i < gameTable.getPlayerTwoFrontRow().size(); i++) {
                if (gameTable.getPlayerTwoFrontRow().get(i).isTank()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cardAttack(GameTable gameTable, Actions action) {
        Player enemyPlayer = null;
        if (this.getName().equals("player1")) {
            enemyPlayer = gameTable.getSecondPlayer();
        } else if (this.getName().equals("player2")) {
            enemyPlayer = gameTable.getFirstPlayer();
        }
        Card attackerCard = gameTable.getPlayerCard(this, action.getCardAttacker());
        Card attackedCard = gameTable.getPlayerCard(enemyPlayer, action.getCardAttacked());

        if (attackerCard == null) {
            System.out.println("Attacker card error at " + action);
            return;
        }

        boolean flag = true;
        if (attackerCard.isFrozen()) {
            flag = false;
            System.out.println(ATTACKER_CARD_FROZEN);
        }
        if (attackerCard.isUsedThisTurn()) {
            flag = false;
            System.out.println(ATTACKER_CARD_USED);
        }
        if (attackedCard == null) {
            //if the attackedCard is null
            flag = false;
            System.out.println(ATTACKED_CARD_NONEXISTENT);
        } else if ((!attackedCard.isTank()) && gameTable.getSecondPlayer().hasTankOnTable(gameTable)) {
            //if the attackedCard exists, but is not Tank and player has a Tank on table
            flag = false;
            System.out.println(ATTACKED_CARD_NOT_TANK);
        } else if (flag) {
            //we attack that card
            attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());
            if (attackedCard.getHealth() <= 0) {
                gameTable.removeCard(action.getCardAttacked());
            }
            attackerCard.setUsedThisTurn(true);
        }
    }

    public void cardAbilityAttack(GameTable gameTable, Actions action) {
        Card attackedCard;
        Player enemyPlayer = null;
        if (this.getName().equals("player1")) {
            enemyPlayer = gameTable.getSecondPlayer();
        } else if (this.getName().equals("player2")) {
            enemyPlayer = gameTable.getFirstPlayer();
        }

        Card attackerCard = gameTable.getPlayerCard(this, action.getCardAttacker());

        if (attackerCard == null) {
            System.out.println("Attacker card error at " + action);
            return;
        }

        if (attackerCard.isDisciple()) {
            attackedCard = gameTable.getPlayerCard(this, action.getCardAttacked());
        } else {
            attackedCard = gameTable.getPlayerCard(enemyPlayer, action.getCardAttacked());
        }

        boolean flag = true;
        if (attackerCard.isFrozen()) {
            flag = false;
            String errorString = ATTACKER_CARD_FROZEN;
        }
        if (attackerCard.isUsedThisTurn()) {
            flag = false;
            String errorString = ATTACKER_CARD_USED;
        }
        if (attackedCard == null) {
            //if the attackedCard is null
            flag = false;
            if (attackerCard.isDisciple()) {
                //if attacker is Disciple, it should use ability against ally
                String errorString = ATTACKED_CARD_NOT_ALLY;
            } else {
                String errorString = ATTACKED_CARD_NONEXISTENT;
            }
        } else if ((!attackerCard.isDisciple()) && (!attackedCard.isTank()) && enemyPlayer.hasTankOnTable(gameTable)) {
            //if the attackedCard exists, but is not Tank and player has a Tank on table
            flag = false;
            String errorString = ATTACKED_CARD_NOT_TANK;
        } else if ((!attackerCard.isDisciple()) && flag) {
            //we use ability on that card (enemy)
            if(attackedCard.getName().equals("The Ripper")) {
                attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
            } else if(attackedCard.getName().equals("Miraj")) {
                int auxHealth = attackerCard.getHealth();
                attackerCard.setHealth(attackedCard.getHealth());
                attackedCard.setHealth(auxHealth);
            } else if(attackedCard.getName().equals("The Cursed One")) {
                int auxHealth = attackedCard.getHealth();
                attackedCard.setHealth(attackedCard.getAttackDamage());
                attackedCard.setAttackDamage(auxHealth);
            }
            attackerCard.setUsedThisTurn(true);
        } else if (attackerCard.isDisciple() && flag) {
            //we use ability on that card (ally)
            attackedCard.setHealth(attackedCard.getHealth() + 2);
            attackerCard.setUsedThisTurn(true);
        }
    }

    public void placeCard(GameTable gameTable, Actions action, ArrayNode output) throws JsonProcessingException {
        boolean flag = true;
        //if the card mana is more than player mana
        if (this.getCardsInHand().get(action.getHandIdx()) == null) {
            System.out.println("CardsInHandError in function placeCard");
        }
        if(this.getCardsInHand().get(action.getHandIdx()).getMana() >
                this.getMana()) {
            flag = false;
            PlaceCardErrorOutput errorStr = new PlaceCardErrorOutput("placeCard", action.getHandIdx(), NOT_ENOUGH_MANA_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        }
        //verify if the card type is Environment
        if(flag && this.getCardsInHand().get(action.getHandIdx()).getType().equals(ENVIRONMENT)) {
            flag = false;
            PlaceCardErrorOutput errorStr =
                    new PlaceCardErrorOutput("placeCard", action.getHandIdx(), NOT_PLACE_ENVIRONMENT_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        }
        //if there is place on the table row to place the card (and the card is eligible)
        if (flag && !this.hasSpaceOnTableRow(gameTable, this.getCardsInHand().get(action.getHandIdx()))) {
            flag = false;
            PlaceCardErrorOutput errorStr = new PlaceCardErrorOutput("placeCard", action.getHandIdx(), FULL_ROW_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        } //if all conditions are true, we place the card
        if (flag) {
            this.putCardOnTable(this.getCardsInHand().get(action.getHandIdx()), gameTable);
            this.getCardsInHand().remove(action.getHandIdx());
        }
    }

    boolean executeAction(Actions action, GameTable gameTable, ArrayNode output) throws JsonProcessingException {
        if (action.getCommand().equals("endPlayerTurn")) {
            //if we end turn, return 1
            return true;
        } else if (action.getCommand().equals("cardUsesAttack")) {
            //this.cardAttack(gameTable, action);
        } else if (action.getCommand().equals("cardUsesAbility")) {
            //this.cardAbilityAttack(gameTable, action);
        } else if (action.getCommand().equals("useAttackHero")) {

        } else if (action.getCommand().equals("useHeroAbility")) {

        } else if (action.getCommand().equals("useEnvironmentCard")) {
            this.useEnvironmentalCard(this.getCardsInHand().get(action.getHandIdx()), action, gameTable, output);
        } else if (action.getCommand().equals("placeCard")) {
            this.placeCard(gameTable, action, output);
        } else {
            System.out.println(action.getCommand() + " doesn't exist!!!");
        }
        // return false if we don't end the turn and continue with same player
        return false;
    }

    private void useEnvironmentalCard(Card card, Actions action, GameTable gameTable, ArrayNode output) throws JsonProcessingException {
        boolean flag = true;
        if (!card.getType().equals(ENVIRONMENT)) {
            flag = false;
            EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                    action.getHandIdx(), action.getAffectedRow(), NOT_ENVIRONMENT_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        }
        if (flag && this.getMana() < card.getMana()) {
            flag = false;
            EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                    action.getHandIdx(), action.getAffectedRow(), NOT_MANA_ENVIRONMENT_MESSAGE);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        }
        if (flag && !this.isEnemyRow(action.getAffectedRow(), gameTable)) {
            flag = false;
            EnvironmentErrorOutput errorStr = new EnvironmentErrorOutput("useEnvironmentCard",
                    action.getHandIdx(), action.getAffectedRow(), CHOSEN_ROW_NOT_ENEMY);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStr);
            output.add(jsonNode);
        }
        if (flag && card.getName().equals("Heart Hound")) {
            if (!this.rowIsFree(action.getAffectedRow(), gameTable)) {
                flag = false;
            }
        }
        if (flag) {
            this.useEnvCard(card, action, gameTable);
            this.setMana(this.getMana() - card.getMana());
        }
    }

    private boolean rowIsFree(int enemyRow, GameTable gameTable) {
        if (enemyRow == PLAYER_ONE_BACK_ROW_INDEX && gameTable.getPlayerTwoBackRow().size() < 5) {
            return true;
        }
        if (enemyRow == PLAYER_ONE_FRONT_ROW_INDEX && gameTable.getPlayerTwoFrontRow().size() < 5) {
            return true;
        }
        if (enemyRow == PLAYER_TWO_BACK_ROW_INDEX && gameTable.getPlayerOneBackRow().size() < 5) {
            return true;
        } if (enemyRow == PLAYER_TWO_FRONT_ROW_INDEX && gameTable.getPlayerOneFrontRow().size() < 5) {
            return true;
        }
        return false;
    }

    public void useEnvCard(Card card, Actions action, GameTable gameTable) {
        ArrayList<Card> row = gameTable.getRow(action.getAffectedRow());
        if (row == null) {
            System.out.println("useEnvironmentCard - row number error");
        }
        if (card.getName().equals("Firestorm")) {
            for (int i = 0; i < row.size(); i++) {
                row.get(i).setHealth(row.get(i).getHealth() - 1);
            }
            this.getCardsInHand().remove(action.getHandIdx());
        }
        if (card.getName().equals("Winterfell")) {
            for (int i = 0; i < row.size(); i++) {
                row.get(i).setFrozen(true);
            }
            this.getCardsInHand().remove(action.getHandIdx());
        }
        if (card.getName().equals("Heart Hound")) {
            int max = 0;
            int pos = -1;
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i).getHealth() > max) {
                    max = row.get(i).getHealth();
                    pos = i;
                }
            }
            Card aux = row.get(pos);
            ArrayList<Card> currentPlayerRow = gameTable.getMirroredRow(action.getAffectedRow());
            if (currentPlayerRow.size() < 5) {
                currentPlayerRow.add(aux);
                row.remove(pos);
                this.getCardsInHand().remove(action.getHandIdx());
            } else {
                System.out.println("Heart Hound error not enough space on row");
            }
        }
    }

    public boolean isEnemyRow(int affectedRow, GameTable gameTable) {
        if (this.getName().equals(gameTable.getFirstPlayer().getName())) {
            if (affectedRow == PLAYER_TWO_BACK_ROW_INDEX || affectedRow == PLAYER_TWO_FRONT_ROW_INDEX) {
                return true;
            }
        } else {
            if (affectedRow == PLAYER_ONE_BACK_ROW_INDEX || affectedRow == PLAYER_ONE_FRONT_ROW_INDEX) {
                return true;
            }
        }
        return false;
    }
}