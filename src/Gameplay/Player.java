package Gameplay;

import Cards.*;

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

    public ArrayList<Card> getEnvironmentalCardsInHand() {
        ArrayList<Card> environmentalCards = new ArrayList<>();
        for (int i = 0; i < this.getCardsInHand().size(); i++) {
            if (this.getCardsInHand().get(i).getType().equals(ENVIRONMENT)) {
                environmentalCards.add(this.getCardsInHand().get(i));
            }
        }
        return environmentalCards;
    }

    public void putCardOnTable(Card card, GameTable gameTable, int tableRow) {
        if (this.name.equals("player1")) {
            if (tableRow == PLAYER_ONE_FRONT_ROW_INDEX) {
                gameTable.getPlayerOneFrontRow().add(card);
            }
            if (tableRow == PLAYER_ONE_BACK_ROW_INDEX) {
                gameTable.getPlayerOneBackRow().add(card);
            }
        } else if (this.name.equals("player2")) {
            if (tableRow == PLAYER_TWO_FRONT_ROW_INDEX) {
                gameTable.getPlayerTwoFrontRow().add(card);
            }
            if (tableRow == PLAYER_TWO_BACK_ROW_INDEX) {
                gameTable.getPlayerTwoBackRow().add(card);
            }
        }
    }

    //returns true if there is space on the table row
    public boolean verifySpaceOnTableRow(GameTable gameTable, int tableRow) {
        if (tableRow >= 4) {
            return false;
        }//if the row number does not exist, return false

        if (this.name.equals("player1")) {
            if (tableRow == PLAYER_ONE_FRONT_ROW_INDEX && gameTable.getPlayerOneFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
            if (tableRow == PLAYER_ONE_BACK_ROW_INDEX && gameTable.getPlayerOneBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
        } else if (this.name.equals("player2")) {
            if (tableRow == PLAYER_TWO_FRONT_ROW_INDEX && gameTable.getPlayerTwoFrontRow().size() < NUMBER_OF_CARDS_PER_ROW) {
                return true;
            }
            if (tableRow == PLAYER_TWO_BACK_ROW_INDEX && gameTable.getPlayerTwoBackRow().size() < NUMBER_OF_CARDS_PER_ROW) {
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
            enemyPlayer = gameTable.getPlayer2();
        } else if (this.getName().equals("player2")) {
            enemyPlayer = gameTable.getPlayer1();
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
        } else if ((!attackedCard.isTank()) && gameTable.getPlayer2().hasTankOnTable(gameTable)) {
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
            enemyPlayer = gameTable.getPlayer2();
        } else if (this.getName().equals("player2")) {
            enemyPlayer = gameTable.getPlayer1();
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

    public void placeCard(GameTable gameTable, Actions action) {
        boolean flag = true;
        //if the card mana is more than player mana
        if(this.getCardsInHand().get(action.getHandIdx()).getMana() >
                this.getMana()) {
            flag = false;
            System.out.println(NOT_PLACE_ENVIRONMENT_MESSAGE);
        }
        //verify if the card type is Environment
        if(this.getCardsInHand().get(action.getHandIdx()).getType().equals(ENVIRONMENT)) {
            flag = false;
            System.out.println(NOT_ENOUGH_MANA_MESSAGE);
        }
        //if there is place on the table row to place the card (and the card is eligible)
        if (!this.verifySpaceOnTableRow(gameTable, action.getAffectedRow())) {
            flag = false;
            System.out.println(FULL_ROW_MESSAGE);
        } //if all conditions are true, we place the card
        if (flag) {
            this.putCardOnTable(this.getCardsInHand().get(action.getHandIdx()), gameTable, action.getAffectedRow());
        }
    }

    boolean executeAction(Actions action, GameTable gameTable) {
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

        } else if (action.getCommand().equals("placeCard")) {
            //this.placeCard(gameTable, action);
        } else {
            System.out.println(action.getCommand() + " doesn't exist!!!");
        }
        // return false if we don't end the turn and continue with same player
        return false;
    }
}