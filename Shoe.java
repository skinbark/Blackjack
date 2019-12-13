import java.util.*;

/** Shoe is a CardSet with a method to shuffle its contents.
 * David G Brown */
public class Shoe extends CardSet {
    
    /* shuffle() generates a random number between 0 and n, exchanges
     * the element at that position with the element at n, decrements n,
     * and repeats until deck is shuffled */
    public void shuffle() {
        int n = super.numCards();
        ArrayList<Card> temp = new ArrayList<Card>();
        for (int i=0; i<super.numCards(); i++) {
            int r = (int) (Math.random() * n);
            temp.add(i, super.removeCard(r));
            n--;
        }
        for(Card c: temp)
            super.addCard(c);
    }
}
                
