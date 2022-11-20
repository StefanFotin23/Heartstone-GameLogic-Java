package JsonOutput;

public class HeroErrorMessage {
    private final String command;
    private final int affectedRow;
    private final String error;

    public HeroErrorMessage(String command, int affectedRow, String error) {
        this.command = command;
        this.affectedRow = affectedRow;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public int getAffectedRow() {
        return affectedRow;
    }

    public String getError() {
        return error;
    }
}
