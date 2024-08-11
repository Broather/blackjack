import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Blackjack
 */
public class Blackjack {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int DECKS_IN_SHOE = 1;

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

        System.out.printf("%s\t%s\n\n",
                dealerHand,
                !shoe.hasPlasticCard() ? String.format("%s#%s <- plastic card", Hand.ANSI_RED, Hand.ANSI_RESET) : "");
        for (int i = 0; i < playerHands.size(); i++) {
            System.out.printf("%s%s\n", i == selected ? "->" : "", playerHands.get(i));
        }
    }

    private static void resolveHand(Shoe shoe, ArrayList<Hand> playerHands, int selected, Hand dealerHand) {
        Hand playerHand = playerHands.get(selected);
        boolean satisfied = playerHand.isBlackjack() || dealerHand.isBlackjack();
        while (!satisfied) {
            printState(playerHands, selected, dealerHand, shoe);
            String action = scanner.next("[hsDS]");
            switch (action) {
                case "h":
                    // hit
                    playerHand.add(shoe.draw(false));
                    satisfied = playerHand.getValue() >= 21;
                    break;
                case "s":
                    // stand
                    satisfied = true;
                    break;
                case "D":
                    // double
                    playerHand.doubleBet();
                    playerHand.add(shoe.draw(true));
                    satisfied = true;
                    break;
                case "S":
                    // split
                    if (!playerHand.isSplittable()) {
                        System.out.println("can't split that");
                    } else {
                        Hand splitHand = playerHand.split();
                        splitHand.add(shoe.draw(false));
                        playerHand.add(shoe.draw(false));

                        playerHands.add(selected + 1, splitHand);

                        satisfied = playerHand.isForcedSatisfied() || playerHand.getValue() >= 21;
                    }
                    break;

                default:
                    System.out.println("unhandled action: " + action);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        Shoe shoe = new Shoe(DECKS_IN_SHOE, rand);

        Player jack = new Player("Jack");
        ArrayList<Hand> initialHands = Hand.of(jack, 20, 15);
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
                resolveHand(shoe, playerHands, i, dealerHand);
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

        Set<Player> participatedPlayers = initialHands.stream().map(Hand::getPlayer).collect(Collectors.toSet());
        participatedPlayers.forEach(System.out::println);

        scanner.close();
    }
}