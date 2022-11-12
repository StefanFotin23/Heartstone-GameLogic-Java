package JsonOutput;

public class GetPlayerHeroJsonOutput {
    private final String command;
    private final int playerIdx;
    private final HeroJsonOutput output;

    public GetPlayerHeroJsonOutput(int playerIdx, HeroJsonOutput hero) {
        this.command = "getPlayerHero";
        this.playerIdx = playerIdx;
        this.output = hero;
    }

    public String getCommand() {
        return command;
    }

    public int getPlayerIdx() {
        return playerIdx;
    }

    public HeroJsonOutput getOutput() {
        return output;
    }
}
