import java.util.Map;
import java.util.Scanner;

/**
 * Player
 */
public class Player extends Owner {
    public Player(String name) {
        super.name = name;
    }

    public MoveType parseInput(Scanner scanner) {
        Map<String, MoveType> moveMap = Map.of(
                "h", MoveType.Hit,
                "s", MoveType.Stand,
                "D", MoveType.Double);

        String input = scanner.nextLine();
        while (true) {
            if (moveMap.containsKey(input)) {
                return moveMap.get(input);
            } else {
                System.out.printf("unrecognised input: %s\n", input);
                input = scanner.nextLine();
            }
        }
    }
}