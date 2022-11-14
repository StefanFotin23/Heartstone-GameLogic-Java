package JsonOutput;

public class EnvironmentErrorOutput {
    private final String command;
    private final int handIdx;
    private final int affectedRow;
    private final String error;

    public EnvironmentErrorOutput(String command, int handIdx, int affectedRow, String error) {
        this.command = command;
        this.handIdx = handIdx;
        this.affectedRow = affectedRow;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public int getHandIdx() {
        return handIdx;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getError() {
        return error;
    }
}
