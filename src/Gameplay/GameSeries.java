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

    //main method for a gameSeries
    public void playSeries(ArrayNode output, String filePath1) throws JsonProcessingException {
        for (int i = 0; i < games.size(); i++) {
            games.get(i).startGame(playerOneDecks, playerTwoDecks, output, filePath1);
        }
    }
}
