package game;

import common.Card;
import common.Hand;
import common.HandRank;
import common.HoleCards;

/**
 * <h1> Class that determines which player who has the best poker hand </h1>
 */
public class PokerRules
{

    // TODO Don't call getRank or getSuit that often, save those values and reuse them instead
    // TODO Comment the methods and create java doc.
    // TODO Go through and see if I can remove some hard coding.
    // TODO Rename some methods and enums

    // TODO Remove some code repetition. For instance, the decideHighest... methods could be merged into one method that
    // TODO takes an argument that determines what to do exactly.

    // TODO Write some unit tests for this module. There might be combinations of cards where the code determines the
    // TODO wrong winner.
    // TODO Do I need to call the ordinal method in checkStraight?
    // TODO Remove the hard coding of numberOfWinners
    // TODO Make determineWinner take an array of hands and the cards on the table as arguments.


    /**
     * Determines the highest possible rank of the five cards given as an input.
     * @param fiveCards An array containing five cards.
     * @return The highest ranking of the five cards given as an input.
     */
    public common.HandRank determineHandRank(Card[] fiveCards)
    {

        common.HandRank rankCards;

        /* Sort the cards so that checkStraight, checkFullHouse,
        checkThreeOfAKind, checkTwoPair and checkPair will work */
        fiveCards = insertionSort(fiveCards);

        if (checkStraight(fiveCards) && checkFlush(fiveCards))
        {
            rankCards = common.HandRank.STRAIGHT_FLUSH;
        }
        else if (checkFourOfAKind(fiveCards))
        {
            rankCards = common.HandRank.FOUR_OF_A_KIND;
        }
        else if (checkFullHouse(fiveCards))
        {
            rankCards = common.HandRank.FULL_HOUSE;
        }
        else if (checkFlush(fiveCards))
        {
            rankCards = common.HandRank.FLUSH;
        }
        else if (checkStraight(fiveCards))
        {
            rankCards = common.HandRank.STRAIGHT;
        }
        else if (checkThreeOfAKind(fiveCards))
        {
            rankCards = common.HandRank.THREE_OF_A_KIND;
        }
        else if (checkTwoPair(fiveCards))
        {
            rankCards = common.HandRank.TWO_PAIR;
        }
        else if (checkPair(fiveCards))
        {
            rankCards = common.HandRank.PAIR;
        }
        else
        {
            rankCards = common.HandRank.NOTHING;
        }

        return rankCards;
    }


    /**
     * Algorithm that sorts the rank of an arbitrary number of cards in ascending order.
     *
     * @param cards The unsorted cards.
     * @return The cards sorted from the lowest to the highest rank.
     */
    private Card[] insertionSort(Card[] cards)
    {

        Card tmpCard;
        int numberOfCards = cards.length;
        int j;
        for (int i = 1; i < numberOfCards; i++)
        {

            j = i;
            while (j > 0 && cards[j].getRank().ordinal() < cards[j - 1].getRank().ordinal())
            {

                tmpCard = cards[j - 1];
                cards[j - 1] = cards[j];
                cards[j] = tmpCard;
                j--;
            }
        }

        return cards;
    }


    public Card[] getBestCardsSameRank(Card[] hand1, Card[] hand2, HandRank rank)
    {

        Card[] bestCards = hand1;

        switch (rank)
        {
            case NOTHING:
                bestCards = decideHighestCard(hand1, hand2);
                break;

            case PAIR:

                bestCards = decideBestPair(hand1, hand2);

                break;

            case TWO_PAIR:
                bestCards = decideBestTwoPair(hand1, hand2);
                break;

            case THREE_OF_A_KIND:
                bestCards = decideBestThreeOfAKind(hand1, hand2);
                break;

            case STRAIGHT:
                bestCards = decideBestStraight(hand1, hand2);
                break;

            case FLUSH:
            case FULL_HOUSE:
            case FOUR_OF_A_KIND:
            case STRAIGHT_FLUSH:
        }

        return bestCards;
    }
    /**
     * Method that determines which one of two players who has the highest card, including the cards on the table.
     *
     * @param hand1 Hand of five cards.
     * @param hand2 Hand of five cards.
     * @return The hand that contains the highest card.
     */
    public Card[] decideHighestCard(Card[] hand1, Card[] hand2)
    {

        hand1 = insertionSort(hand1);
        hand2 = insertionSort(hand2);

        for (int i = 0; i < 5; i++)
        {

            if (hand1[4 - i].getRank().ordinal() > hand2[4 - i].getRank().ordinal())
            {
                return hand1;
            }
            else if (hand1[4 - i].getRank().ordinal() < hand2[4 - i].getRank().ordinal())
            {
                return hand2;
            }
        }

        return hand1;
    }

    /**
     * Method that decides which of two players, who both have pair, has the best hand. If both players have the
     * same pair, the player with the highest of the three remaining cards will have the best hand.
     *
     * @param hand1 Hand of five cards.
     * @param hand2 Hand of five cards.
     * @return The hand that contains the highest pair.
     */
    public Card[] decideBestPair(Card[] hand1, Card[] hand2)
    {
        hand1 = insertionSort(hand1);
        hand2 = insertionSort(hand2);


        Card.Rank pairHand1 = hand1[0].getRank(); // They need to be initialized.
        Card.Rank pairHand2 = hand2[0].getRank(); // They need to be initialized.

        /* Saving the index where the cards that make up a pair are. */
        int idxPair1 = 0;
        int idxPair2 = 0;

        for (int i = 0; i < 4; i++)
        {
            /* Save the ranking of the pair. */
            if (hand1[i].getRank() == hand1[i + 1].getRank())
            {
                pairHand1 = hand1[i].getRank();
                idxPair1 = i + 1;
            }
            /* Save the ranking of the pair of the second hand. */
            if (hand2[i].getRank() == hand2[i + 1].getRank())
            {
                pairHand2 = hand2[i].getRank();
                idxPair2 = i + 1;
            }
        }

        int idxHighestCard1 = 4;
        int idxHighestCard2 = 4;

        /* If hand 1 has the highest pair, return hand1. */
        if (pairHand1.ordinal() > pairHand2.ordinal())
        {
            return hand1;
        }
        /* If hand 2 has the highest pair, return hand2. */
        else if (pairHand1.ordinal() < pairHand2.ordinal())
        {
            return hand2;
        }
        /* If they have the same pair, return the index of the player with the highest
           card of their three remaining cards */
        else if (pairHand1 == pairHand2)
        {

            /* Loop through the three highest remaining cards for both players */
            for (int i=4; i > 1; i--)
            {

                if (i == idxPair1)
                { // Skip the cards that make up the pair.
                    idxHighestCard1 -= 2;
                }
                if (i == idxPair2)
                { // Skip the cards that make up the pair.
                    idxHighestCard2 -= 2;
                }

                if (hand1[idxHighestCard1].getRank().ordinal() > hand2[idxHighestCard2].getRank().ordinal())
                {
                    return hand1; // Player 1 wins because she/he has the same pair as player 2 but
                    // the highest card of the three remaining cards.
                }
                else if (hand1[idxHighestCard1].getRank().ordinal() < hand2[idxHighestCard2].getRank().ordinal())
                {
                    return hand2; // Player 2 wins because she/he has the same pair as player 1 but
                    // the highest card of the three remaining cards.
                }
                idxHighestCard1--;
                idxHighestCard2--;
            }
        }

        return hand1;
    }


    /**
     * Method that decides which of two players, who both have two pair, has the best hand. The player with the highest
     * pair wins. If two players have the same two pair, then the fifth card kicker determines the winner.
     *
     * @param hand1 Hand of five cards.
     * @param hand2 Hand of five cards.
     * @return The hand that contains the highest two pair.
     */
    public Card[] decideBestTwoPair(Card[] hand1, Card[] hand2)
    {

        hand1 = insertionSort(hand1);
        hand2 = insertionSort(hand2);


        Card.Rank highestPairHand1 = hand1[0].getRank(); // They need to be initialized.
        Card.Rank lowestPairHand1 = hand1[0].getRank();

        Card.Rank highestPairHand2 = hand2[0].getRank(); // They need to be initialized.
        Card.Rank lowestPairHand2 = hand2[0].getRank();

        boolean lowestPairFoundHand1 = false;
        boolean lowestPairFoundHand2 = false;

        int idxHighestPairHand1 = 0;
        int idxLowestPairHand1 = 0;

        int idxHighestPairHand2 = 0;
        int idxLowestPairHand2 = 0;

        for (int i = 0; i < 4; i++)
        {

            if (hand1[i].getRank() == hand1[i + 1].getRank())
            {

                if (!lowestPairFoundHand1)
                {
                    lowestPairHand1 = hand1[i].getRank();
                    lowestPairFoundHand1 = true;
                    idxLowestPairHand1 = i + 1;

                }
                else
                {
                    highestPairHand1 = hand1[i].getRank();
                    idxHighestPairHand1 = i + 1;
                }
            }

            if (hand2[i].getRank() == hand2[i + 1].getRank())
            {

                if (!lowestPairFoundHand2)
                {
                    lowestPairHand2 = hand2[i].getRank();
                    lowestPairFoundHand2 = true;
                    idxLowestPairHand2 = i + 1;
                }
                else
                {
                    highestPairHand2 = hand2[i].getRank();
                    idxHighestPairHand2 = i + 1;
                }
            }
        }

        // Return the hand with the highest pair.
        if (highestPairHand1.ordinal() > highestPairHand2.ordinal())
        {
            return hand1;
        }
        else if (highestPairHand1.ordinal() < highestPairHand2.ordinal())
        {
            return hand2;
        }
        // If the two players have the same highest pair, decide which one that has the second highest pair.
        else if (highestPairHand1 == highestPairHand2)
        {

            if (lowestPairHand1.ordinal() > lowestPairHand2.ordinal())
            {
                return hand1;
            }
            else if (lowestPairHand1.ordinal() < lowestPairHand2.ordinal())
            {
                return hand2;
            }
            // If the two players have the same two pairs, the player with the highest kicker wins.
            else if (lowestPairHand1 == lowestPairHand2)
            {

                // The indices of the kickers in the hands.
                int kickerIndexHand1 = 4;
                int kickerIndexHand2 = 4;

                // Temporary indices used for finding the indices of the kickers.
                int indexHand1 = 4;
                int indexHand2 = 4;

                for (int i = 0; i < 3; i++) {

                    if ((indexHand1 == idxHighestPairHand1) || (indexHand1 == idxLowestPairHand1)) {
                        indexHand1 -= 2;
                    } else {
                        kickerIndexHand1 = indexHand1;
                    }

                    if ((indexHand2 == idxHighestPairHand2) || (indexHand2 == idxLowestPairHand2)) {
                        indexHand2 -= 2;
                    } else {
                        kickerIndexHand2 = indexHand2;
                    }

                }

                if (hand1[kickerIndexHand1].getRank().ordinal() > hand2[kickerIndexHand2].getRank().ordinal())
                {
                    return hand1;
                }
                else if (hand1[kickerIndexHand1].getRank().ordinal() < hand2[kickerIndexHand2].getRank().ordinal())
                {
                    return hand2;
                }

            }
        }

        return hand1;
    }

    /**
     * Method that decides which of two players, who both have three of a kind, has the best hand. The player with the
     * highest ranked cards that makes up three of a kind wins. If two players have the same three of a kind, then the
     * highest of the two remaining card kickers determines the winner.
     *
     * @param hand1 Hand of five cards.
     * @param hand2 Hand of five cards.
     * @return The hand that contains the three of a kind.
     */
    public Card[] decideBestThreeOfAKind(Card[] hand1, Card[] hand2)
    {

        hand1 = insertionSort(hand1);
        hand2 = insertionSort(hand2);

        Card.Rank rankThreeOfAKindHand1 = hand1[0].getRank();
        Card.Rank rankThreeOfAKindHand2 = hand2[0].getRank();

        int indexThreeOfAKindHand1 = 0;
        int indexThreeOfAKindHand2 = 0;

        for (int i = 0; i < 3; i++)
        {

            if ((hand1[i] == hand1[i + 1]) && (hand1[i + 1] == hand1[i + 2]))
            {
                rankThreeOfAKindHand1 = hand1[i].getRank();
                indexThreeOfAKindHand1 = i + 2;
            }
            if ((hand2[i] == hand2[i + 1]) && (hand2[i + 1] == hand2[i + 2]))
            {
                rankThreeOfAKindHand2 = hand2[i].getRank();
                indexThreeOfAKindHand2 = i + 2;
            }
        }

        // Return the index of the player with the highest three of a kind.
        if (rankThreeOfAKindHand1.ordinal() > rankThreeOfAKindHand2.ordinal())
        {
            return hand1;
        }
        else if (rankThreeOfAKindHand1.ordinal() < rankThreeOfAKindHand2.ordinal())
        {
            return hand2;
        }
        // If the two players have the same three of a kind, the player with the highest kicker wins.
        else if (rankThreeOfAKindHand1 == rankThreeOfAKindHand2)
        {

            int indexContHand1 = 4;
            int indexContHand2 = 4;
            for (int i = 4; i > 2; i--)
            {

                if (i == indexThreeOfAKindHand1)
                {
                    indexContHand1 -= 3;
                }
                if (i == indexThreeOfAKindHand2)
                {
                    indexContHand2 -= 3;
                }

                // Decide which player who has the highest kicker.
                if (hand1[indexContHand1].getRank().ordinal() > hand2[indexContHand2].getRank().ordinal())
                {
                    return hand1;
                }
                else if (hand1[indexContHand1].getRank().ordinal() < hand2[indexContHand2].getRank().ordinal())
                {
                    return hand2;
                }
                indexContHand1--;
                indexContHand2--;
            }
        }

        return hand1;
    }

    /**
     * Method that decides which of two players, who both have a straight, has the best hand. The player with the
     * highest ranked card wins.
     *
     * @param hand1 Hand of five cards.
     * @param hand2 Hand of five cards.
     * @return The hand that contains the highest straight.
     */
    public Card[] decideBestStraight(Card[] hand1, Card[] hand2)
    {
        hand1 = insertionSort(hand1);
        hand2 = insertionSort(hand2);


        for(int i=4; i >= 0; i++) {
            if (hand1[i].getRank().ordinal() > hand2[i].getRank().ordinal()) {
                return hand1;
            } else if (hand1[i].getRank().ordinal() < hand2[i].getRank().ordinal()) {
                return hand2;
            }
        }

        return hand1;
    }


    /**
     * Method that checks if five cards make a straight. Also checking the special case with straight from ACE to FIVE.
     *
     * @param fiveCards The hand that may or may not make up a straight.
     * @return A boolean that is true if the cards make a straight and is false otherwise.
     */
    private boolean checkStraight(Card[] fiveCards)
    {

        /* Check if the cards make a straight. Also checking the special case with a straight from ACE to FIVE. */
        boolean gotStraight = false;

        if ( fiveCards[4].getRank().ordinal() == fiveCards[3].getRank().ordinal() + 1 &&
             fiveCards[3].getRank().ordinal() == fiveCards[2].getRank().ordinal() + 1 &&
             fiveCards[2].getRank().ordinal() == fiveCards[1].getRank().ordinal() + 1 &&
             fiveCards[1].getRank().ordinal() == fiveCards[0].getRank().ordinal() + 1)
        {
            gotStraight = true;

        }
        else if ( Card.Rank.TWO   == fiveCards[0].getRank() &&
                  Card.Rank.THREE == fiveCards[1].getRank() &&
                  Card.Rank.FOUR  == fiveCards[2].getRank() &&
                  Card.Rank.FIVE  == fiveCards[3].getRank() &&
                  Card.Rank.ACE   == fiveCards[4].getRank() )
        {
            gotStraight = true;
        }

        return gotStraight;
    }

    /**
     * Method that checks if five cards make a flush.
     *
     * @param fiveCards The hand that may or may not make up a flush.
     * @return A boolean that is true if the cards make a flush and is false otherwise.
     */
    private boolean checkFlush(Card[] fiveCards)
    {

        boolean gotFlush = ( fiveCards[0].getSuit() == fiveCards[1].getSuit() &&
                             fiveCards[0].getSuit() == fiveCards[2].getSuit() &&
                             fiveCards[0].getSuit() == fiveCards[3].getSuit() &&
                             fiveCards[0].getSuit() == fiveCards[4].getSuit() );

        return gotFlush;
    }

    /**
     * Method that checks if five cards have four of a kind.
     *
     * @param fiveCards The hand that may or may not have four of a kind.
     * @return A boolean that is true if the cards have four of a kind and is false otherwise.
     */
    private boolean checkFourOfAKind(Card[] fiveCards)
    {

        boolean gotFourOfAKind = false;

        for (int i = 0; i < 5; i++)
        {
            if (fiveCards[i % 5].getRank() == fiveCards[(i + 1) % 5].getRank() &&
                    fiveCards[(i + 1) % 5].getRank() == fiveCards[(i + 2) % 5].getRank() &&
                    fiveCards[(i + 2) % 5].getRank() == fiveCards[(i + 3) % 5].getRank())
            {
                gotFourOfAKind = true;
                break;
            }
        }

        return gotFourOfAKind;
    }

    /**
     * Method that checks if five cards make up a full house.
     *
     * @param fiveCards The hand that may or may not make up a full house.
     * @return A boolean that is true if the cards make up a full house and is false otherwise.
     */
    private boolean checkFullHouse(Card[] fiveCards)
    {


        boolean gotFullHouse = ( (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                  fiveCards[1].getRank() == fiveCards[2].getRank()  &&
                                  fiveCards[2].getRank() != fiveCards[3].getRank()  &&
                                  fiveCards[3].getRank() == fiveCards[4].getRank()) ||

                                 (fiveCards[0].getRank() == fiveCards[1].getRank()  &&
                                  fiveCards[1].getRank() != fiveCards[2].getRank()  &&
                                  fiveCards[2].getRank() == fiveCards[3].getRank()  &&
                                  fiveCards[3].getRank() == fiveCards[4].getRank()) );

        return gotFullHouse;
    }

    /**
     * Method that checks if five cards have three of a kind.
     *
     * @param fiveCards The hand that may or may not have three of a kind.
     * @return A boolean that is true if the cards have three of a kind and is false otherwise.
     */
    private boolean checkThreeOfAKind(Card[] fiveCards)
    {

        boolean gotThreeOfAKind = false;

        /* For loop to permute all the possible combination to get three of a kind */
        for (int i = 0; i < 3; i++)
        {

            if (fiveCards[(i % 5)].getRank() == fiveCards[(i + 1) % 5].getRank() &&
                    fiveCards[(i + 1) % 5].getRank() == fiveCards[(i + 2) % 5].getRank())
            {
                gotThreeOfAKind = true;
                break;
            }
        }

        return gotThreeOfAKind;
    }

    /**
     * Method that checks if there are two pairs among five cards.
     *
     * @param fiveCards The hand that may or may not contain two pairs.
     * @return A boolean that is true if the cards contain two pairs and is false otherwise.
     */
    private boolean checkTwoPair(Card[] fiveCards)
    {

        boolean gotTwoPair = ((fiveCards[0].getRank() == fiveCards[1].getRank() &&
                fiveCards[2].getRank() == fiveCards[3].getRank()) ||

                (fiveCards[1].getRank() == fiveCards[2].getRank() &&
                        fiveCards[3].getRank() == fiveCards[4].getRank()) ||

                (fiveCards[0].getRank() == fiveCards[1].getRank() &&
                        fiveCards[3].getRank() == fiveCards[4].getRank()));

        return gotTwoPair;
    }

    /**
     * Method that checks if there is a pair among five cards.
     *
     * @param fiveCards The hand that may or may not contain a pair.
     * @return A boolean that is true if the cards contain a pair and is false otherwise.
     */
    private boolean checkPair(Card[] fiveCards)
    {

        boolean gotPair = (fiveCards[0].getRank() == fiveCards[1].getRank() ||
                fiveCards[1].getRank() == fiveCards[2].getRank() ||
                fiveCards[2].getRank() == fiveCards[3].getRank() ||
                fiveCards[3].getRank() == fiveCards[4].getRank());

        return gotPair;
    }

/*
    // This code is for testing purposes:
    public static void main(String[] args) {

        Card[] testHand1 = new Card[5];
        Card[] testHand2 = new Card[5];
        Card[] returnHand = new Card[5];


        testHand1[0] = new Card(Card.Suit.DIAMONDS, Card.Rank.SIX, null);
        testHand1[1] = new Card(Card.Suit.SPADES, Card.Rank.ACE, null);
        testHand1[2] = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE, null);
        testHand1[3] = new Card(Card.Suit.SPADES, Card.Rank.TEN, null);
        testHand1[4] = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN, null);

        testHand2[0] = new Card(Card.Suit.HEARTS, Card.Rank.NINE, null);
        testHand2[1] = new Card(Card.Suit.CLUBS, Card.Rank.ACE, null);
        testHand2[2] = new Card(Card.Suit.HEARTS, Card.Rank.ACE, null);
        testHand2[3] = new Card(Card.Suit.CLUBS, Card.Rank.TEN, null);
        testHand2[4] = new Card(Card.Suit.HEARTS, Card.Rank.TEN, null);

        PokerRules p = new PokerRules();
        System.out.println("Hand rank 1 " + p.determineHandRank(testHand1));
        System.out.println("Hand rank 2 " + p.determineHandRank(testHand2));
        returnHand = p.decideBestTwoPair(testHand1,testHand2);
        System.out.println("Best hand: ");
        for(Card card : returnHand){
            System.out.println(card.getRank() + " " + card.getSuit());
        }


    } */
}
