public class BJHand extends CardSet {

    private boolean won;
    private boolean tied;
    private boolean lost;
    private boolean standing;
    private boolean doubled;
    private boolean surrendered;
    private boolean handOver;

    public void reset() {
        won = false;
        tied = false;
        lost = false;
        standing = false;
        handOver = false;
        doubled = false;
        surrendered = false;
    }

    public boolean getWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean getTied() {
        return tied;
    }

    public void setTied(boolean tied) {
        this.tied = tied;
    }

    public boolean getLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean getStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public boolean getDoubled() {
        return doubled;
    }

    public void setDoubled(boolean doubled) {
        this.doubled = doubled;
    }

    public boolean getSurrendered() {
        return surrendered;
    }

    public void setSurrendered(boolean surrendered) {
        this.surrendered = surrendered;
    }

    public boolean getHandOver() {
        return handOver;
    }

    public void setHandOver(boolean handOver) {
        this.handOver = handOver;
    }

    int lowValue() {

        int sum = 0;
        for (int i = 0; i < numCards(); i++) {
            int val = getCard(i).getValue();
            if (val > 10) val = 10;
            sum = sum + val;
        }
        return sum;
    }

    boolean hasAce() {
        boolean ace = false;
        for (int i = 0; i < numCards(); i++) {
            if (getCard(i).getValue() == Card.ACE)
                ace = true;
        }
        return ace;
    }

    boolean isBusted() {
        if (lowValue() > 21) 
            return true;
        return false;
    }
}
