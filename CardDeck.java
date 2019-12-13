import java.util.*;

/** CardDeck is a CardSet with a constructor to create a standard shuffled
 * 52 card deck of playing cards, and a method to shuffle the deck.
 * David G Brown */
public class CardDeck extends CardSet {
    
    public static final int DECK_SIZE = 52;

    public CardDeck() {
        int j;
        for (j=0; j<13; j++)
            super.addCard(new Card(j+1, Card.CLUBS));
        for (j=13; j<26; j++)
            super.addCard(new Card(j-12, Card.DIAMONDS));
        for (j=26; j<39; j++)
            super.addCard(new Card(j-25, Card.HEARTS));
        for (j=39; j<52; j++)
            super.addCard(new Card(j-38, Card.SPADES));
        shuffle();
    }

    /* shuffle() generates a random number between 0 and n, exchanges
     * the element at that position with the element at n, decrements n,
     * and repeats until deck is shuffled */
    public void shuffle() {
        int n = DECK_SIZE;
        ArrayList<Card> temp = new ArrayList<Card>();
        for (int i=0; i<DECK_SIZE; i++) {
            int r = (int) (Math.random() * n);
            temp.add(i, super.removeCard(r));
            n--;
        }
        for(Card c: temp)
            super.addCard(c);
    }
}
                
