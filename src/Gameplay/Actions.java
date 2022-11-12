package Gameplay;

import fileio.ActionsInput;
import fileio.Coordinates;

public class Actions {
    private final String command;
    private final int handIdx;
    private final Coordinates cardAttacker;
    private final Coordinates cardAttacked;
    private final int affectedRow;
    private final int playerIdx;
    private final int x;
    private final int y;

    public Actions(String command, int handIdx, Coordinates cardAttacker, Coordinates cardAttacked, int affectedRow, int playerIdx, int x, int y) {
        this.command = command;
        this.handIdx = handIdx;
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.affectedRow = affectedRow;
        this.playerIdx = playerIdx;
        this.x = x;
        this.y = y;
    }

    public Actions(ActionsInput obj) {
        this.command = obj.getCommand();
        this.handIdx = obj.getHandIdx();
        this.cardAttacker = obj.getCardAttacker();
        this.cardAttacked = obj.getCardAttacked();
        this.affectedRow = obj.getAffectedRow();
        this.playerIdx = obj.getPlayerIdx();
        this.x = obj.getX();
        this.y = obj.getY();
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Actions{" +
                "command='" + command + '\'' +
                ", handIdx=" + handIdx +
                ", cardAttacker=" + cardAttacker +
                ", cardAttacked=" + cardAttacked +
                ", affectedRow=" + affectedRow +
                ", playerIdx=" + playerIdx +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
