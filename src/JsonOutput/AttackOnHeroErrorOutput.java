package JsonOutput;

import fileio.Coordinates;

public class AttackOnHeroErrorOutput {
    private final String command;
    private final Coordinates cardAttacker;
    private final String error;

    public AttackOnHeroErrorOutput(Coordinates cardAttacker, String error) {
        this.command = "useAttackHero";
        this.cardAttacker = cardAttacker;
        this.error = error;
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCardAttacker() {
        return cardAttacker;
    }

    public String getError() {
        return error;
    }
}
