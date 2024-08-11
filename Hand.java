import java.util.ArrayList;
import java.util.Stack;

public class Hand {
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_RESET = "\u001b[0m";

    private Stack<Card> cards = new Stack<>();
    private Player player;
    private float baseBet = 0.0f;
    private float payoutBet = 0.0f;
    private boolean forcedSatisfaction = false;

    public static ArrayList<Hand> of(Player player, float... bets) {
        ArrayList<Hand> hands = new ArrayList<>(bets.length);
        for (int i = 0; i < bets.length; i++) {
            hands.add(i, new Hand(player, bets[i]));
        }
        return hands;
    }

    public Hand() {
        this.player = new Player("dealer");
    }

    public Hand(Player player, float bet) {
        this.player = player;
        this.baseBet = bet;
    }

    public Hand(Hand hand) {
        this.player = hand.player;
        this.baseBet = hand.baseBet;
    }

    @Override
    public String toString() {
        // need to call getValue before isSoft
        // because it updates Aces' value if hand exceeds 21
        // can't call it in isSoft because getValue depends on it
        int value = getValue();

        if (baseBet == 0.0f) {
            return String.format("%s's hand: %s (%s%d)", player.getName(), cards, isSoft() ? "*" : "", value);
        }
        if (payoutBet == 0.0f) {
            return String.format("(%.2f) %s's hand: %s (%s%d)", baseBet, player.getName(), cards, isSoft() ? "*" : "",
                    value);
        }
        return String.format("(%s%+.2f%s) %s's hand: %s (%s%d)", payoutBet > 0.0f ? ANSI_GREEN : ANSI_RED,
                payoutBet,
                ANSI_RESET,
                player.getName(),
                cards,
                isSoft() ? "*" : "",
                value);
    }

    private int calculateValue() {
        return cards.stream()
                .filter(Card::isFaceUp)
                .mapToInt(Card::getValue)
                .sum();
    }

    public int getValue() {
        int value = calculateValue();
        if (value <= 21) {
            return value;
        } else if (!isSoft()) {
            return value;
        } else {
            for (Card c : cards) {
                c.harden();
                value = calculateValue();
                if (value <= 21) {
                    return value;
                }
            }
            return value;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void doubleBet() {
        baseBet *= 2;
    }

    public boolean isBusted() {
        return getValue() > 21;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && cards.stream()
                .mapToInt(Card::getValue)
                .sum() == 21;
    }

    public boolean isSplittable() {
        return cards.size() == 2 && cards.get(0).equals(cards.get(1));
    }

    /*
     * true if hand has 11 value Ace
     */
    public boolean isSoft() {
        return cards.stream().anyMatch(c -> c.isFaceUp() && c.isAce() && c.getValue() == 11);
    }

    public boolean isForcedSatisfied() {
        return forcedSatisfaction;
    }

    public void evaluate(Hand dealerHand) {
        payoutBet = baseBet;
        if (this.isBusted()) {
            payoutBet *= -1;
        } else if (dealerHand.isBusted()) {
            if (this.isBlackjack()) {
                this.payoutBet *= 1.5f;
            }
        } else if (dealerHand.getValue() > this.getValue()) {
            payoutBet *= -1;
        } else if (this.getValue() > dealerHand.getValue()) {
            if (this.isBlackjack() && !dealerHand.isBlackjack()) {
                payoutBet *= 1.5f;
            }
        } else if (this.isBlackjack() && !dealerHand.isBlackjack()) {
            payoutBet *= 1.5f;
        } else {
            payoutBet = 0.0f;
        }

        player.addToTotal(payoutBet);
    }

    public Hand reveal() {
        for (Card card : cards) {
            card.setFaceDown(false);
        }
        return this;
    }

    public Hand split() {
        assert this.isSplittable() : "attempted to split unsplittable hand";

        Hand splitHand = new Hand(player, baseBet);
        if (cards.stream().allMatch(Card::isAce)) {
            forcedSatisfaction = true;
            splitHand.forcedSatisfaction = true;
        }

        splitHand.add(this.cards.pop());

        return splitHand;
    }

    public void add(Card c) {
        cards.push(c);
    }
}
