package JsonOutput;

import static Constants.Constants.*;

public class HeartHoundErrorMessage {
    private final int affectedRow;
    private final String command;
    private final String error;
    private final int handIdx;

    public HeartHoundErrorMessage(int affectedRow, String command, String error, int handIdx) {
        this.affectedRow = affectedRow;
        this.command = command;
        this.error = CANT_STEAL_ENEMY_CARD;
        this.handIdx = handIdx;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getCommand() {
        return command;
    }

    public String getError() {
        return error;
    }

    public int getHandIdx() {
        return handIdx;
    }
}
