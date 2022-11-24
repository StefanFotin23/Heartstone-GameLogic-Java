package JsonOutput;

import Cards.Card;

public class GetCardSpecialJsonOutput {
    private final String command;
    private final CardJsonOutput output;

    public GetCardSpecialJsonOutput(String command, Card card) {
        this.command = command;
        output = new CardJsonOutput(card);
    }

    public String getCommand() {
        return command;
    }

    public CardJsonOutput getOutput() {
        return output;
    }
}
