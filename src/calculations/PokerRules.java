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

    public void setRanking(PokerRules.Ranking rnk) { ranking = rnk; }
    public PokerRules.Ranking getRanking() {return ranking; }
}

public class PokerRules {

    // TODO Don't call getRank or getSuit that ofter, save those values and reuse them instead
    // TODO Comment the methods and create java doc.
    // TODO Go through and see if I can remove some hard coding.
    // TODO Rename some methods and enums

    public enum Ranking
    {
        NOTHING,
        HIGH_CARD,
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
    private Card p1c2 = new Card(Card.Suit.SPADES, Card.Rank.FIVE, null);

    private Card p2c1 = new Card(Card.Suit.HEARTS, Card.Rank.TWO, null);
    private Card p2c2 = new Card(Card.Suit.SPADES, Card.Rank.EIGHT, null);


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
        cardsOnTable[1] = new Card(Card.Suit.HEARTS, Card.Rank.ACE, null);
        cardsOnTable[2] = new Card(Card.Suit.HEARTS, Card.Rank.FOUR, null);
        cardsOnTable[3] = new Card(Card.Suit.HEARTS, Card.Rank.ACE, null);
        cardsOnTable[4] = new Card(Card.Suit.HEARTS, Card.Rank.ACE, null);

        Hand hand1 = new Hand();
        hand1.setHand(p1c1,p1c2);

        Hand hand2 = new Hand();
        hand2.setHand(p2c1,p2c2);

        for(int i=0; i<numberOfPlayers; i++){

            playersHands.add(hand1);
            playersHands.add(hand2);
        }

        /* Find out which poker hand there is on the table. */
        tableCardRank = determineHandRanking(cardsOnTable);
        System.out.println("cards on table " + tableCardRank);

        Ranking rankPlayer;
        Ranking bestCombination;
        Card[] cardCombination = new Card[5];

        /* Decide the best combination each player can get */
        for (int iPlayersHand=0; iPlayersHand < numberOfPlayers; iPlayersHand++){

            bestCombination = Ranking.NOTHING;
            for (int i=0; i<15; i++){

                /* Test all combinations with two cards from the player and three cards from the table */
                if (i<5){

                    cardCombination[0] = cardsOnTable[i%5];
                    cardCombination[1] = cardsOnTable[(i+1)%5];
                    cardCombination[2] = cardsOnTable[(i+2)%5];
                    cardCombination[3] = playersHands.get(iPlayersHand).getCard1();
                    cardCombination[4] = playersHands.get(iPlayersHand).getCard2();

                }
                /* Test all combinations with the first card from the player and four cards from the table */
                else if (i < 10){

                    cardCombination[0] = cardsOnTable[i%5];
                    cardCombination[1] = cardsOnTable[(i+1)%5];
                    cardCombination[2] = cardsOnTable[(i+2)%5];
                    cardCombination[3] = cardsOnTable[(i+3)%5];
                    cardCombination[4] = playersHands.get(iPlayersHand).getCard1();
                }
                /* Test all combinations with the second card from the player and four cards from the table */
                else{

                    cardCombination[0] = cardsOnTable[i%5];
                    cardCombination[1] = cardsOnTable[(i+1)%5];
                    cardCombination[2] = cardsOnTable[(i+2)%5];
                    cardCombination[3] = cardsOnTable[(i+3)%5];
                    cardCombination[4] = playersHands.get(iPlayersHand).getCard2();
                }

                /* Save the highest combination */
                rankPlayer = determineHandRanking(cardCombination);
                if (rankPlayer.ordinal() > bestCombination.ordinal()){
                    bestCombination = rankPlayer;
                }
            }

            if (bestCombination.ordinal() > tableCardRank.ordinal()){
                playersHands.get(iPlayersHand).setRanking(bestCombination);
            }
            else {
                playersHands.get(iPlayersHand).setRanking(tableCardRank);
            }

            System.out.println("player " + iPlayersHand + ": " + playersHands.get(iPlayersHand).getRanking());
        }
    }


    private Ranking determineHandRanking(Card[] fiveCards){

        Ranking rankCards;

        /* Sort the cards so that checkStraight, checkFullHouse,
        checkThreeOfAKind, checkTwoPair and checkPair will work */
        fiveCards = insertionSort(fiveCards);

        if( checkStraight(fiveCards) && checkFlush(fiveCards) )
        {
            rankCards = Ranking.STRAIGHT_FLUSH;
        }
        else if ( checkFourOfAKind(fiveCards) )
        {
            rankCards = Ranking.FOUR_OF_A_KIND;
        }
        else if ( checkFullHouse(fiveCards) )
        {
            rankCards = Ranking.FULL_HOUSE;
        }
        else if( checkFlush(fiveCards) )
        {
            rankCards = Ranking.FLUSH;
        }
        else if( checkStraight(fiveCards) )
        {
            rankCards = Ranking.STRAIGHT;
        }
        else if ( checkThreeOfAKind(fiveCards) )
        {
            rankCards = Ranking.THREE_OF_A_KIND;
        }
        else if ( checkTwoPair(fiveCards) )
        {
            rankCards = Ranking.TWO_PAIR;
        }
        else if ( checkPair(fiveCards))
        {
            rankCards = Ranking.PAIR;
        }
        else
        {
            rankCards = Ranking.NOTHING;
        }

        return rankCards;
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
            if (fiveCards[i%5].getRank()     == fiveCards[(i+1)%5].getRank() &&
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

        /* For loop to permute all the possible combination to get three of a kind */
        for (int i=0; i<3; i++)
        {

            if (fiveCards[(i%5)].getRank()   == fiveCards[(i+1)%5].getRank() &&
                fiveCards[(i+1)%5].getRank() == fiveCards[(i+2)%5].getRank()   );

            {
                gotThreeOfAKind = true;
                break;
            }
        }

        return gotThreeOfAKind;
    }

    private boolean checkTwoPair(Card[] fiveCards){

        boolean gotTwoPair = ( (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                fiveCards[2].getRank() == fiveCards[3].getRank()) ||

                               (fiveCards[1].getRank() == fiveCards[2].getRank()  &&
                                fiveCards[3].getRank() == fiveCards[4].getRank()) ||

                               (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                fiveCards[3].getRank() == fiveCards[4].getRank())   );

        return gotTwoPair;
    }

    private boolean checkPair(Card[] fiveCards){

        boolean gotPair = ( fiveCards[0].getRank() == fiveCards[1].getRank() ||
                            fiveCards[1].getRank() == fiveCards[2].getRank() ||
                            fiveCards[2].getRank() == fiveCards[3].getRank() ||
                            fiveCards[3].getRank() == fiveCards[4].getRank()   );

        return gotPair;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PokerRules p = new PokerRules();
        p.determineBestHand();

    }

}
