import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Shoe {
    private Stack<Card> cards = new Stack<>();
    private Stack<Card> discard = new Stack<>();

    public Shoe(int amountOfDecks, Random random) {
        List<String> symbols = List.of("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
        List<String> suits = List.of("Hearts", "Diamonds", "Clubs", "Spades");
        for (int i = 0; i < amountOfDecks; i++) {
            for (String symbol : symbols) {
                for (String suit : suits) {
                    cards.add(new Card(symbol, suit, false));
                }
            }
        }
        Collections.shuffle(cards, random);
        int plasticCardIndex = random.nextInt(cards.size() / 8, cards.size() / 2);
        cards.add(plasticCardIndex, new Card());
    }

    public boolean hasPlasticCard() {
        return cards.stream().anyMatch(Card::isPlastic);
    }

    public Card draw(boolean drawFaceDown) {
        // when cards run out, they get reshuffled from the dicard pile
        if (cards.isEmpty()) {
            Collections.shuffle(discard);
            cards = discard;
            discard = new Stack<>();
        }
        Card c = cards.pop();
        if (c.isPlastic()) {
            System.out.printf("dealer drew plastic card (%s cards remain)\n", cards.size());
            c = cards.pop();
        }
        discard.add(c);

        c.setFaceDown(drawFaceDown);
        return c;
    }

    public int size() {
        return cards.size();
    }

}
