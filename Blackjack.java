import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Blackjack
 */
public class Blackjack {
    private static final int DECKS_IN_SHOE = 4;

    private static void firstDraw(Shoe shoe, ArrayList<Hand> playerHands, Hand dealerHand) {
        for (Hand player : playerHands) {
            player.add(shoe.draw(false));
        }
        dealerHand.add(shoe.draw(false));
        for (Hand player : playerHands) {
            player.add(shoe.draw(false));
        }
        dealerHand.add(shoe.draw(true));
    }

    private static void printState(ArrayList<Hand> playerHands, int selected, Hand dealerHand, Shoe shoe) {
        // set cursor home (0, 0) and erase everything below
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();

        Set<Owner> participatingPlayers = playerHands.stream().map(Hand::getOwner).collect(Collectors.toSet());
        participatingPlayers.forEach(System.out::println);

        System.out.printf("\n%s\t%s%s\n\n",
                dealerHand,
                !shoe.hasPlasticCard() ? Hand.ANSI_RED_FG + "#" + Hand.ANSI_RESET_ALL : "",
                !shoe.hasPlasticCard() ? " <- plastic card" : "");
        for (int i = 0; i < playerHands.size(); i++) {
            System.out.printf("%s%s%s\n", i == selected ? "->" + Hand.ANSI_BOLD : "", playerHands.get(i),
                    Hand.ANSI_RESET_ALL);
        }
    }

    private static void resolveHand(Shoe shoe, ArrayList<Hand> playerHands, int selected, Hand dealerHand,
            Scanner scanner) {
        Hand selectedHand = playerHands.get(selected);
        boolean satisfied = selectedHand.isBlackjack() || dealerHand.isBlackjack();
        printState(playerHands, selected, dealerHand, shoe);
        while (!satisfied) {
            MoveType action = selectedHand.resolve(dealerHand, scanner);
            switch (action) {
                case Hit:
                    // hit
                    selectedHand.add(shoe.draw(false));
                    printState(playerHands, selected, dealerHand, shoe);
                    satisfied = selectedHand.getValue() >= 21;
                    break;
                case Stand:
                    // stand
                    satisfied = true;
                    break;
                case Double:
                    // double
                    selectedHand.doubleBet();
                    selectedHand.add(shoe.draw(true));
                    printState(playerHands, selected, dealerHand, shoe);
                    satisfied = true;
                    break;
                case Split:
                    // split
                    if (!selectedHand.isSplittable()) {
                        System.out.println("can't split that");
                    } else {
                        Hand splitHand = selectedHand.split();
                        splitHand.add(shoe.draw(false));
                        selectedHand.add(shoe.draw(false));

                        playerHands.add(selected + 1, splitHand);

                        printState(playerHands, selected, dealerHand, shoe);
                        satisfied = selectedHand.isForcedSatisfied() || selectedHand.getValue() >= 21;
                    }
                    break;

                default:
                    System.out.println("unrecognised action: " + action);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final Random rand = new Random();
        final Shoe shoe = new Shoe(DECKS_IN_SHOE, rand);

        Player jack = new Player("Jack");
        Bot bot = new Bot(BotType.PerfectStrategy);

        ArrayList<Hand> initialHands = new ArrayList<>();
        initialHands.add(new Hand(jack, 10));
        initialHands.add(new Hand(bot, 10));

        while (shoe.hasPlasticCard()) {
            Hand dealerHand = new Hand();
            // deep copy of initialHands
            ArrayList<Hand> playerHands = new ArrayList<>();
            for (Hand h : initialHands) {
                playerHands.add(new Hand(h));
            }
            firstDraw(shoe, playerHands, dealerHand);

            // TODO: offer insurance if dealer has Ace

            for (int i = 0; i < playerHands.size(); i++) {
                resolveHand(shoe, playerHands, i, dealerHand, scanner);
            }

            printState(playerHands, -1, dealerHand.reveal(), shoe);
            while (dealerHand.getValue() < 17) {
                dealerHand.add(shoe.draw(false));
                printState(playerHands, -1, dealerHand, shoe);
            }

            for (Hand playerHand : playerHands) {
                playerHand.reveal();
                playerHand.evaluate(dealerHand);
            }
            printState(playerHands, -1, dealerHand, shoe);
            scanner.nextLine();
        }

        scanner.close();
    }
}