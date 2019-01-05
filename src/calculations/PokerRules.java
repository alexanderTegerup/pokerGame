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
    private PokerRules.Ranking ranking;

    public Card getCard1(){ return card1; }
    public Card getCard2(){ return card2; }

    public void setHand(Card c1, Card c2) {
        card1 = c1;
        card2 = c2;
    }
}

public class PokerRules {

    // TODO Optimize the code by not using insertionSort() more than necessary
    // TODO Remove calculations for verification that the hand is not better than the hand we calculate in the function

    public enum Ranking
    {
        NOTHING,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH
    }

    /* ---- stubs stubs stubs stubs stubs stubs stubs  ---- */
    private Card[] cardsOnTable = new Card[5]; // stub
    private Deck deck = new Deck(); // stub
    private int numberOfPlayers = 2;

    private Card p1c1 = new Card(Card.Suit.DIAMONDS, Card.Rank.SIX, null);
    private Card p1c2 = new Card(Card.Suit.SPADES, Card.Rank.SIX, null);

    private Card p2c1 = new Card(Card.Suit.SPADES, Card.Rank.ACE, null);
    private Card p2c2 = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT, null);


    //----------------------

    private ArrayList<Hand> playersHands = new ArrayList<>(); // Array list with the hand of each player
    private Ranking tableCardRank;


    /**
     * Determines which player who has the best hand.
     * @return The ID of a player which has the best hand.
     */
    public void determineBestHand()
    {

        cardsOnTable[0] = new Card(Card.Suit.HEARTS, Card.Rank.THREE, null);
        cardsOnTable[1] = new Card(Card.Suit.HEARTS, Card.Rank.JACK, null);
        cardsOnTable[2] = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE, null);
        cardsOnTable[3] = new Card(Card.Suit.HEARTS, Card.Rank.ACE, null);
        cardsOnTable[4] = new Card(Card.Suit.HEARTS, Card.Rank.THREE, null);

        Hand hand1 = new Hand();
        hand1.setHand(p1c1,p1c2);

        Hand hand2 = new Hand();
        hand2.setHand(p2c1,p2c2);

        for(int i=0; i<numberOfPlayers; i++){

            playersHands.add(hand1);
            playersHands.add(hand2);
        }


        /* Find out which poker hand there is on the table. */

        if( checkStraight(cardsOnTable) && checkFlush(cardsOnTable) )
        {
            tableCardRank = Ranking.STRAIGHT_FLUSH;
        }
        else if ( checkFourOfAKind(cardsOnTable) )
        {
            tableCardRank = Ranking.FOUR_OF_A_KIND;
        }
        else if ( checkFullHouse(cardsOnTable) )
        {
            tableCardRank = Ranking.FULL_HOUSE;
        }
        else if( checkFlush(cardsOnTable) )
        {
            tableCardRank = Ranking.FLUSH;
        }
        else if( checkStraight(cardsOnTable) )
        {
            tableCardRank = Ranking.STRAIGHT;
        }
        else if ( checkThreeOfAKind(cardsOnTable) )
        {
            tableCardRank = Ranking.THREE_OF_A_KIND;
        }
        else
        {
            tableCardRank = Ranking.NOTHING;
        }

        System.out.println(tableCardRank);


    }


    /**
     * Algorithm that sorts the rank of five cards in ascending order.
     * @param cards The unsorted cards.
     * @return The cards sorted from the lowest to the highest rank.
     */
    private Card[] insertionSort(Card[] cards) {

        Card tmpCard;
        int j;
        for(int i = 1; i<5; i++) {

            j=i;
            while(j>0 && cards[j].getRank().ordinal() < cards[j-1].getRank().ordinal()){

                tmpCard = cards[j-1];
                cards[j-1] = cards[j];
                cards[j] = tmpCard;
                j--;
            }
        }

        return cards;
    }

    /**
     * Method that checks if five cards make a straight. Also checking the special case with straight from ACE to FIVE.
     * @param fiveCards The hand that may or may not make up a straight.
     * @return A boolean that is true if the cards make a straight and is false otherwise.
     */
    private boolean checkStraight(Card[] fiveCards) {

        /* Sort the cards */
        fiveCards = insertionSort(fiveCards); // Why can I remove the return value here?

        /* Check if the cards make a straight. Also checking the special case with a straight from ACE to FIVE. */
        boolean gotStraight =  (fiveCards[4].getRank().ordinal() == fiveCards[3].getRank().ordinal()+1 &&
                                fiveCards[3].getRank().ordinal() == fiveCards[2].getRank().ordinal()+1 &&
                                fiveCards[2].getRank().ordinal() == fiveCards[1].getRank().ordinal()+1 &&
                                fiveCards[1].getRank().ordinal() == fiveCards[0].getRank().ordinal()+1 ||
                                Card.Rank.TWO                    == fiveCards[0].getRank()             &&
                                Card.Rank.THREE                  == fiveCards[1].getRank()             &&
                                Card.Rank.FOUR                   == fiveCards[2].getRank()             &&
                                Card.Rank.FIVE                   == fiveCards[3].getRank()             &&
                                Card.Rank.ACE                    == fiveCards[4].getRank()               );
        return gotStraight;
    }

    /**
     * Method that checks if five cards make a flush.
     * @param fiveCards The hand that may or may not make up a flush.
     * @return A boolean that is true if the cards make a flush and is false otherwise.
     */
    private boolean checkFlush(Card[] fiveCards){

        boolean gotFlush = (fiveCards[0].getSuit() == fiveCards[1].getSuit() &&
                            fiveCards[0].getSuit() == fiveCards[2].getSuit() &&
                            fiveCards[0].getSuit() == fiveCards[3].getSuit() &&
                            fiveCards[0].getSuit() == fiveCards[4].getSuit()   );

        return gotFlush;
    }

    private boolean checkFourOfAKind(Card[] fiveCards){

        boolean gotFourOfAKind = false;

        for (int i=0;i<5;i++)
        {
            if (fiveCards[i%5].getRank() == fiveCards[(i+1)%5].getRank() &&
                fiveCards[(i+1)%5].getRank() == fiveCards[(i+2)%5].getRank() &&
                fiveCards[(i+2)%5].getRank() == fiveCards[(i+3)%5].getRank()   )
            {
                gotFourOfAKind = true;
                break;
            }
        }

        return gotFourOfAKind;
    }

    private boolean checkFullHouse(Card[] fiveCards){

        /* Sort the cards */
        fiveCards = insertionSort(fiveCards);

        boolean gotFullHouse = ( (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                  fiveCards[1].getRank() == fiveCards[2].getRank()  &&
                                  fiveCards[2].getRank() != fiveCards[3].getRank()  &&
                                  fiveCards[3].getRank() == fiveCards[4].getRank()) ||

                                 (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                  fiveCards[1].getRank() != fiveCards[2].getRank()  &&
                                  fiveCards[2].getRank() == fiveCards[3].getRank()  &&
                                  fiveCards[3].getRank() == fiveCards[4].getRank())   );

        return gotFullHouse;
    }

    private boolean checkThreeOfAKind(Card[] fiveCards){

        boolean gotThreeOfAKind = false;

        /* Sort the cards */
        fiveCards = insertionSort(fiveCards);

        /* for loop to permute all the possible combination to get three of a kind */
        for (int i=0; i<3; i++)
        {
            // The last three rows in the if statement are unnecessary since/if we know before this function is called
            // that we don't have anything better than three of a kind.
            if (fiveCards[(i%5)].getRank() == fiveCards[(i+1)%5].getRank()   &&
                fiveCards[(i+1)%5].getRank() == fiveCards[(i+2)%5].getRank() &&
                fiveCards[(i+2)%5].getRank() != fiveCards[(i+3)%5].getRank() && // To verify that we don't have
                fiveCards[(i+2)%5].getRank() != fiveCards[(i+4)%5].getRank() && // four of a kind.
                fiveCards[(i+3)%5].getRank() != fiveCards[(i+4)%5].getRank()   ) // To verify that we don't have full house )
            {
                gotThreeOfAKind = true;
                break;
            }
        }

        return gotThreeOfAKind;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PokerRules p = new PokerRules();
        p.determineBestHand();

    }

}
