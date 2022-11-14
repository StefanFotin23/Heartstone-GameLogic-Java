package JsonOutput;

public class PlaceCardErrorOutput {
    private final String command;
    private final int handIdx;
    private final String error;

    public PlaceCardErrorOutput(String command, int handIdx, String error) {
        this.command = command;
        this.handIdx = handIdx;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public String getError() {
        return error;
    }
}
