package JsonOutput;

public class GetManaJsonOutput {
    private final String command;
    private final int playerIdx;
    private final int output;

    public GetManaJsonOutput(String command, int playerIdx, int number) {
        this.command = command;
        this.output = number;
        this.playerIdx = playerIdx;
    }

    public String getCommand() {
        return command;
    }

    public int getOutput() {
        return output;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }
}
