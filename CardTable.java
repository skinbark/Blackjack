// package cards;

import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.Timer;

public class CardTable extends JComponent implements MouseListener {
    
    private static final Color BACKGROUND_COLOR = new Color(0,112,0);
    private static final int   TABLE_WIDTH       = 800;    // Pixels.
    private static final  int  TABLE_HEIGHT     = 600;
    private static final int MAX_HANDS = 4;
    private ArrayList<BJHand> hands;
    private int active;
    private BJHand dealerHand;
    private Image cardImages;
    private GameModel game;
    private Rectangle2D hitButton;
    private Rectangle2D standButton;
    private Rectangle2D newButton;
    private Rectangle2D incButton;
    private Rectangle2D decButton;
    private Rectangle2D doubleButton;
    private Rectangle2D splitButton;
    private Rectangle2D surrenderButton;
    private Rectangle2D stackButton;
    private String hitLabel = "HIT";
    private String standLabel = "STAND";
    private String newLabel = "NEW HAND";
    private String incLabel = "INCREASE";
    private String decLabel = "DECREASE";
    private String doubleLabel = "DOUBLE";
    private String splitLabel = "SPLIT";
    private String surrenderLabel = "SURRENDER";
    private String stackLabel = "STACK";
    private Font sansbold16;
    private FontRenderContext context;
    
    public CardTable(GameModel gameModel) {
        game = gameModel;        
        //... Initialize graphics
        setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT)); 
        setBackground(Color.blue); 
        ClassLoader cl = CardTable.class.getClassLoader();
        URL imageURL = cl.getResource("cards.png");
        cardImages = Toolkit.getDefaultToolkit().createImage(imageURL);
        //... Add mouse listener.
        addMouseListener(this);
        sansbold16 = new Font("SansSerif", Font.BOLD, 16);
        double leftX = 130;
        double topY = 480;
        double rectWidth = 120;
        double rectHeight = 30;
        newButton = new Rectangle2D.Double(leftX, topY, rectWidth, 
                                                                rectHeight);
        hitButton = new Rectangle2D.Double(leftX+140, topY, 
                                                      rectWidth, rectHeight);
        standButton = new Rectangle2D.Double(leftX+2*140, topY, 
                                                      rectWidth, rectHeight);
        surrenderButton = new Rectangle2D.Double(leftX+3*140, topY,
                                                      rectWidth, rectHeight);
        incButton = new Rectangle2D.Double(leftX, topY+50,
                                                      rectWidth, rectHeight);
        decButton = new Rectangle2D.Double(leftX+140, topY+50, rectWidth,
                                                                rectHeight);
        doubleButton = new Rectangle2D.Double(leftX+2*140, topY+50, rectWidth,
                                                                rectHeight);
        splitButton = new Rectangle2D.Double(leftX+3*140, topY+50,
                                                        rectWidth, rectHeight);
        stackButton = new Rectangle2D.Double(400, 0, rectWidth, rectHeight);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        //... Paint background
        int width = getWidth();
        int height = getHeight();
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, width, height);
        Graphics2D g2 = (Graphics2D) g;
        drawButton(g, hitButton, hitLabel);
        drawButton(g, standButton, standLabel);
        drawButton(g, newButton, newLabel);
        drawButton(g, incButton, incLabel);
        drawButton(g, decButton, decLabel);
        drawButton(g, doubleButton, doubleLabel);
        drawButton(g, splitButton, splitLabel);
        drawButton(g, surrenderButton, surrenderLabel);
        drawButton(g, stackButton, stackLabel);
        g2.drawString("Bankroll", 10, 500);
        g2.drawString("" + (game.getBankroll()), 10, 520);
        g2.drawString("Bet Amount", 10, 540);
        g2.drawString("" + game.getBetAmount(), 10, 560);
        hands = game.getHandSet();
        active = game.handNumber();
        dealerHand = game.getDealerHand();
        drawHands(g);
    }

    public void drawButton(Graphics g, Rectangle2D rect, String label) {
        Rectangle2D bounds;
        double x, y;
        double ascent;
        double baseY;

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.RED);
        g2.fill(rect);
        g2.setPaint(Color.YELLOW);
        g2.setFont(sansbold16);
        context = g2.getFontRenderContext();
        bounds = sansbold16.getStringBounds(label, context);
        x = rect.getX()+((rect.getWidth()-bounds.getWidth())/2);
        y = rect.getY()+((rect.getHeight()-bounds.getHeight())/2);
        ascent = -bounds.getY();
        baseY = y + ascent;
        g2.drawString(label, (int) x, (int) baseY);
    }

    public void drawHands(Graphics g) {
        Card dCard;
        for (int i = 0; i < dealerHand.numCards(); i++) {
            if (i==0 && !hands.get(active).getHandOver()) dCard = null;
            else dCard = dealerHand.getCard(i);
            drawCard(g, dCard, i*40, 0);
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.RED);
        g2.setFont(sansbold16);
        for (int x = 0; x < hands.size(); x++) {
            int card_y;
            int disp_x;

            if (x % 2 == 0) 
                card_y = 200;
            else
                card_y = 344;
            if (x < MAX_HANDS / 2)
                disp_x = 0;
            else
                disp_x = 400;
            for (int i = 0; i < hands.get(x).numCards(); i++) {
                drawCard(g, hands.get(x).getCard(i), i*40+disp_x, card_y);
            }
            if (hands.get(x).getStanding())
                g2.drawString("STAND", disp_x, card_y+120);
            else if (hands.get(x).isBusted())
                g2.drawString("BUST", disp_x, card_y+120);
            if (hands.get(x).getHandOver() && 
                hands.get(x).numCards() != 0) {
                if (game.playerBlackjack())
                    g2.drawString("BLACKJACK!", disp_x, card_y);
                else if (hands.get(x).getWon()) {
                    if (hands.get(x).getDoubled())
                        g2.drawString("PLAYER WINS DOUBLE", disp_x,
                                                                 card_y);
                    else
                        g2.drawString("PLAYER WINS", disp_x, card_y);
                }
                else if (hands.get(x).getTied())
                     g2.drawString("PUSH", disp_x, card_y);
                else if (hands.get(x).getLost()) {
                    if (hands.get(x).getDoubled())
                        g2.drawString("PLAYER LOSES DOUBLE", disp_x,
                                                                 card_y);
                    else
                        g2.drawString("PLAYER LOSES", disp_x, card_y);
                }
            }
        }
    }

    /**
     * Draws a card in a 79x123 pixel rectangle with its
     * upper left corner at a specified point (x, y).  Drawing the card
     * requires the image file "cards.png".
     * @param g The graphics context used for drawing the card.
     * @param card The card that is to be drawn.  If the value is null, then a
     * face-down card is drawn.
     * @param x the x-coord of the upper left corner of the card
     * @param y the y-coord of the upper left corner of the card
     */
    public void drawCard(Graphics g, Card card, int x, int y) {
       int cx;  // x-coord of upper left corner of the card inside cardsImage
      int cy;   // y-coord of upper left corner of the card inside cardsImage
        if (card == null) {
           cy = 4*123;  // coords for a face-down card.
           cx = 2*79;
        }
        else {
           cx = (card.getValue()-1)*79;
           switch (card.getSuit()) {
               case Card.CLUBS:
                  cy = 0;
                  break;
               case Card.DIAMONDS:
                  cy = 123;
                  break;
               case Card.HEARTS:
                  cy = 2*123;
                  break;
               default:     // spades
                  cy = 3*123;
                  break;
           }
        }
        g.drawImage(cardImages,x,y,x+79,y+123,cx,cy,cx+79,cy+123,this);
    } 

    public Rectangle2D find(Point2D p) {
        if (hitButton.contains(p))
            return hitButton;
        else if (standButton.contains(p))
            return standButton;
        else if (newButton.contains(p))
            return newButton;
        else if (incButton.contains(p))
            return incButton;
        else if (decButton.contains(p))
            return decButton;
        else if (doubleButton.contains(p))
            return doubleButton;
        else if (splitButton.contains(p))
            return splitButton;
        else if (surrenderButton.contains(p))
            return surrenderButton;
        else if (stackButton.contains(p))
            return stackButton;
        else
            return null;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        Rectangle2D current;
        current = find(e.getPoint());
        if (current == hitButton) {
                game.hit();
                repaint();
        }
        else if (current == standButton) {
                game.stand();
                repaint();
        }
        else if (current == newButton) {
                game.newRound();
                repaint();
        }
        else if (current == incButton) {
                game.increaseBet();
                repaint();
        }
        else if (current == decButton) {
                game.decreaseBet();
                repaint();
        }
        else if (current == doubleButton) {
                game.doubleDown();
                repaint();
        }
        else if (current == splitButton) {
                game.splitHand();
                repaint();
        }
        else if (current == surrenderButton) {
                game.giveUp();
                repaint();
        }
        else if (current == stackButton) {
                game.stackDeck();
        }
    }
    public void mouseReleased(MouseEvent e) {}  // ignore these events
    public void mouseEntered(MouseEvent e) {}  // ignore these events
    public void mouseExited(MouseEvent e) {}   // ignore these events
}
