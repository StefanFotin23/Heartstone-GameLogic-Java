package JsonOutput;

import Cards.Card;

import java.util.ArrayList;

public class GetFrozenCardsOutput {
    private final String command;
    private final ArrayList<CardJsonOutput> output;

    public GetFrozenCardsOutput(ArrayList<CardJsonOutput> output) {
        this.command = "getFrozenCardsOnTable";
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<CardJsonOutput> getOutput() {
        return output;
    }
}
