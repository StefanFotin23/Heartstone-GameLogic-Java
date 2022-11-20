package JsonOutput;

import fileio.Coordinates;

public class CardAttackerErrorMessage {
    private final String command;
    private final Coordinates cardAttacker;
    private final Coordinates cardAttacked;
    private final String error;

    public CardAttackerErrorMessage(String command, Coordinates cardAttacker, Coordinates cardAttacked, String error) {
        this.command = command;
        this.cardAttacker = cardAttacker;
        this.cardAttacked = cardAttacked;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public Coordinates getCardAttacked() {
        return cardAttacked;
    }

    public String getError() {
        return error;
    }
}
