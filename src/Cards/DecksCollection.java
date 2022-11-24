package Cards;

import fileio.CardInput;

import java.util.ArrayList;

public class DecksCollection {
    private final int nrCardsInDeck;
    private final int nrDecks;
    private final ArrayList<ArrayList<Card>> decks;

    public DecksCollection(int nrCardsInDeck, int nrDecks, ArrayList<ArrayList<Card>> decks) {
        this.nrCardsInDeck = nrCardsInDeck;
        this.nrDecks = nrDecks;
        this.decks = decks;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }
}
