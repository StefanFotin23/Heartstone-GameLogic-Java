package Gameplay;

import Cards.*;
import JsonOutput.*;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;

import java.util.ArrayList;

import static Constants.Constants.*;

public class Game {
    private final int playerOneDeckIdx;
    private final int playerTwoDeckIdx;
    private final int shuffleSeed;
    private final Hero playerOneHero;
    private final Hero playerTwoHero;
    private final int startingPlayer;
    private final ArrayList<Actions> actions;
    private final int gameNumber;
    private GameTable gameTable;

    public Game(int playerOneDeckIdx, int playerTwoDeckIdx, int shuffleSeed, Hero playerOneHero, Hero playerTwoHero, int startingPlayer, ArrayList<Actions> actions, int gameNumber) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.actions = actions;
        this.startingPlayer = startingPlayer;
        this.gameNumber = gameNumber;
    }

    public Game(GameInput obj, int gameNumber) {
        this.playerOneDeckIdx = obj.getStartGame().getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = obj.getStartGame().getPlayerTwoDeckIdx();
        this.shuffleSeed = obj.getStartGame().getShuffleSeed();
        this.playerOneHero = new Hero(obj.getStartGame().getPlayerOneHero());
        this.playerTwoHero = new Hero(obj.getStartGame().getPlayerTwoHero());
        this.gameNumber = gameNumber;
        ArrayList<Actions> actionList = new ArrayList<>();
        for (int i = 0; i < obj.getActions().size(); i++) {
            actionList.add(new Actions(obj.getActions().get(i)));
        }
        this.actions = actionList;
        this.startingPlayer = obj.getStartGame().getStartingPlayer();
    }

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public Hero getPlayerOneHero() {
        return playerOneHero;
    }

    public Hero getPlayerTwoHero() {
        return playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public ArrayList<Actions> getActions() {
        return actions;
    }

    public ArrayNode startGame(DecksCollection playerOneDecks, DecksCollection playerTwoDecks) throws JsonProcessingException {
        ArrayNode arrayNode = null;
        Player player1 = new Player("player1", playerOneDecks.getDecks().get(playerOneDeckIdx), playerOneHero, shuffleSeed);
        Player player2 = new Player("player2", playerTwoDecks.getDecks().get(playerTwoDeckIdx), playerTwoHero, shuffleSeed);

        if (startingPlayer == 1) {
            arrayNode =  playGame(player1, player2, actions);
        } else if(startingPlayer == 2) {
            arrayNode = playGame(player2, player1, actions);
        } else {
            System.out.println("StartingPlayer error!!!");
        }
        return arrayNode;
    }

    public void initNewRound(Player firstPlayer, Player secondPlayer, int roundNr) {
        firstPlayer.getCardsInHand().add(firstPlayer.getDeck().pop());
        secondPlayer.getCardsInHand().add(secondPlayer.getDeck().pop());
        int manaBonus = roundNr;
        if (manaBonus > 10) {
            manaBonus = 10;
        }
        firstPlayer.addMana(manaBonus);
        secondPlayer.addMana(manaBonus);
    }

    public ArrayNode playGame(Player firstPlayer, Player secondPlayer, ArrayList<Actions> actions) throws JsonProcessingException {
        ArrayNode output = new ObjectMapper().createArrayNode();
        boolean playerOneTurnBeenPlayed = true;
        boolean playerTwoTurnBeenPlayed = true;
        //we initialize them with true in order to init the first Round
        int turnCounter = 1; //number of current turn
        int roundCounter = 0;
        gameTable = new GameTable(firstPlayer, secondPlayer);

        for (int i = 0; i < actions.size(); i++) {
            //here we check if we start a new round
            if (playerOneTurnBeenPlayed && playerTwoTurnBeenPlayed) {
                this.initNewRound(firstPlayer, secondPlayer, turnCounter / 2 + 1);
                roundCounter++;
                playerOneTurnBeenPlayed = false;
                playerTwoTurnBeenPlayed = false;
            }

            //here we check if the action is a statistic that will be done by the game (not players)
            if (actions.get(i).getCommand().equals("getCardsOnTable")) {
                ArrayList<Card> cardsOnTable = this.gameTable.getCardsOnTable();
                // CardsOnTableJson cardsOnTableJson = null;
                //JsonNode jsonNode = JsonParse.parseObjectToJson(cardsOnTableJson);
                //output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getFrozenCardsOnTable")) {
               ArrayList<Card> frozenCardsOnTable = this.gameTable.getFrozenCards();
            } else if (actions.get(i).getCommand().equals("getPlayerOneWins")) {
                int playerOneWins = Statistics.getInstance().getPlayerOneWins();
                GetNumberForCommandJsonOutput getPlayerOneWins = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), playerOneWins);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerOneWins);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getPlayerTwoWins")) {
                int playerTwoWins = Statistics.getInstance().getPlayerTwoWins();
                GetNumberForCommandJsonOutput getPlayerTwoWins = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), playerTwoWins);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerTwoWins);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getTotalGamesPlayed")) {
                int totalGamesPlayed = Statistics.getInstance().getGameNumber();
                GetNumberForCommandJsonOutput getTotalGamesPlayed = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), totalGamesPlayed);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getTotalGamesPlayed);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getPlayerDeck")) {
                ArrayList<Card> playerDeck =
                        this.gameTable.getPlayer(actions.get(i).getPlayerIdx()).getDeck().getCards();
                if (playerDeck == null) {
                    System.out.println("getPlayerDeck error");
                }
                ArrayList<EnvironmentCardJsonOutput> deckOutput = new ArrayList<>();
                for (int h = 0; h < playerDeck.size(); h++) {
                    if (playerDeck.get(h).getType().equals(ENVIRONMENT)) {
                        deckOutput.add(new EnvironmentCardJsonOutput(playerDeck.get(h)));
                    } else {
                        deckOutput.add(new CardJsonOutput(playerDeck.get(h)));
                    }
                }
                GetPlayerDeckJsonOutput playerDeckJson = new GetPlayerDeckJsonOutput(actions.get(i).getPlayerIdx(), deckOutput);
                JsonNode jsonNode = JsonParse.parseObjectToJson(playerDeckJson);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getPlayerHero")) {
                Hero playerHero = this.gameTable.getPlayer(actions.get(i).getPlayerIdx()).getHero();
                if (playerHero == null) {
                    System.out.println("getPlayerHero error");
                }
                HeroJsonOutput heroJsonOutput = new HeroJsonOutput(playerHero);
                GetPlayerHeroJsonOutput getPlayerHeroJsonOutput = new GetPlayerHeroJsonOutput(actions.get(i).getPlayerIdx(), heroJsonOutput);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerHeroJsonOutput);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getCardsInHand")) {
                ArrayList<Card> cardsInHand;
                if (actions.get(i).getPlayerIdx() == 1) {
                    cardsInHand = this.gameTable.getPlayer1().getCardsInHand();
                } else if (actions.get(i).getPlayerIdx() == 2) {
                    cardsInHand = this.gameTable.getPlayer2().getCardsInHand();
                } else {
                    System.out.println("getCardsInHand error");
                }
            } else if (actions.get(i).getCommand().equals("getPlayerMana")) {
                int playerMana;
                if (actions.get(i).getPlayerIdx() == 1) {
                    playerMana = this.gameTable.getPlayer1().getMana();
                } else if (actions.get(i).getPlayerIdx() == 2) {
                    playerMana = this.gameTable.getPlayer2().getMana();
                } else {
                    System.out.println("getPlayerMana error");
                }
            } else if (actions.get(i).getCommand().equals("getEnvironmentCardsInHand")) {
                ArrayList<Card> environmentCardsInHand;
                if (actions.get(i).getPlayerIdx() == 1) {
                    environmentCardsInHand = this.gameTable.getPlayer1().getEnvironmentalCardsInHand();
                } else if (actions.get(i).getPlayerIdx() == 2) {
                    environmentCardsInHand = this.gameTable.getPlayer2().getEnvironmentalCardsInHand();
                } else {
                    System.out.println("getEnvironmentCardsInHand error");
                }
            } else if (actions.get(i).getCommand().equals("getCardAtPosition")) {
                Card card = this.gameTable.getCardFromTable(actions.get(i).getX(), actions.get(i).getY());
                if (card == null) {
                    String errorString = NO_CARD_THERE;
                }
            } else {
                //if command is "endPlayerTurn", the "player turn" flag becomes true
                if (turnCounter % 2 == 1 && !playerOneTurnBeenPlayed) {
                    if (actions.get(i).getCommand().equals("getPlayerTurn")) {
                        int playerTurn;
                        if (firstPlayer.getName().equals(PLAYER1)) {
                            playerTurn = 1;
                        } else {
                            playerTurn = 2;
                        }
                        GetNumberForCommandJsonOutput getPlayerTurnJsonOutput = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), playerTurn);
                        JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerTurnJsonOutput);
                        output.add(jsonNode);
                    } else {
                        playerOneTurnBeenPlayed = firstPlayer.executeAction(actions.get(i), gameTable);
                        if (playerOneTurnBeenPlayed) {
                            turnCounter++;
                        }
                    }
                } else {
                    if (actions.get(i).getCommand().equals("getPlayerTurn")) {
                        int playerTurn;
                        if (secondPlayer.getName().equals(PLAYER1)) {
                            playerTurn = 1;
                        } else {
                            playerTurn = 2;
                        }
                        GetNumberForCommandJsonOutput getPlayerTurnJsonOutput = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), playerTurn);
                        JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerTurnJsonOutput);
                        output.add(jsonNode);
                    } else {
                        playerTwoTurnBeenPlayed = secondPlayer.executeAction(actions.get(i), gameTable);
                        if (playerTwoTurnBeenPlayed) {
                            turnCounter++;
                        }
                    }
                }
            }
        }
        return output;
    }
}
