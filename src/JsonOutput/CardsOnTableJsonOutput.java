package JsonOutput;

import Cards.Card;

import java.util.ArrayList;

public class CardsOnTableJsonOutput {
    private final String command;
    private final ArrayList<ArrayList<EnvironmentCardJsonOutput>> output;

    public CardsOnTableJsonOutput(String command, ArrayList<ArrayList<EnvironmentCardJsonOutput>> output) {
        this.command = command;
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<ArrayList<EnvironmentCardJsonOutput>> getOutput() {
        return output;
    }
}
