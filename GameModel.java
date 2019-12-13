import java.util.*;
import java.awt.*;
import javax.swing.*;

public class GameModel {

    public static final int NUM_DECKS = 4; // # of decks in the shoe
    private CardDeck deck; 
    private Shoe shoe;
    private CardSet usedPile;
    private ArrayList<BJHand> handSet;
    private BJHand dealer; 
    private int activeHand;
    private int current;
    private boolean surrendered;
    private int bankroll;
    private int betAmount;
    private boolean blackjack;

    public GameModel() {
        shoe = new Shoe();
        for (int i=0; i<NUM_DECKS; i++) {
            deck = new CardDeck();
            shoe.takeCards(deck, CardDeck.DECK_SIZE);
        }
        usedPile = new CardSet();
        handSet = new ArrayList<BJHand>();
        handSet.add(new BJHand());
        dealer = new BJHand();
        handSet.get(0).setHandOver(true);
        activeHand = 0;
        surrendered = false;
        bankroll = 950;
        betAmount = 50;
    }

    public ArrayList<BJHand> getHandSet() {
        return handSet;
    }
    
    public int handNumber() {
        return activeHand;
    }

    public BJHand getDealerHand() {
        return dealer;
    }

    public boolean playerBlackjack() {
        return blackjack;
    }

    public int getBankroll() {
        return bankroll;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void increaseBet() {
        if (betAmount <= 95 && handSet.get(activeHand).getHandOver()) {
            betAmount += 5;
            bankroll -= 5;
        }
    }

    public void decreaseBet() {
        if (betAmount >= 10 && handSet.get(activeHand).getHandOver()) {
            betAmount -= 5;
            bankroll += 5;
        }
    }

    public void hit() {
        if (handSet.get(activeHand).getHandOver()) return;
        handSet.get(activeHand).takeCards(shoe, 1);
        if (handSet.get(activeHand).isBusted()) {
            if ((activeHand+1) < handSet.size()) 
                activeHand++;
            else
                dealerPlay();
        }
        else if (handSet.get(activeHand).getDoubled())
            stand();
    }

    public void stand() {
        if (handSet.get(activeHand).getHandOver()) return;
        handSet.get(activeHand).setStanding(true);
        if ((activeHand+1) < handSet.size())
            activeHand++;
        else
            dealerPlay();
    }

    public void doubleDown() {
        if (handSet.get(activeHand).getHandOver()) return;
        if (handSet.get(activeHand).numCards() > 2) return;
        handSet.get(activeHand).setDoubled(true);
        hit();
    }

    public void splitHand() {
        int card1, card2;
        if (handSet.size() > 3) return;
        if (handSet.get(activeHand).getHandOver()) return;
        if (handSet.get(activeHand).numCards() > 2) return;
        card1 = handSet.get(activeHand).getCard(0).getValue(); 
        card2 = handSet.get(activeHand).getCard(1).getValue(); 
        if (card1 > 10) card1 = 10;
        if (card2 > 10) card2 = 10;
        if (card1 != card2) return;
        BJHand temp = new BJHand();
        temp.takeCards(handSet.get(activeHand), 1);
        handSet.get(activeHand).takeCards(shoe, 1);
        temp.takeCards(shoe, 1);
        handSet.add(temp);
        if (handSet.get(activeHand).getCard(0).getValue() == Card.ACE) {
            stand();
            stand();
        }
    }

    public void giveUp() {
        if (activeHand > 0) return;
        if (handSet.get(0).getHandOver()) return;
        if (handSet.get(0).numCards() > 2) return;
        handSet.get(0).setSurrendered(true);
        dealerPlay(); 
    }

    public void stackDeck() {
        int value;
        int position;
        int start;
        Scanner in = new Scanner(System.in);
        System.out.print("Value ");
        value = in.nextInt();
        System.out.print("Position ");
        position = in.nextInt();
        start = position;
        while (shoe.getCard(start).getValue() != value)
            start++;
        Card c = shoe.removeCard(start);
        shoe.addCard(c, position);
    }

    public void newRound() {
        if (!handSet.get(0).getHandOver()) return;
        for (int c = 0; c < handSet.size(); c++) {
            usedPile.takeCards(handSet.get(c), handSet.get(c).numCards());
        }
        handSet.clear();
        handSet.add(new BJHand());
        usedPile.takeCards(dealer, dealer.numCards());
        if (shoe.numCards() < CardDeck.DECK_SIZE) {
            shoe.takeCards(usedPile, usedPile.numCards());
            shoe.shuffle();
        }
        activeHand = 0;
        current = 0;
        blackjack = false;
        handSet.get(0).takeCards(shoe, 2);
        dealer.takeCards(shoe, 2);
        if (handSet.get(0).hasAce() && handSet.get(0).lowValue() == 11)
            if (dealer.hasAce() && dealer.lowValue() == 11)
                playerTies();
            else {
                blackjack = true;
                playerWins();
            }
        else if (dealer.hasAce() && dealer.lowValue() == 11)
            playerLoses();
    }
    public void dealerPlay() {
        boolean ace = false;
        int handVal = 0;
        boolean allBusted = true;
        dealer.reset();

        for (int x=0; x<handSet.size(); x++) {
            if (!handSet.get(x).isBusted() && 
                !handSet.get(x).getSurrendered()) {
                allBusted = false;
            }
        }
        if (!allBusted) {
        while (!dealer.getStanding() && !dealer.isBusted()) {
            handVal = dealer.lowValue();
            ace = dealer.hasAce();
            if (handVal < 7)
                dealer.takeCards(shoe, 1);
            else if (handVal < 12) {
                if (ace) {
                    handVal += 10;
                    dealer.setStanding(true);
                } else
                    dealer.takeCards(shoe, 1);
            }
            else if (handVal < 17)
                dealer.takeCards(shoe, 1);
            else if (handVal < 22) dealer.setStanding(true);
        }
        } else {
            dealer.setStanding(true);
        }
        compareHands();
    }


    private void getPair() {
        handSet.get(activeHand).takeCards(shoe, 1);
        int i = 0;
    }

    public void compareHands() {
        int playerVal = 0;
        int dealerVal = 0;
        for ( current = 0; current < handSet.size(); current++) {
            if (handSet.get(current).isBusted() ||
                handSet.get(current).getSurrendered()) playerLoses();
            else if (dealer.isBusted()) playerWins();
            else {
                playerVal = handSet.get(current).lowValue();
                dealerVal = dealer.lowValue();

                if (handSet.get(current).hasAce() && playerVal < 12)
                    playerVal += 10;
                if (dealer.hasAce() && dealerVal < 12)
                    dealerVal += 10;
                if (playerVal > dealerVal)
                    playerWins();
                else if (playerVal == dealerVal)
                    playerTies();
                else
                    playerLoses();
            }
        }
    }

    private void playerWins() {
        handSet.get(current).setWon(true);
        handSet.get(current).setHandOver(true);
        bankroll += betAmount;
        if (handSet.get(current).getDoubled()) 
            bankroll += betAmount;
        else if (blackjack)
            bankroll += (betAmount / 2);
    }

    private void playerTies() {
        handSet.get(current).setTied(true);
        handSet.get(current).setHandOver(true);
    }

    private void playerLoses() {
        handSet.get(current).setLost(true);
        handSet.get(current).setHandOver(true);
        bankroll -= betAmount;
        if (handSet.get(current).getDoubled())
            bankroll -= betAmount;
        if (handSet.get(current).getSurrendered())
            bankroll += betAmount/2;
    }
}
