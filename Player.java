import java.util.Map;

/**
 * Player
 */
public class Player extends Owner {
    public Player(String name) {
        super.name = name;
    }

    public MoveType parseInput(String input) {
        Map<String, MoveType> moveMap = Map.of(
                "h", MoveType.Hit,
                "s", MoveType.Stand,
                "D", MoveType.Double,
                "S", MoveType.Split);

        return moveMap.getOrDefault(input, MoveType.Unrecognised);
    }
}