import java.util.Map;

public class Card {
    private static final String ANSI_BG_WHITE = "\u001b[47m";
    private static final String ANSI_BG_RED = "\u001b[41m";
    private static final String ANSI_RESET = "\u001b[0m";

    private static final Map<String, Integer> valueMap = Map.ofEntries(
            Map.entry("A", 11),
            Map.entry("2", 2),
            Map.entry("3", 3),
            Map.entry("4", 4),
            Map.entry("5", 5),
            Map.entry("6", 6),
            Map.entry("7", 7),
            Map.entry("8", 8),
            Map.entry("9", 9),
            Map.entry("10", 10),
            Map.entry("J", 10),
            Map.entry("Q", 10),
            Map.entry("K", 10));

    private String rank;
    public int value;
    private String suit;
    private boolean isFaceDown;

    public Card() {
        rank = "plastic card";
        suit = "bright colored";
    }

    public Card(String rank, String suit, boolean isFaceDown) {
        this.rank = rank;
        this.value = valueMap.get(rank);
        this.suit = suit;
        this.isFaceDown = isFaceDown;
    }

    public boolean equals(Card that) {
        return this.value == that.getValue();
    }

    public String getRank() {
        return rank;
    }

    public boolean isAce() {
        return rank.equals("A");
    }

    public boolean isFaceDown() {
        return isFaceDown;
    }

    public boolean isFaceUp() {
        return !isFaceDown;
    }

    public boolean isPlastic() {
        return rank == "plastic card" &&
                suit == "bright colored";
    }

    public String toString() {
        if (isFaceDown) {
            return "?? of ??";
        }
        return String.format("%s of %s%s%s", rank, suit == "Hearts" || suit == "Diamonds" ? ANSI_BG_RED : ANSI_BG_WHITE,
                suit, ANSI_RESET);
    }

    public int getValue() {
        return value;
    }

    /*
     * drops the value of an Ace from 11 to 1
     * does nothing with other cards
     */
    public int harden() {
        if (isAce() && value == 11) {
            value = 1;
        }
        return value;
    }

    public void setFaceDown(boolean b) {
        isFaceDown = b;
    }
}
