package JsonOutput;

import java.util.ArrayList;

public class GetArrayOfCardsJsonOutput {
    private String command;
    private int playerIdx;
    private ArrayList<EnvironmentCardJsonOutput> output;

    public GetArrayOfCardsJsonOutput(String command, int playerIdx, ArrayList<EnvironmentCardJsonOutput> output) {
        this.command = command;
        this.playerIdx = playerIdx;
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public ArrayList<EnvironmentCardJsonOutput> getOutput() {
        return output;
    }
}
