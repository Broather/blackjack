import java.util.Set;
import java.util.function.BiFunction;

/**
 * Bot
 */
public class Bot extends Owner {
    public BiFunction<Hand, Hand, MoveType> resolve;

    public Bot(BotType type) {
        super.name = "Bot";
        if (type == BotType.PerfectStrategy) {

            resolve = (Hand dealer, Hand me) -> {
                if (me.getValue() >= 17) {
                    return MoveType.Stand;
                } else if (me.getValue() <= 16 && me.getValue() >= 13) {
                    if (dealer.getValue() <= 6) {
                        return MoveType.Stand;
                    } else {
                        return MoveType.Hit;
                    }
                } else if (me.getValue() == 12) {
                    if (Set.of(4, 5, 6).contains(dealer.getValue())) {
                        return MoveType.Stand;
                    } else {
                        return MoveType.Hit;
                    }
                } else if (me.getValue() == 11) {
                    return MoveType.Double;
                } else if (me.getValue() == 10) {
                    if (Set.of(10, 11).contains(dealer.getValue())) {
                        return MoveType.Hit;
                    } else {
                        return MoveType.Double;
                    }
                } else if (me.getValue() == 9) {
                    if (Set.of(3, 4, 5, 6).contains(dealer.getValue())) {
                        return MoveType.Double;
                    } else {
                        return MoveType.Hit;
                    }
                } else {
                    return MoveType.Hit;
                }
            };
        }
    }
}