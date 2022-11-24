package Gameplay;

import Cards.Card;
import Cards.Minion;
import JsonOutput.*;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

import java.util.ArrayList;

import static Constants.Constants.*;

public class GameManager {
    public static void printCardsOnTable(Game game, ArrayNode output) throws JsonProcessingException {
        ArrayList<ArrayList<EnvironmentCardJsonOutput>> cardsOnTableOutput = new ArrayList<>();

        ArrayList<EnvironmentCardJsonOutput> p1BackRowOut = new ArrayList<>();
        for (int i = 0; i < game.getPlayer1().getBackRow().size(); i++) {
            p1BackRowOut.add(new CardJsonOutput(game.getPlayer1().getBackRow().get(i)));
        }

        ArrayList<EnvironmentCardJsonOutput> p1FrontRowOut = new ArrayList<>();
        for (int i = 0; i < game.getPlayer1().getFrontRow().size(); i++) {
            p1FrontRowOut.add(new CardJsonOutput(game.getPlayer1().getFrontRow().get(i)));
        }

        ArrayList<EnvironmentCardJsonOutput> p2FrontRowOut = new ArrayList<>();
        for (int i = 0; i < game.getPlayer2().getFrontRow().size(); i++) {
            p2FrontRowOut.add(new CardJsonOutput(game.getPlayer2().getFrontRow().get(i)));
        }

        ArrayList<EnvironmentCardJsonOutput> p2BackRowOut = new ArrayList<>();
        for (int i = 0; i < game.getPlayer2().getBackRow().size(); i++) {
            p2BackRowOut.add(new CardJsonOutput(game.getPlayer2().getBackRow().get(i)));
        }

        cardsOnTableOutput.add(p2BackRowOut);
        cardsOnTableOutput.add(p2FrontRowOut);
        cardsOnTableOutput.add(p1FrontRowOut);
        cardsOnTableOutput.add(p1BackRowOut);

        CardsOnTableJsonOutput playerDeckJson = new CardsOnTableJsonOutput("getCardsOnTable", cardsOnTableOutput);
        JsonNode jsonNode = JsonParse.parseObjectToJson(playerDeckJson);
        output.add(jsonNode);
    }

    public static ArrayList<Card> getCardsOnTable(Game game) {
        ArrayList<Card> getCardsOnTable = new ArrayList<>();
        getCardsOnTable.addAll(game.getPlayer1().getFrontRow());
        getCardsOnTable.addAll(game.getPlayer1().getBackRow());
        getCardsOnTable.addAll(game.getPlayer2().getFrontRow());
        getCardsOnTable.addAll(game.getPlayer2().getBackRow());
        return getCardsOnTable;
    }

    public static Minion getCardAtPosition(Game game, int x, int y) {
        if (x == 0 && game.getPlayer2().getBackRow().size() > y) {
            return game.getPlayer2().getBackRow().get(y);
        } else if (x == 1 && game.getPlayer2().getFrontRow().size() > y) {
            return game.getPlayer2().getFrontRow().get(y);
        } else if (x == 2 && game.getPlayer1().getFrontRow().size() > y) {
            return game.getPlayer1().getFrontRow().get(y);
        } else if (x == 3 && game.getPlayer1().getBackRow().size() > y) {
            return game.getPlayer1().getBackRow().get(y);
        }
        System.out.println("Couldn't find card on table at pos (" + x + ',' + y + ')');
        return null;
    }

    public static Minion getCardAtPosition(Game game, Coordinates coordinates) {
        if (coordinates.getX() == 0 && game.getPlayer2().getBackRow().size() > coordinates.getY()) {
            return game.getPlayer2().getBackRow().get(coordinates.getY());
        } else if (coordinates.getX() == 1 && game.getPlayer2().getFrontRow().size() > coordinates.getY()) {
            return game.getPlayer2().getFrontRow().get(coordinates.getY());
        } else if (coordinates.getX() == 2 && game.getPlayer1().getFrontRow().size() > coordinates.getY()) {
            return game.getPlayer1().getFrontRow().get(coordinates.getY());
        } else if (coordinates.getX() == 3 && game.getPlayer1().getBackRow().size() > coordinates.getY()) {
            return game.getPlayer1().getBackRow().get(coordinates.getY());
        }
        System.out.println("Couldn't find card on table at pos (" + coordinates.getX() + ',' + coordinates.getY() + ')');
        return null;
    }

    public static boolean isPlayersCard(Player player, Coordinates cardCoordinates) {
        if (player.getName().equals(PLAYER1)) {
            if (cardCoordinates.getX() == 2 || cardCoordinates.getX() == 3) {
                return true;
            }
        } else if (player.getName().equals(PLAYER2)) {
            if (cardCoordinates.getX() == 0 || cardCoordinates.getX() == 1) {
                return true;
            }
        }
        return false;
    }

    public static Player getPlayerEnemy(Game game, Player player) {
        if (player.getName().equals(PLAYER1)) {
            return game.getPlayer2();
        } else if (player.getName().equals(PLAYER2)) {
            return game.getPlayer1();
        } else {
            System.out.println("Couldn't get " + player + " enemy");
            return null;
        }
    }

    public static ArrayList<Minion> getRow(Game game, int rowNumber) {
        if (rowNumber == 0) {
            return game.getPlayer2().getBackRow();
        } else if (rowNumber == 1) {
            return game.getPlayer2().getFrontRow();
        } else if (rowNumber == 2) {
            return game.getPlayer1().getFrontRow();
        } else if (rowNumber == 3) {
            return game.getPlayer1().getBackRow();
        }
        System.out.println("Couldn't find row " + rowNumber + " on table");
        return null;
    }

    public static void printFrozenCardsOnTable(Game game, ArrayNode output) throws JsonProcessingException {
        ArrayList<Card> cardsOnTable = GameManager.getCardsOnTable(game);
        ArrayList<Card> frozenCards = new ArrayList<>();
        for (int i = 0; i < cardsOnTable.size(); i++) {
            if (cardsOnTable.get(i).isFrozen()) {
                frozenCards.add(cardsOnTable.get(i));
            }
        }
        ArrayList<CardJsonOutput> frozenCardsOnTableOutput = new ArrayList<>();
        for (int h = 0; h < frozenCards.size(); h++) {
            if (frozenCards.get(h).getType().equals(ENVIRONMENT)) {
                frozenCardsOnTableOutput.add(new CardJsonOutput(frozenCards.get(h)));
            } else {
                frozenCardsOnTableOutput.add(new CardJsonOutput(frozenCards.get(h)));
            }
        }
        GetFrozenCardsOutput frozenCardsOutput = new GetFrozenCardsOutput(frozenCardsOnTableOutput);
        JsonNode jsonNode = JsonParse.parseObjectToJson(frozenCardsOutput);
        output.add(jsonNode);
    }

    public static void printPlayerDeck(Player player, ArrayNode output) throws JsonProcessingException {
        if (player.getDeck() == null) {
            System.out.println("getPlayerDeck error");
        }
        ArrayList<EnvironmentCardJsonOutput> deckOutput = new ArrayList<>();
        for (int h = 0; h < player.getDeck().size(); h++) {
            if (player.getDeck().get(h).getType().equals(ENVIRONMENT)) {
                deckOutput.add(new EnvironmentCardJsonOutput(player.getDeck().get(h)));
            } else {
                deckOutput.add(new CardJsonOutput(player.getDeck().get(h)));
            }
        }
        GetArrayOfCardsJsonOutput playerDeckJson = new GetArrayOfCardsJsonOutput("getPlayerDeck", player.getIndex(), deckOutput);
        JsonNode jsonNode = JsonParse.parseObjectToJson(playerDeckJson);
        output.add(jsonNode);
    }

    public static void printCardsInHand(Player player, ArrayNode output) throws JsonProcessingException {
        ArrayList<Card> cardsInHand = player.getCardsInHand();
        ArrayList<EnvironmentCardJsonOutput> cardsOutput = new ArrayList<>();
        for (int h = 0; h < cardsInHand.size(); h++) {
            if (cardsInHand.get(h) == null) {
                System.out.println("getCardsInHand nullPointerException maybe");
            }
            if (cardsInHand.get(h) != null && cardsInHand.get(h).getType().equals(ENVIRONMENT)) {
                cardsOutput.add(new EnvironmentCardJsonOutput(cardsInHand.get(h)));
            } else {
                if (cardsInHand.get(h) != null) {
                    cardsOutput.add(new CardJsonOutput(cardsInHand.get(h)));
                }
            }
        }
        GetArrayOfCardsJsonOutput cardsInHandJsonOutput = new GetArrayOfCardsJsonOutput("getCardsInHand", player.getIndex(), cardsOutput);
        JsonNode jsonNode = JsonParse.parseObjectToJson(cardsInHandJsonOutput);
        output.add(jsonNode);
    }

    public static void printEnvironmentCardsInHand(Player player, ArrayNode output) throws JsonProcessingException {
        ArrayList<Card> environmentCardsInHand = player.getEnvironmentalCardsInHand();
        if (environmentCardsInHand == null) {
            System.out.println("getEnvironmentCardsInHand error");
        }
        ArrayList<EnvironmentCardJsonOutput> cardsOutput = new ArrayList<>();
        for (int h = 0; h < environmentCardsInHand.size(); h++) {
            if (environmentCardsInHand.get(h) == null) {
                System.out.println("getEnvironmentCardsInHand nullPointerException maybe");
            }
            if (environmentCardsInHand.get(h) != null) {
                cardsOutput.add(new EnvironmentCardJsonOutput(environmentCardsInHand.get(h)));
            }
        }
        GetArrayOfCardsJsonOutput cardsInHandJsonOutput = new GetArrayOfCardsJsonOutput("getEnvironmentCardsInHand", player.getIndex(), cardsOutput);
        JsonNode jsonNode = JsonParse.parseObjectToJson(cardsInHandJsonOutput);
        output.add(jsonNode);
    }

    public static void printCardAtPosition(Game game, int x, int y, ArrayNode output) throws JsonProcessingException {
        Card card = GameManager.getCardAtPosition(game, x, y);

        if (card == null) {
            //if there is no card
            String errorString = NO_CARD_THERE;
            GetCardErrorStringOutput errorStringJsonOutput = new GetCardErrorStringOutput(
                    "getCardAtPosition", x, y, errorString);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStringJsonOutput);
            output.add(jsonNode);
        } else {
            //if there is a card
            if (card.getType().equals(ENVIRONMENT)) {
                GetEnvironmentCardJsonOutput jsonOutput = new GetEnvironmentCardJsonOutput("getCardAtPosition",
                        x, y, card);
                JsonNode jsonNode = JsonParse.parseObjectToJson(jsonOutput);
                output.add(jsonNode);
            } else {
                GetCardJsonOutput jsonOutput = new GetCardJsonOutput("getCardAtPosition",
                        x, y, card);
                JsonNode jsonNode = JsonParse.parseObjectToJson(jsonOutput);
                output.add(jsonNode);
            }
        }
    }

    public static void specialPrintCardAtPosition(Game game, int x, int y, ArrayNode output) throws JsonProcessingException {
        Card card = GameManager.getCardAtPosition(game, x, y);

        if (card == null) {
            //if there is no card
            String errorString = NO_CARD_THERE;
            GetCardErrorStringOutput errorStringJsonOutput = new GetCardErrorStringOutput(
                    "getCardAtPosition", x, y, errorString);
            JsonNode jsonNode = JsonParse.parseObjectToJson(errorStringJsonOutput);
            output.add(jsonNode);
        } else {
            //if there is a card
            if (card.getType().equals(ENVIRONMENT)) {
                GetEnvironmentCardJsonOutput jsonOutput = new GetEnvironmentCardJsonOutput("getCardAtPosition",
                        x, y, card);
                JsonNode jsonNode = JsonParse.parseObjectToJson(jsonOutput);
                output.add(jsonNode);
            } else {
                GetCardSpecialJsonOutput jsonOutput = new GetCardSpecialJsonOutput("getCardAtPosition", card);
                JsonNode jsonNode = JsonParse.parseObjectToJson(jsonOutput);
                output.add(jsonNode);
            }
        }
    }

    public static void deleteDeadCardsOnTable(Game game) {
        for (int i = 0; i < game.getPlayer1().getBackRow().size(); i++) {
            if (game.getPlayer1().getBackRow().get(i).getHealth() <= 0) {
                game.getPlayer1().getBackRow().remove(i);
            }
        }
        for (int i = 0; i < game.getPlayer1().getFrontRow().size(); i++) {
            if (game.getPlayer1().getFrontRow().get(i).getHealth() <= 0) {
                game.getPlayer1().getFrontRow().remove(i);
            }
        }
        for (int i = 0; i < game.getPlayer2().getBackRow().size(); i++) {
            if (game.getPlayer2().getBackRow().get(i).getHealth() <= 0) {
                game.getPlayer2().getBackRow().remove(i);
            }
        }
        for (int i = 0; i < game.getPlayer2().getFrontRow().size(); i++) {
            if (game.getPlayer2().getFrontRow().get(i).getHealth() <= 0) {
                game.getPlayer2().getFrontRow().remove(i);
            }
        }
    }
}
