package calculations;

import common.Observer;
import player.Player;
import table.Card;
import table.Deck;
import player.Players;
import java.util.ArrayList;

class Hand{
    private Card card1;
    private Card card2;

    public Card getCard1(){ return card1; }
    public Card getCard2(){ return card2; }

    public void setHand(Card c1, Card c2) {
        card1 = c1;
        card2 = c2;
    }
}

public class PokerRules {

    //private Card[] Hand;

    /* ---- stubs  ---- */
    private Card[] cardsOnTable = new Card[5]; // stub
    private Deck deck = new Deck(); // stub
    private int numberOfPlayers = 2;
    private Hand test;
    //----------------------

    private ArrayList<Hand> playersHands = new ArrayList<>(); // Array list with the hand of each player


    /**
     * Determines which player who has the best hand.
     * @return The ID of a player which has the best hand.
     */
    public void determineBestHand()
    {
        for(int i=0; i<5; i++){
            cardsOnTable[i] = deck.getTopCard();
        }

        Hand hand1 = new Hand();
        hand1.setHand(deck.getTopCard(),deck.getTopCard());

        Hand hand2 = new Hand();
        hand2.setHand(deck.getTopCard(),deck.getTopCard());


        System.out.println("First player:");
        System.out.println(hand1.getCard1().getSuit() + " " + hand1.getCard1().getRank());
        System.out.println(hand1.getCard2().getSuit() + " " + hand1.getCard2().getRank() + "\n");

        System.out.println("Second player:");
        System.out.println(hand2.getCard1().getSuit() + " " + hand2.getCard1().getRank());
        System.out.println(hand2.getCard2().getSuit() + " " + hand2.getCard2().getRank() + "\n");


        for(int i=0; i<numberOfPlayers; i++){

            playersHands.add(hand1);
            playersHands.add(hand2);
        }

        System.out.println("Cards on table:");
        for(int i=0; i<5; i++){
            System.out.println(cardsOnTable[i].getSuit() + " " + cardsOnTable[i].getRank());
        }
        /*
        test = playersHands.get(1);
        System.out.println(test.getCard1().getSuit() + " "  + test.getCard1().getRank() + " " + "\n");*/




    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PokerRules p = new PokerRules();
        p.determineBestHand();

    }


}
