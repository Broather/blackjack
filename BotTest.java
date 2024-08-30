
// TODO: put all the enums in a package and then statically import all of them
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;

import org.junit.Test;

public class BotTest {
    @Test
    public void splitEights() {
        Bot bot = new Bot(BotType.PerfectStrategy);
        // TODO: turn ranks and suits to enums
        Card eight1 = new Card("8", "Spades", false);
        Card eight2 = new Card("8", "Clubs", false);

        Hand me = new Hand();
        me.add(eight1);
        me.add(eight2);

        ArrayList<Card> revealedCards = Card.allRanks("Spades", false);
        Card hiddenTen = new Card("10", "Hearts", true);

        for (Card card : revealedCards) {
            Hand dealer = new Hand();
            dealer.add(card);
            dealer.add(hiddenTen);

            assertEquals(MoveType.Split, bot.resolve.apply(me, dealer));
        }
    }

    @Test
    public void splitAces() {
        Bot bot = new Bot(BotType.PerfectStrategy);
        // TODO: turn ranks and suits to enums
        Card Ace1 = new Card("A", "Spades", false);
        Card Ace2 = new Card("A", "Spades", false);

        Hand me = new Hand();
        me.add(Ace1);
        me.add(Ace2);

        ArrayList<Card> revealedCards = Card.allRanks("Spades", false);
        Card hiddenTen = new Card("10", "Hearts", true);

        for (Card card : revealedCards) {
            Hand dealerHand = new Hand();
            dealerHand.add(card);
            dealerHand.add(hiddenTen);

            assertEquals(MoveType.Split, bot.resolve.apply(me, dealerHand));
        }
    }

    @Test
    public void standOnHard17OrMore() {
        Bot bot = new Bot(BotType.PerfectStrategy);

        Card revealedTen = new Card("10", "Clubs", false);
        Card hiddenTen = new Card("10", "Hearts", true);
        Hand dealer = Hand.of(revealedTen, hiddenTen);

        for (Card first : Card.allRanks("Spades", false)) {
            for (Card second : Card.allRanks("Hearts", false)) {
                Hand me = new Hand();
                me.add(first);
                me.add(second);

                if (!me.isSoft() && me.getValue() >= 17) {
                    assertEquals(MoveType.Stand, bot.resolve.apply(me, dealer));
                }
            }
        }
    }

    @Test
    public void double11() {
        Bot bot = new Bot(BotType.PerfectStrategy);

        Card revealedTen = new Card("10", "Clubs", false);
        Card hiddenTen = new Card("10", "Hearts", true);
        Hand dealer = Hand.of(revealedTen, hiddenTen);

        for (Card first : Card.allRanks("Spades", false)) {
            for (Card second : Card.allRanks("Hearts", false)) {
                Hand me = new Hand();
                me.add(first);
                me.add(second);

                if (me.getValue() == 11) {
                    assertEquals(MoveType.Double, bot.resolve.apply(me, dealer));
                }
            }
        }
    }
}
