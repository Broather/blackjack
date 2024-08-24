import java.util.Set;
import java.util.function.BiFunction;

/**
 * Bot
 */
public class Bot extends Owner {
    public BiFunction<Hand, Hand, MoveType> resolve;

    public Bot(BotType type) {
        final MoveType hit = MoveType.Hit;
        final MoveType stand = MoveType.Stand;
        final MoveType doubleDown = MoveType.Double;

        super.name = "Bot";
        if (type == BotType.PerfectStrategy) {

            resolve = (Hand dealer, Hand me) -> {
                if (me.isSoft()) {
                    if (me.getValue() == 19) {
                        if (dealer.getValue() == 6) {
                            return doubleDown;
                        } else {
                            return stand;
                        }
                    } else if (me.getValue() == 18) {
                        if (Set.of(2, 3, 4, 5, 6).contains(dealer.getValue())) {
                            return doubleDown;
                        } else if (Set.of(7, 8).contains(dealer.getValue())) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (me.getValue() == 17) {
                        if (Set.of(3, 4, 5, 6).contains(dealer.getValue())) {
                            return doubleDown;
                        } else {
                            return hit;
                        }
                    } else if (me.getValue() == 16 || me.getValue() == 15) {
                        if (Set.of(4, 5, 6).contains(dealer.getValue())) {
                            return doubleDown;
                        } else {
                            return hit;
                        }
                    } else if (me.getValue() == 14 || me.getValue() == 13) {
                        if (Set.of(5, 6).contains(dealer.getValue())) {
                            return doubleDown;
                        } else {
                            return hit;
                        }
                    } else {
                        return stand;
                    }
                } else {
                    if (me.getValue() >= 17) {
                        return stand;
                    } else if (me.getValue() <= 16 && me.getValue() >= 13) {
                        if (dealer.getValue() <= 6) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (me.getValue() == 12) {
                        if (Set.of(4, 5, 6).contains(dealer.getValue())) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (me.getValue() == 11) {
                        return doubleDown;
                    } else if (me.getValue() == 10) {
                        if (Set.of(10, 11).contains(dealer.getValue())) {
                            return hit;
                        } else {
                            return doubleDown;
                        }
                    } else if (me.getValue() == 9) {
                        if (Set.of(3, 4, 5, 6).contains(dealer.getValue())) {
                            return doubleDown;
                        } else {
                            return hit;
                        }
                    } else {
                        return hit;
                    }
                }
            };
        }
    }
}