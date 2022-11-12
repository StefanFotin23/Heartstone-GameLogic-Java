package Gameplay;

import Cards.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class GameSeries {
    private final DecksCollection playerOneDecks;
    private final DecksCollection playerTwoDecks;
    private final ArrayList<Game> games;

    public GameSeries(DecksCollection playerOneDecks, DecksCollection playerTwoDecks, ArrayList<Game> games) {
        this.playerOneDecks = playerOneDecks;
        this.playerTwoDecks = playerTwoDecks;
        this.games = games;
    }

    public DecksCollection getPlayerOneDecks() {
        return playerOneDecks;
    }

    public DecksCollection getPlayerTwoDecks() {
        return playerTwoDecks;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    //main method for a gameSeries
    public ArrayNode playSeries() throws JsonProcessingException {
        ArrayNode output = null;
        for (int i = 0; i < games.size(); i++) {
            output = (games.get(i).startGame(playerOneDecks, playerTwoDecks));
        }
        return output;
    }
}
