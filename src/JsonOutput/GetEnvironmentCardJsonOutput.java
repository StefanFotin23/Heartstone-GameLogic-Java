package JsonOutput;

import Cards.Card;

public class GetEnvironmentCardJsonOutput {
    private final String command;
    private final int x;
    private final int y;
    private final EnvironmentCardJsonOutput output;

    public GetEnvironmentCardJsonOutput(String command, int x, int y, Card card) {
        this.command = command;
        this.x = x;
        this.y = y;
        output = new EnvironmentCardJsonOutput(card);
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

    public EnvironmentCardJsonOutput getOutput() {
        return output;
    }
}
