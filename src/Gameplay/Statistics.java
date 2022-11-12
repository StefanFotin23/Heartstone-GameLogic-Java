package Gameplay;

public class Statistics {
    private int gameNumber;
    private int playerOneWins;
    private int playerTwoWins;

    private static Statistics instance = null;

    private Statistics() {}

    public static Statistics getInstance() {
        if (instance == null) {
            instance = new Statistics();
        }
        return instance;
    }

    public void addPlayerOneWin() {
        setPlayerOneWins(getPlayerOneWins() + 1);
    }

    public void addPlayerTwoWin() {
        setPlayerTwoWins(getPlayerTwoWins() + 1);
    }

    public void addGame() {
        setGameNumber(getGameNumber() + 1);
    }

    public int getGameNumber() {
        return gameNumber;
    }

    private void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    private void setPlayerOneWins(int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    private void setPlayerTwoWins(int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }
}
