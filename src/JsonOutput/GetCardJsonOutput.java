package JsonOutput;

import Cards.Card;

public class GetCardJsonOutput {
    private final String command;
    private final int x;
    private final int y;
    private final CardJsonOutput output;

    public GetCardJsonOutput(String command, int x, int y, Card card) {
        this.command = command;
        this.x = x;
        this.y = y;
        output = new CardJsonOutput(card);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCommand() {
        return command;
    }

    public CardJsonOutput getOutput() {
        return output;
    }
}
