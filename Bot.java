import java.util.Set;
import java.util.function.BiFunction;

/**
 * Bot
 */
public class Bot extends Owner {
    /*
     * resolve my hand given dealer hand
     */
    public BiFunction<Hand, Hand, MoveType> resolve;

    public Bot(BotType type) {
        super.name = "Bot";

        final MoveType hit = MoveType.Hit;
        final MoveType stand = MoveType.Stand;
        final MoveType doubleDown = MoveType.Double;
        final MoveType split = MoveType.Split;

        if (type == BotType.PerfectStrategy) {
            resolve = (Hand me, Hand dealer) -> {
                final int myValue = me.getValue();
                final int dealerValue = dealer.getValue();
                if (me.isSplittable()) {
                    if (myValue == 12 || myValue == 16) {
                        return split;
                    } else if (myValue == 18) {
                        if (!Set.of(7, 10, 11).contains(dealerValue)) {
                            return split;
                        }
                    } else if (Set.of(14, 6, 4).contains(myValue) && dealerValue <= 7) {
                        return split;
                    } else if (myValue == 12 && dealerValue <= 6) {
                        return split;
                    } else if (myValue == 8 && dealerValue == 5 || dealerValue == 6) {
                        return split;
                    }
                }
                if (me.isSoft()) {
                    if (myValue == 19) {
                        if (dealerValue == 6) {
                            return me.isDoubleable() ? doubleDown : hit;
                        } else {
                            return stand;
                        }
                    } else if (myValue == 18) {
                        if (Set.of(2, 3, 4, 5, 6).contains(dealerValue)) {
                            return me.isDoubleable() ? doubleDown : hit;
                        } else if (Set.of(7, 8).contains(dealerValue)) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (myValue == 17) {
                        if (Set.of(3, 4, 5, 6).contains(dealerValue)) {
                            return me.isDoubleable() ? doubleDown : hit;
                        } else {
                            return hit;
                        }
                    } else if (myValue == 16 || myValue == 15) {
                        if (Set.of(4, 5, 6).contains(dealerValue)) {
                            return me.isDoubleable() ? doubleDown : hit;
                        } else {
                            return hit;
                        }
                    } else if (myValue == 14 || myValue == 13) {
                        if (Set.of(5, 6).contains(dealerValue)) {
                            return me.isDoubleable() ? doubleDown : hit;
                        } else {
                            return hit;
                        }
                    } else {
                        return stand;
                    }
                } else {
                    if (myValue >= 17) {
                        return stand;
                    } else if (myValue <= 16 && myValue >= 13) {
                        if (dealerValue <= 6) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (myValue == 12) {
                        if (Set.of(4, 5, 6).contains(dealerValue)) {
                            return stand;
                        } else {
                            return hit;
                        }
                    } else if (myValue == 11) {
                        return me.isDoubleable() ? doubleDown : hit;
                    } else if (myValue == 10) {
                        if (Set.of(10, 11).contains(dealerValue)) {
                            return hit;
                        } else {
                            return me.isDoubleable() ? doubleDown : hit;
                        }
                    } else if (myValue == 9) {
                        if (Set.of(3, 4, 5, 6).contains(dealerValue)) {
                            return me.isDoubleable() ? doubleDown : hit;
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