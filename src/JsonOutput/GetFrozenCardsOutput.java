package JsonOutput;

import Cards.Card;

import java.util.ArrayList;

public class GetFrozenCardsOutput {
    private final String command;
    private final ArrayList<EnvironmentCardJsonOutput> output;

    public GetFrozenCardsOutput(String command, ArrayList<EnvironmentCardJsonOutput> frozenCards) {
        this.command = command;
        this.output = frozenCards;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<EnvironmentCardJsonOutput> getFrozenCards() {
        return output;
    }
}
