
// TODO: put all the enums in a package and then statically import all of them
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BotTest {
    @Test
    public void SplitAces() {
        Bot bot = new Bot(BotType.PerfectStrategy);
        // TODO: turn ranks and suits to enums
        Card Ace = new Card("A", "Spades", false);
        Card two = new Card("2", "Hearts", false);
        Card hiddenTen = new Card("10", "Hearts", true);

        Hand myHand = new Hand();
        myHand.add(Ace);
        myHand.add(Ace);

        Hand dealerHand = new Hand();
        dealerHand.add(two);
        dealerHand.add(hiddenTen);

        assertEquals(MoveType.Split, bot.resolve.apply(myHand, dealerHand));
    }
}
