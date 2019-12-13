/** Card.java - a class to store a single playing card
 * by Dave G Brown
 */
public class Card {
    public Card(int aValue,int aSuit) {
        value = aValue;
        suit = aSuit;
    }

    public int getValue() {
        return value;
    }

    public int getSuit() {
        return suit;
    }

    /** toString() returns the suit and value of a card as a text string */
    @Override
    public String toString() {
        String valString = "";
        String suitString = "";
        switch (value)
        {
            case Card.ACE: valString = "Ace"; break;
            case Card.JACK: valString = "Jack"; break;
            case Card.QUEEN: valString = "Queen"; break;
            case Card.KING: valString = "King"; break;
            default: valString = "" + value; break;
        }
        switch (suit)
        {
            case Card.CLUBS: suitString = "Clubs"; break;
            case Card.DIAMONDS: suitString = "Diamonds"; break;
            case Card.HEARTS: suitString = "Hearts"; break;
            case Card.SPADES: suitString = "Spades"; break;
        }
        return valString + " of " + suitString;
    }

    /** static final variables for the four suits */
    public static final int CLUBS = 1;
    public static final int DIAMONDS = 2;
    public static final int HEARTS = 3;
    public static final int SPADES = 4;
    /** static final variables for the ace and the 3 face cards */
    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    private int value;
    private int suit;
}
