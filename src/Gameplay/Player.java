package Gameplay;

import Cards.*;
import fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import static Constants.Constants.*;

public class Player {
    private final String name;
    private final int index;
    private ArrayList<Card> deck;
    private final Hero hero;
    private ArrayList<Card> cardsInHand = new ArrayList<>();
    private int mana;
    private ArrayList<Minion> frontRow = new ArrayList<>();
    private ArrayList<Minion> backRow = new ArrayList<>();

    public Player(String name, Hero hero, int shuffleSeed, int index, ArrayList<Card> deck) {
        this.name = name;
        this.hero = hero;
        this.index = index;
        this.mana = 0;
        this.deck = deck;
        Random rand = new Random();
        rand.setSeed(shuffleSeed);
        Collections.shuffle(this.deck, rand);
    }

    public ArrayList<Minion> getFrontRow() {
        return frontRow;
    }

    public ArrayList<Minion> getBackRow() {
        return backRow;
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

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
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

    public void getNextCardFromDeck() {
        if (!this.getDeck().isEmpty()) {
            Card c = this.getDeck().get(0);
            this.getDeck().remove(0);
            this.cardsInHand.add(c);
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

    public boolean hasTankOnTable() {
        for (int i = 0; i < this.getBackRow().size(); i++) {
            if (this.getBackRow().get(i).isTank()) {
                return true;
            }
        }
        for (int i = 0; i < this.getFrontRow().size(); i++) {
            if (this.getFrontRow().get(i).isTank()) {
                return true;
            }
        }
        return false;
    }

    public void removeCardFromTable(Coordinates coordinates) {
        if (this.getName().equals(PLAYER1)) {
            //frontRow
            if (coordinates.getX() == 2){
                this.getFrontRow().remove(coordinates.getY());
            }
            //backRow
            if (coordinates.getX() == 3) {
                this.getBackRow().remove(coordinates.getY());
            }
        } else if (this.getName().equals(PLAYER2)) {
            //backRow
            if (coordinates.getX() == 0){
                this.getBackRow().remove(coordinates.getY());
            }
            //frontRow
            if (coordinates.getX() == 1) {
                this.getFrontRow().remove(coordinates.getY());
            }
        }
    }

    public boolean ownsRow(int rowIdx) {
        if (this.getName().equals(PLAYER1)) {
            if (rowIdx == 2 || rowIdx == 3) {
                return true;
            } else {
                return false;
            }
        } else if (this.getName().equals(PLAYER2)) {
            if (rowIdx == 0 || rowIdx == 1) {
                return true;
            } else {
                return false;
            }
        }
        System.out.println("ownsRow error on player");
        return false;
    }

    public int getIndex() {
        return this.index;
    }
}