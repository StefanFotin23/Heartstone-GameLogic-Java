package JsonOutput;

public class GetCardErrorStringOutput {
    private final String command;
    private final int x;
    private final int y;
    private final String output;

    public GetCardErrorStringOutput(String command, int x, int y, String errorString) {
        this.command = command;
        this.x = x;
        this.y = y;
        output = errorString;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCommand() {
        return command;
    }

    public String getOutput() {
        return output;
    }
}
