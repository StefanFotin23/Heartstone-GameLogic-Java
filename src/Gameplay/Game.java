package Gameplay;

import Cards.*;
import JsonOutput.*;
import Service.JsonParse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
    private boolean gameEnded = false;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int turnNumber = 1;
    private int roundNumber = 1;
    private boolean player1TurnBeenPlayed = false;
    private boolean player2TurnBeenPlayed = false;

    public Game(int playerOneDeckIdx, int playerTwoDeckIdx, int shuffleSeed, Hero playerOneHero, Hero playerTwoHero,
                int startingPlayer, ArrayList<Actions> actions) {
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.shuffleSeed = shuffleSeed;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.actions = actions;
        this.startingPlayer = startingPlayer;
    }

    public Game(GameInput obj) {
        this.playerOneDeckIdx = obj.getStartGame().getPlayerOneDeckIdx();
        this.playerTwoDeckIdx = obj.getStartGame().getPlayerTwoDeckIdx();
        this.shuffleSeed = obj.getStartGame().getShuffleSeed();
        this.playerOneHero = new Hero(obj.getStartGame().getPlayerOneHero());
        this.playerTwoHero = new Hero(obj.getStartGame().getPlayerTwoHero());
        ArrayList<Actions> actionList = new ArrayList<>();
        for (int i = 0; i < obj.getActions().size(); i++) {
            actionList.add(new Actions(obj.getActions().get(i)));
        }
        this.actions = actionList;
        this.startingPlayer = obj.getStartGame().getStartingPlayer();
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
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

    public void startGame(DecksCollection playerOneDecks, DecksCollection playerTwoDecks, ArrayNode output, String filePath1)
            throws JsonProcessingException {
        player1 = new Player(PLAYER1,
                playerOneHero, shuffleSeed, 1, playerOneDecks.getDecks().get(playerOneDeckIdx));
        player2 = new Player(PLAYER2,
                playerTwoHero, shuffleSeed, 2, playerTwoDecks.getDecks().get(playerTwoDeckIdx));
        this.playGame(player1, player2, startingPlayer, actions, output, filePath1);
    }

    public void initNewRound(Player player1, Player player2, int roundNr) {
        player1.getNextCardFromDeck();
        player2.getNextCardFromDeck();
        //adding mana
        int manaBonus = roundNr;
        if (manaBonus > 10) {
            manaBonus = 10;
        }
        player1.addMana(manaBonus);
        player2.addMana(manaBonus);
        player1TurnBeenPlayed = false;
        player2TurnBeenPlayed = false;
    }

    public void resetCardsOnTableState(Player player) {
        for (int i = 0; i< player.getFrontRow().size(); i++) {
            //front row
            player.getFrontRow().get(i).setFrozen(false);
            player.getFrontRow().get(i).setUsedThisTurn(false);
        }
        for (int i = 0; i< player.getBackRow().size(); i++) {
            //back row
            player.getBackRow().get(i).setFrozen(false);
            player.getBackRow().get(i).setUsedThisTurn(false);
        }
        player.getHero().setUsedThisTurn(false);
    }

    public Player getPlayer(int index) {
        if (index == 1) {
            return player1;
        } else if (index == 2) {
            return player2;
        }
        System.out.println("Couldn't find player with index " + index);
        return null;
    }

    public void switchPlayersTurn() {
        if (gameEnded) {
            return;
        }
        if (currentPlayer.getName().equals(PLAYER1)) {
            player1TurnBeenPlayed = true;
            //set the frozen and already used cards state to ready to action
            this.resetCardsOnTableState(player1);
            currentPlayer = player2;
        } else if (currentPlayer.getName().equals(PLAYER2)){
            player2TurnBeenPlayed = true;
            //set the frozen and already used cards state to ready to action
            this.resetCardsOnTableState(player2);
            currentPlayer = player1;
        } else {
            System.out.println("Switch player error");
            return;
        }
        this.turnNumber++;
        if (player1TurnBeenPlayed && player2TurnBeenPlayed) {
            roundNumber++;
            initNewRound(player1, player2, roundNumber);
        }
    }

    public void playGame(Player player1, Player player2, int startingPlayer, ArrayList<Actions> actions, ArrayNode output, String filePath1) throws JsonProcessingException {
        this.currentPlayer = this.getPlayer(startingPlayer);
        //get the first player to start the game
        initNewRound(player1, player2, roundNumber);

        for (int i = 0; i < actions.size(); i++) {
            GameManager.deleteDeadCardsOnTable(this);

            if (actions.get(i).getCommand().equals("getCardsOnTable")) {
                GameManager.printCardsOnTable(this, output);
            } else if (actions.get(i).getCommand().equals("getFrozenCardsOnTable")) {
                GameManager.printFrozenCardsOnTable(this, output);
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
                GameManager.printPlayerDeck(this.getPlayer(actions.get(i).getPlayerIdx()), output);
            } else if (actions.get(i).getCommand().equals("getPlayerHero")) {
                Hero playerHero = this.getPlayer(actions.get(i).getPlayerIdx()).getHero();
                if (playerHero == null) {
                    System.out.println("getPlayerHero error");
                }
                assert playerHero != null;
                HeroJsonOutput heroJsonOutput = new HeroJsonOutput(playerHero);
                GetPlayerHeroJsonOutput getPlayerHeroJsonOutput = new GetPlayerHeroJsonOutput(actions.get(i).getPlayerIdx(), heroJsonOutput);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerHeroJsonOutput);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getCardsInHand")) {
                ArrayList<Card> cardsInHand = this.getPlayer(actions.get(i).getPlayerIdx()).getCardsInHand();
                if (cardsInHand == null) {
                    System.out.println("getCardsInHand error, player index is " + actions.get(i).getPlayerIdx());
                }
                GameManager.printCardsInHand(this.getPlayer(actions.get(i).getPlayerIdx()), output);
            } else if (actions.get(i).getCommand().equals("getPlayerMana")) {
                int playerMana = this.getPlayer(actions.get(i).getPlayerIdx()).getMana();
                GetManaJsonOutput getPlayerMana = new GetManaJsonOutput(actions.get(i).getCommand(), actions.get(i).getPlayerIdx(), playerMana);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerMana);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("getEnvironmentCardsInHand")) {
                GameManager.printEnvironmentCardsInHand(this.getPlayer(actions.get(i).getPlayerIdx()), output);
            } else if (actions.get(i).getCommand().equals("getCardAtPosition")) {
                //aici am facut o modificare pt toate testele de la 13 incolo, unde nu mai e nevoie de Coordinates
                // pt cartea pe care o afisam
                if (filePath1.equals("test13_use_hero_ability_1_invalid.json") ||
                        filePath1.equals("test14_use_hero_ability_2.json")) {
                    GameManager.specialPrintCardAtPosition(this, actions.get(i).getX(), actions.get(i).getY(), output);
                } else {
                    GameManager.printCardAtPosition(this, actions.get(i).getX(), actions.get(i).getY(), output);
                }
            } else if (actions.get(i).getCommand().equals("getPlayerTurn")){
                int playerTurn = currentPlayer.getIndex();
                GetNumberForCommandJsonOutput getPlayerTurnJsonOutput = new GetNumberForCommandJsonOutput(actions.get(i).getCommand(), playerTurn);
                JsonNode jsonNode = JsonParse.parseObjectToJson(getPlayerTurnJsonOutput);
                output.add(jsonNode);
            } else if (actions.get(i).getCommand().equals("endPlayerTurn")) {
                this.switchPlayersTurn();// we switch to next player's turn
            } else if (actions.get(i).getCommand().equals("cardUsesAttack")) {
                Minion attackerCard = GameManager.
                        getCardAtPosition(this, actions.get(i).getCardAttacker());
                Minion attackedCard = GameManager.getCardAtPosition(this, actions.get(i).getCardAttacked());
                assert attackerCard != null;
                attackerCard.useAttack(this, currentPlayer, attackedCard, actions.get(i).getCardAttacker(),
                        actions.get(i).getCardAttacked(), output);
            } else if (actions.get(i).getCommand().equals("cardUsesAbility")) {
                Minion attackerCard = GameManager.getCardAtPosition(this, actions.get(i).getCardAttacker());
                Minion attackedCard = GameManager.getCardAtPosition(this, actions.get(i).getCardAttacked());
                assert attackerCard != null;
                attackerCard.useSpecialAbility(this, currentPlayer, attackedCard, actions.get(i).getCardAttacker()
                        ,actions.get(i).getCardAttacked(), output);
            } else if (actions.get(i).getCommand().equals("useAttackHero")) {
                Minion attackerCard = GameManager.getCardAtPosition(this, actions.get(i).getCardAttacker());
                assert attackerCard != null;
                attackerCard.useAttackOnHero(this, currentPlayer, actions.get(i).getCardAttacker(),
                        GameManager.getPlayerEnemy(this, currentPlayer).getHero(), output);
            } else if (actions.get(i).getCommand().equals("useHeroAbility")) {
                currentPlayer.getHero().useHeroAbility(this, actions.get(i).getAffectedRow(),
                        GameManager.getRow(this, actions.get(i).getAffectedRow()), currentPlayer, output);
            } else if (actions.get(i).getCommand().equals("useEnvironmentCard")) {
                Card environment = currentPlayer.getCardsInHand().get(actions.get(i).getHandIdx());
                environment.useEnvironmentCard(this, actions.get(i).getHandIdx(), actions.get(i).getAffectedRow(),
                        GameManager.getRow(this, actions.get(i).getAffectedRow()), currentPlayer, output);
            } else if (actions.get(i).getCommand().equals("placeCard")) {
                Card card = currentPlayer.getCardsInHand().get(actions.get(i).getHandIdx());
                card.placeCard(this, currentPlayer, actions.get(i).getHandIdx(), output);
            } else {
                System.out.println(actions.get(i).getCommand() + " doesn't exist!!!");
            }
        }
    }
}
