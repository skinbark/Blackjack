public class DealerPlay {

    public static void main(String[] args) {

        CardDeck deck = new CardDeck();
        BJHand dealer = new BJHand();
        boolean stand = false;
        boolean busted = false;
        boolean ace = false;
        int handVal = 0;

        dealer.takeCards(deck, 1);

        while(!stand && !busted) {
            dealer.takeCards(deck, 1);
            handVal = dealer.lowValue();
            ace = dealer.hasAce();
            if (handVal < 7); // hit
            else if (handVal < 12) {
                if (ace) {
                    handVal += 10;
                    stand = true;
                }
            }
            else if (handVal < 17); // hit
            else if (handVal < 22) stand = true;
            else busted = true;
        }

        CardSet.printCards(dealer);
        if (!busted) 
            System.out.println("Dealer has " + handVal);
        else 
            System.out.println("Dealer is busted.");
    }
}

