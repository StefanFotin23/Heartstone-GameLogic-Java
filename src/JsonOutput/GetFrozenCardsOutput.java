package JsonOutput;

import Cards.Card;

import java.util.ArrayList;

public class GetFrozenCardsOutput {
    private final String command;
    private final ArrayList<EnvironmentCardJsonOutput> frozenCards;

    public GetFrozenCardsOutput(String command, ArrayList<EnvironmentCardJsonOutput> frozenCards) {
        this.command = command;
        this.frozenCards = frozenCards;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<EnvironmentCardJsonOutput> getFrozenCards() {
        return frozenCards;
    }
}
