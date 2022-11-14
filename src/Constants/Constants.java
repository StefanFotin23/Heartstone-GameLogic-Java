package Constants;

public class Constants {
    public static String HERO = "Hero";
    public static String MINION = "Minion";
    public static String ENVIRONMENT = "Environment";
    public static String TANK = "Tank";
    public static int NUMBER_OF_CARDS_PER_ROW = 5;
    public static int PLAYER_ONE_BACK_ROW_INDEX = 3;
    public static int PLAYER_ONE_FRONT_ROW_INDEX = 2;
    public static int PLAYER_TWO_FRONT_ROW_INDEX = 1;
    public static int PLAYER_TWO_BACK_ROW_INDEX = 0;
    public static String NOT_PLACE_ENVIRONMENT_MESSAGE = "Cannot place environment card on table.";
    public static String NOT_ENOUGH_MANA_MESSAGE = "Not enough mana to place card on table.";
    public static String FULL_ROW_MESSAGE = "Cannot place card on table since row is full.";
    public static String ATTACKER_CARD_FROZEN = "Attacker card is frozen.";
    public static String ATTACKER_CARD_USED = "Attacker card has already attacked this turn.";
    public static String ATTACKED_CARD_NONEXISTENT = "Attacked card does not belong to the enemy.";
    public static String ATTACKED_CARD_NOT_ALLY = "Attacked card does not belong to the current player.";
    public static String ATTACKED_CARD_NOT_TANK = "Attacked card is not of type 'Tank’.";
    public static String NO_CARD_THERE = "No card at that position.";
    public static String PLAYER1 = "player1";
    public static String PLAYER2 = "player2";
    public static int HERO_HEALTH_DEFAULT_VALUE = 30;
    public static String NOT_ENVIRONMENT_MESSAGE = "Chosen card is not of type environment.";
    public static String NOT_MANA_ENVIRONMENT_MESSAGE = "Not enough mana to use environment card.";
    public static String CHOSEN_ROW_NOT_ENEMY = "Chosen row does not belong to the enemy.";
    public static String CANT_STEAL_ENEMY_CARD = "Cannot steal enemy card since the player's row is full.";
}
