package JsonOutput;

public class ErrorStringJsonOutput {
    private final String command;
    private final String output;

    public ErrorStringJsonOutput(String command, String output) {
        this.command = command;
        this.output = output;
    }

    public String getCommand() {
        return command;
    }

    public String getOutput() {
        return output;
    }
}
