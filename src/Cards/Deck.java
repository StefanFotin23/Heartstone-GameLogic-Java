package Cards;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    //pop first element of card deck
    public Card pop() {
        if (cards.isEmpty()) {
            return null;
        }
        Card c = cards.get(0);
        cards.remove(0);
        return c;
    }

    public int getSize() {
        return cards.size();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
