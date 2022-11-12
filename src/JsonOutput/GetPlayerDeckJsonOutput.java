package JsonOutput;

import java.util.ArrayList;

public class GetPlayerDeckJsonOutput {
    private String command;
    private int playerIdx;
    private ArrayList<EnvironmentCardJsonOutput> output;

    public GetPlayerDeckJsonOutput(int playerIdx, ArrayList<EnvironmentCardJsonOutput> output) {
        this.command = "getPlayerDeck";
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
