package JsonOutput;

public class GetNumberForCommandJsonOutput {
    private final String command;
    private final int output;

    public GetNumberForCommandJsonOutput(String command, int number) {
        this.command = command;
        this.output = number;
    }

    public String getCommand() {
        return command;
    }

    public int getOutput() {
        return output;
    }
}
