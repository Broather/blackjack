/**
 * Owner
 */
public class Owner {
    private static final String ANSI_GREEN = "\u001b[32m";
    private static final String ANSI_RED = "\u001b[31m";
    private static final String ANSI_RESET = "\u001b[0m";

    protected String name;
    protected float runningTotal = 0.0f;

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