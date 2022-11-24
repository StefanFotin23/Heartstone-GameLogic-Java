package Gameplay;

import Cards.Card;
import fileio.Coordinates;
import java.util.ArrayList;
import static Constants.Constants.*;

public class GameTable {
    private final Player firstPlayer;
    private final Player secondPlayer;
    private ArrayList<Card> playerOneFrontRow = new ArrayList<>();
    private ArrayList<Card> playerOneBackRow = new ArrayList<>();
    private ArrayList<Card> playerTwoFrontRow = new ArrayList<>();
    private ArrayList<Card> playerTwoBackRow = new ArrayList<>();

    public GameTable(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public Card getPlayerCard(Player player, Coordinates coordinates) {
        if (player.getName().equals("player1")) {
            if (coordinates.getX() == PLAYER_ONE_BACK_ROW_INDEX &&
                    coordinates.getY() < playerOneBackRow.size()) {
                return playerOneBackRow.get(coordinates.getY());
            }
            if (coordinates.getX() == PLAYER_ONE_FRONT_ROW_INDEX &&
                    coordinates.getY() < playerOneFrontRow.size()) {
                return playerOneFrontRow.get(coordinates.getY());
            }
        } else if (player.getName().equals("player2")) {
            if (coordinates.getX() == PLAYER_TWO_BACK_ROW_INDEX &&
                    coordinates.getY() < playerTwoBackRow.size()) {
                return playerTwoBackRow.get(coordinates.getY());
            }
            if (coordinates.getX() == PLAYER_TWO_FRONT_ROW_INDEX &&
                    coordinates.getY() < playerTwoFrontRow.size()) {
                return playerTwoFrontRow.get(coordinates.getY());
            }
        }
        return null;
    }

    public Card getPlayerCard(Player player, int x, int y) {
        if (player.getName().equals("player1")) {
            if (x == PLAYER_ONE_BACK_ROW_INDEX &&
                    y < playerOneBackRow.size()) {
                return playerOneBackRow.get(y);
            }
            if (x == PLAYER_ONE_FRONT_ROW_INDEX &&
                    y < playerOneFrontRow.size()) {
                return playerOneFrontRow.get(y);
            }
        } else if (player.getName().equals("player2")) {
            if (x == PLAYER_TWO_BACK_ROW_INDEX &&
                    y < playerTwoBackRow.size()) {
                return playerTwoBackRow.get(y);
            }
            if (x == PLAYER_TWO_FRONT_ROW_INDEX &&
                    y < playerTwoFrontRow.size()) {
                return playerTwoFrontRow.get(y);
            }
        }
        return null;
    }

    public void removeCard(Coordinates coordinates) {
        if (coordinates.getX() == PLAYER_ONE_BACK_ROW_INDEX) {
            playerOneBackRow.remove(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_ONE_FRONT_ROW_INDEX) {
            playerOneFrontRow.remove(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_TWO_BACK_ROW_INDEX) {
            playerTwoBackRow.remove(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_TWO_FRONT_ROW_INDEX) {
            playerTwoFrontRow.remove(coordinates.getY());
        }
    }

    public ArrayList<Card> getPlayerOneFrontRow() {
        return playerOneFrontRow;
    }

    public void setPlayerOneFrontRow(ArrayList<Card> playerOneFrontRow) {
        this.playerOneFrontRow = playerOneFrontRow;
    }

    public ArrayList<Card> getPlayerOneBackRow() {
        return playerOneBackRow;
    }

    public void setPlayerOneBackRow(ArrayList<Card> playerOneBackRow) {
        this.playerOneBackRow = playerOneBackRow;
    }

    public ArrayList<Card> getPlayerTwoFrontRow() {
        return playerTwoFrontRow;
    }

    public void setPlayerTwoFrontRow(ArrayList<Card> playerTwoFrontRow) {
        this.playerTwoFrontRow = playerTwoFrontRow;
    }

    public ArrayList<Card> getPlayerTwoBackRow() {
        return playerTwoBackRow;
    }

    public void setPlayerTwoBackRow(ArrayList<Card> playerTwoBackRow) {
        this.playerTwoBackRow = playerTwoBackRow;
    }

    public ArrayList<Card> getCardsOnTable() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < playerOneBackRow.size(); i++) {
            cards.add(playerOneBackRow.get(i));
        }
        for (int i = 0; i < playerOneFrontRow.size(); i++) {
            cards.add(playerOneFrontRow.get(i));
        }
        for (int i = 0; i < playerTwoBackRow.size(); i++) {
            cards.add(playerTwoBackRow.get(i));
        }
        for (int i = 0; i < playerTwoFrontRow.size(); i++) {
            cards.add(playerTwoFrontRow.get(i));
        }
        return cards;
    }

    public ArrayList<Card> getRow(int row) {
        if (row == PLAYER_ONE_BACK_ROW_INDEX) {
            return playerOneBackRow;
        }
        if (row == PLAYER_ONE_FRONT_ROW_INDEX) {
            return playerOneFrontRow;
        }
        if (row == PLAYER_TWO_BACK_ROW_INDEX) {
            return playerTwoBackRow;
        }
        if (row == PLAYER_TWO_FRONT_ROW_INDEX) {
            return playerTwoFrontRow;
        }
        return null;
    }

    public ArrayList<Card> getMirroredRow(int row) {
        if (row == PLAYER_ONE_BACK_ROW_INDEX) {
            return playerTwoBackRow;
        }
        if (row == PLAYER_ONE_FRONT_ROW_INDEX) {
            return playerTwoFrontRow;
        }
        if (row == PLAYER_TWO_BACK_ROW_INDEX) {
            return playerOneBackRow;
        }
        if (row == PLAYER_TWO_FRONT_ROW_INDEX) {
            return playerOneFrontRow;
        }
        return null;
    }

    public ArrayList<ArrayList<Card>> getCardsFromTable() {
        ArrayList<ArrayList<Card>> cards = new ArrayList<>();
        cards.add(playerOneBackRow);
        cards.add(playerOneFrontRow);
        cards.add(playerTwoFrontRow);
        cards.add(playerTwoBackRow);
        return cards;
    }

    public ArrayList<Card> getFrozenCards() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < playerOneBackRow.size(); i++) {
            if(playerOneBackRow.get(i).isFrozen()) {
                cards.add(playerOneBackRow.get(i));
            }
        }
        for (int i = 0; i < playerOneFrontRow.size(); i++) {
            if(playerOneFrontRow.get(i).isFrozen()) {
                cards.add(playerOneFrontRow.get(i));
            }
        }
        for (int i = 0; i < playerTwoFrontRow.size(); i++) {
            if(playerTwoFrontRow.get(i).isFrozen()) {
                cards.add(playerTwoFrontRow.get(i));
            }
        }
        for (int i = 0; i < playerTwoBackRow.size(); i++) {
            if(playerTwoBackRow.get(i).isFrozen()) {
                cards.add(playerTwoBackRow.get(i));
            }
        }
        return cards;
    }

    public Card getCardFromTable(Coordinates coordinates) {
        if (coordinates.getX() == PLAYER_ONE_BACK_ROW_INDEX) {
            return  playerOneBackRow.get(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_ONE_FRONT_ROW_INDEX) {
            return  playerOneFrontRow.get(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_TWO_BACK_ROW_INDEX) {
            return  playerTwoBackRow.get(coordinates.getY());
        }
        if (coordinates.getX() == PLAYER_TWO_FRONT_ROW_INDEX) {
            return  playerTwoFrontRow.get(coordinates.getY());
        }
        return null;
    }

    public Card getCardFromTable(int x, int y) {
        if (x == PLAYER_ONE_BACK_ROW_INDEX && y < playerOneBackRow.size()) {
            return  playerOneBackRow.get(y);
        }
        if (x == PLAYER_ONE_FRONT_ROW_INDEX && y < playerOneFrontRow.size()) {
            return  playerOneFrontRow.get(y);
        }
        if (x == PLAYER_TWO_BACK_ROW_INDEX && y < playerTwoBackRow.size()) {
            return  playerTwoBackRow.get(y);
        }
        if (x == PLAYER_TWO_FRONT_ROW_INDEX && y < playerTwoFrontRow.size()) {
            return  playerTwoFrontRow.get(y);
        }
        return null;
    }

    public Player getPlayer(int index) {
        if (index == 1) {
            if (firstPlayer.getName().equals(PLAYER1)) {
                return firstPlayer;
            } else {
                return secondPlayer;
            }
        } else if (index == 2) {
            if (secondPlayer.getName().equals(PLAYER2)) {
                return secondPlayer;
            } else {
                return firstPlayer;
            }
        } else {
            System.out.println("getPlayer index = " + index + " error");
            return null;
        }
    }
}
