/** CardSet class holds a number of cards, contains methods to add,
 * remove, read cards from the set.
 * CardSet.java by Dave G Brown
 */
import java.util.*;

public class CardSet {

    public CardSet() {
        cards = new ArrayList<Card>();
    }

    /** returns the number of cards in CardSet */
    public int numCards() {
        return cards.size();
    }
    
    /** get card at position pos, throws IndexOutOfBounds exception
     * if pos > numCards() - 1 */
    public Card getCard(int pos) {
        return cards.get(pos);
    }
    
    /** adds Card to the end of CardSet, always returns true */
    public boolean addCard(Card c) {
        cards.add(c);
        return true; 
    }

    /** If pos is <= size of CardSet, adds Card to position pos in CardSet
     * and returns true, otherwise returns false */
    public boolean addCard(Card c, int pos) {
        if (pos <= cards.size()) {
            cards.add(pos, c);
            return true;
        } else return false;
    }
    
    /** removes top card from CardSet; throws IndexOutOfBounds exception
     * if CardSet is empty */
    public Card removeCard() {
        return cards.remove(0);
    }

    /** removes card at position pos from CardSet; throws IndexOutOfBounds
     * exception if pos >= numCards() */
    public Card removeCard(int pos) {
        return cards.remove(pos);
    }

    /** giveCards: transfers n cards from the top of this CardSet
     * to the back of CardSet c if there are sufficient cards in
     * this set, otherwise returns false */
    public boolean giveCards(CardSet c, int n) {
        if (this.numCards() < n)
            return false;
        else {
            for (int i=0; i<n; i++)
                c.addCard(cards.remove(0));
            return true;
        }
    }

    /** takeCards: transfers n cards from the top of CardSet c
     * to the back of this CardSet if there are sufficient cards in c,
     * otherwise returns false */
    public boolean takeCards(CardSet c, int n) {
        if (c.numCards() < n)
            return false;
        else {
            for (int i=0; i<n; i++)
                cards.add(c.removeCard());
            return true;
        }
    }

    /* printCards: prints contents of CardSet c as text strings, one
     * per line */
    public static void printCards(CardSet c) {
        for (int i=0; i < c.cards.size(); i++) {
            System.out.println(c.cards.get(i));
        }
    }

    private ArrayList<Card> cards;
}
