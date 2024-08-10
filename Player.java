/**
 * Player
 */
public class Player {
    private static final String ANSI_GREEN = "\u001b[32m";
    private static final String ANSI_RED = "\u001b[31m";
    private static final String ANSI_RESET = "\u001b[0m";

    private String name;
    private float runningTotal = 0.0f;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s's total: (%s%+.2f%s)", name, runningTotal > 0 ? ANSI_GREEN : ANSI_RED, runningTotal,
                ANSI_RESET);
    }

    public String getName() {
        return name;
    }

    public void addToTotal(float a) {
        runningTotal += a;
    }
}