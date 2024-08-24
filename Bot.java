import java.util.function.BiFunction;

/**
 * Bot
 */
public class Bot extends Owner {
    public BiFunction<Integer, Integer, MoveType> resolve;

    public Bot(BotType type) {
        super.name = "Bot";
        if (type == BotType.PerfectStrategy) {
            resolve = (Integer dealerScore, Integer myScore) -> {
                return MoveType.Hit;
            };
        }
    }
}