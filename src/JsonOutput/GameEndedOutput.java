package JsonOutput;

import Gameplay.Player;
import static Constants.Constants.*;

public class GameEndedOutput {
    private final String gameEnded;

    public GameEndedOutput(Player player) {
        String playerNo;
        if (player.getName().equals(PLAYER1)) {
            playerNo = "one";
        } else if (player.getName().equals(PLAYER2)) {
            playerNo = "two";
        } else {
            System.out.println("Game ended player name = null");
            playerNo = "null";
        }
        this.gameEnded = "Player " + playerNo + " killed the enemy hero.";
    }

    public String getGameEnded() {
        return gameEnded;
    }
}
