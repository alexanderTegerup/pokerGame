package game;

import common.Card;
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
    // TODO Make determineBestHand take an array of hands and the cards on the table as arguments.

    /*
    public enum Ranks
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
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }*/


    /** The rank of the cards that are on the table. */
    private common.HandRank tableCardRank;
    /** The number of players participating in the game. */
    private int numberOfPlayers;
    /** The cards that lie on the table. */
    private Card[] cardsOnTable;
    /** An array where every element consist of the two unique cards that are given to each player. */
    private HoleCards[] arrayWithHands;

    /** If a player gets a straight, the highest card that makes up that straight is saved in highestCardStraight. If
     * the cards on the table makes up a straight, the highest card of that straight is saved at the last index in
     * highestCardStraight.
     */
    private Card[] highestCardStraight;

    /**
     * Determines which player who has the best hand.
     *
     * @return The ID(s) of a player/players with the best hand.
     */
    public int[] determineBestHand(Hand[] arrHands, Card[] tableCards)
    {
        /* Preparations */
        arrayWithHands = arrHands;
        numberOfPlayers = arrayWithHands.length;
        System.out.println("number of players are " + numberOfPlayers);

        cardsOnTable = tableCards;

        highestCardStraight = new Card[numberOfPlayers + 1];
        for (int i = 0; i < numberOfPlayers + 1; i++)
        {
            highestCardStraight[i] = new Card(Card.Suit.CLUBS, Card.Rank.TWO, null);
        }


        /* Find out which poker hand there is on the table. */
        tableCardRank = determineHandRanking(cardsOnTable, numberOfPlayers);
        System.out.println("Cards on table " + tableCardRank);


        common.HandRank rankPlayer;
        int playerWithBestHand = -1; // Make sure we don't get null at player zero
        common.HandRank highestRank = common.HandRank.NOTHING;
        for (int iPlayer = 0; iPlayer < numberOfPlayers; iPlayer++)
        {
            if (arrayWithHands[iPlayer] != null)
            {
                rankPlayer = arrayWithHands[iPlayer].getRank();
                /* Save the player with the highest rank this far in the for-loop */
                if (rankPlayer.ordinal() > highestRank.ordinal())
                {
                    highestRank = rankPlayer;
                    playerWithBestHand = iPlayer;
                }
                /* If several players have the same hand */
                else if ((rankPlayer == highestRank) && (iPlayer > 0) && (playerWithBestHand != -1))
                {
                    switch (rankPlayer)
                    {
                        case NOTHING:
                            playerWithBestHand = decideHighestCard(playerWithBestHand, arrayWithHands[playerWithBestHand],
                                    iPlayer, arrayWithHands[iPlayer]);
                            break;

                        case PAIR:

                            playerWithBestHand = decideBestPair(playerWithBestHand, arrayWithHands[playerWithBestHand],
                                    iPlayer, arrayWithHands[iPlayer]);
                            break;

                        case TWO_PAIR:
                            playerWithBestHand = decideBestTwoPair(playerWithBestHand,
                                    arrayWithHands[playerWithBestHand],
                                    iPlayer,
                                    arrayWithHands[iPlayer]);
                            break;

                        case THREE_OF_A_KIND:
                            playerWithBestHand = decideBestThreeOfAKind(playerWithBestHand,
                                    arrayWithHands[playerWithBestHand],
                                    iPlayer,
                                    arrayWithHands[iPlayer]);
                            break;

                        case STRAIGHT:
                            playerWithBestHand = decideBestStraight(playerWithBestHand, iPlayer);
                            break;

                        case FLUSH:
                        case FULL_HOUSE:
                        case FOUR_OF_A_KIND:
                        case STRAIGHT_FLUSH:
                    }
                }
                System.out.println("player " + iPlayer + ": " + arrayWithHands[iPlayer].getRank());
            }
        }

        System.out.println("The player with the best hand is player " + playerWithBestHand);

        int numberOfWinners = 1;
        int[] winners = new int[numberOfWinners];
        for (int i = 0; i < numberOfWinners; i++)
        {
            winners[i] = playerWithBestHand;
        }

        return winners;
    }

    /**
     * Method that computes the rank of each possible combination of the five cards on the table plus the two cards a
     * player has. The highest of those ranks is saved in the attribute 'rank' in the hand those two cards belong to.
     */
    public void setHandRank(Card[] communityCards, HoleCards holeCards)
    {
        cardsOnTable = communityCards;
        common.HandRank handRank;
        common.HandRank bestHandRank;
        Card[] cardCombination = new Card[5];


        bestHandRank = common.HandRank.NOTHING;
        for (int i = 0; i < 20; i++)
        {

            /* i=0:9 tests all combinations with two cards from the player and three cards from the table
             *  (The number of combinations are 5 choose 3 = 10) */
            if (i < 5)
            {
                cardCombination[0] = cardsOnTable[i % 5];
                cardCombination[1] = cardsOnTable[(i + 1) % 5];
                cardCombination[2] = cardsOnTable[(i + 2) % 5];
                cardCombination[3] = holeCards.getCard1();
                cardCombination[4] = holeCards.getCard2();
            }
            else if (i < 10)
            {
                cardCombination[0] = cardsOnTable[i % 5];
                cardCombination[1] = cardsOnTable[(i + 1) % 5];
                cardCombination[2] = cardsOnTable[(i + 3) % 5];
                cardCombination[3] = holeCards.getCard1();
                cardCombination[4] = holeCards.getCard2();
            }
            /* Test all combinations with the first card from the player and four cards from the table
             *  (The number of combinations are 5 choose 4 = 5)*/
            else if (i < 15)
            {

                cardCombination[0] = cardsOnTable[i % 5];
                cardCombination[1] = cardsOnTable[(i + 1) % 5];
                cardCombination[2] = cardsOnTable[(i + 2) % 5];
                cardCombination[3] = cardsOnTable[(i + 3) % 5];
                cardCombination[4] = holeCards.getCard1();
            }
            /* Test all combinations with the second card from the player and four cards from the table
             *  (The number of combinations are 5 choose 4 = 5)*/
            else
            {

                cardCombination[0] = cardsOnTable[i % 5];
                cardCombination[1] = cardsOnTable[(i + 1) % 5];
                cardCombination[2] = cardsOnTable[(i + 2) % 5];
                cardCombination[3] = cardsOnTable[(i + 3) % 5];
                cardCombination[4] = holeCards.getCard2();
            }

            /* Save the highest combination */
            handRank = determineHandRanking(cardCombination, indexPlayer);
            if (handRank.ordinal() > bestHandRank.ordinal())
            {
                bestHandRank = handRank;
            }
        }

        if (bestHandRank.ordinal() > tableCardRank.ordinal())
        {
            arrayWithHands[indexPlayer].setRank(bestHandRank);
        }
        else
        {
            arrayWithHands[indexPlayer].setRank(tableCardRank);
            if ((common.Ranks.STRAIGHT == tableCardRank) &&
                    (highestCardStraight[indexPlayer].getRank().ordinal() <
                            highestCardStraight[numberOfPlayers].getRank().ordinal()))
            {
                highestCardStraight[indexPlayer] = highestCardStraight[numberOfPlayers];
            }
        }

    }

    
    /**
     * Determines the highest possible rank of the five cards given as an input.
     * @param fiveCards An array containing five cards.
     * @param indexPlayer The index of the player the five cards belongs to.
     * @return The highest ranking of the five cards given as an input.
     */
    private common.HandRank determineHandRanking(Card[] fiveCards, int indexPlayer)
    {

        common.HandRank rankCards;

        /* Sort the cards so that checkStraight, checkFullHouse,
        checkThreeOfAKind, checkTwoPair and checkPair will work */
        fiveCards = insertionSort(fiveCards);

        if (checkStraight(fiveCards, indexPlayer) && checkFlush(fiveCards))
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
        else if (checkStraight(fiveCards, indexPlayer))
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

    /**
     * Method that takes the card a player has in its hand and add those cards to an array together with the five cards
     * on the table. This array is sorted from the card with the lowest to the highest rank.
     *
     * @param playersHand  The hand of a player.
     * @param cardsOnTable The five cards that are on the table.
     * @return The sorted array of seven cards.
     */
    private Card[] makeSortedArray7cards(HoleCards playersHand, Card[] cardsOnTable)
    {

        Card[] hand = new Card[7];

        for (int i = 0; i < 5; i++)
        {
            hand[i] = cardsOnTable[i];
        }

        hand[5] = playersHand.getCard1();
        hand[6] = playersHand.getCard2();

        hand = insertionSort(hand);

        return hand;
    }

    /**
     * Method that determines which one of two players who has the highest card, including the cards on the table.
     *
     * @param indexPlayer1 The index of the first player.
     * @param h1           The hand the first player has.
     * @param indexPlayer2 The index of the second player.
     * @param h2           The hand the second player has.
     * @return The index of the player who has the highest card. If they both have the exact same rank of the cards -1
     * is returned.
     */
    private int decideHighestCard(int indexPlayer1, HoleCards h1, int indexPlayer2, HoleCards h2)
    {

        Card[] hand1;
        Card[] hand2;

        hand1 = makeSortedArray7cards(h1, cardsOnTable);
        hand2 = makeSortedArray7cards(h2, cardsOnTable);

        for (int i = 0; i < 5; i++)
        {

            if (hand1[6 - i].getRank().ordinal() > hand2[6 - i].getRank().ordinal())
            {
                return indexPlayer1;
            }
            else if (hand1[6 - i].getRank().ordinal() < hand2[6 - i].getRank().ordinal())
            {
                return indexPlayer2;
            }
        }

        return -1;
    }

    /**
     * Method that decides which of two players, who both have two pair, has the best hand. If both players have the
     * same pair, the player with the highest of the three remaining cards will have the best hand.
     *
     * @param indexPlayer1 The index of the first player.
     * @param h1           The hand the first player has.
     * @param indexPlayer2 The index of the second player.
     * @param h2           The hand the second player has.
     * @return The index of the player who has the best hand. If both hands have the exact same rank, -1 is returned.
     */
    private int decideBestPair(int indexPlayer1, HoleCards h1, int indexPlayer2, HoleCards h2)
    {
        Card[] hand1;
        Card[] hand2;

        hand1 = makeSortedArray7cards(h1, cardsOnTable);
        hand2 = makeSortedArray7cards(h2, cardsOnTable);

        Card.Rank pairHand1 = hand1[0].getRank(); // They need to be initialized.
        Card.Rank pairHand2 = hand2[0].getRank(); // They need to be initialized.

        /* Saving the index where the cards that make up a pair are. */
        int idxPair1 = 0;
        int idxPair2 = 0;

        for (int i = 0; i < 6; i++)
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

        int idxHighestCard1 = 6;
        int idxHighestCard2 = 6;

        /* If hand 1 has the highest pair, return that players index. */
        if (pairHand1.ordinal() > pairHand2.ordinal())
        {
            return indexPlayer1;
        }
        /* If hand 2 has the highest pair, return that players index. */
        else if (pairHand1.ordinal() < pairHand2.ordinal())
        {
            return indexPlayer2;
        }
        /* If they have the same pair, return the index of the player with the highest
           card of their three remaining cards */
        else if (pairHand1 == pairHand2)
        {

            /* Loop through the three highest remaining cards for both players */
            for (int i = 6; i > 3; i--)
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
                    return indexPlayer1; // Player 1 wins because she/he has the same pair as player 2 but
                    // the highest card of the three remaining cards.
                }
                else if (hand1[idxHighestCard1].getRank().ordinal() < hand2[idxHighestCard2].getRank().ordinal())
                {
                    return indexPlayer2; // Player 2 wins because she/he has the same pair as player 1 but
                    // the highest card of the three remaining cards.
                }
                idxHighestCard1--;
                idxHighestCard2--;
            }
        }

        return -1;
    }


    /**
     * Method that decides which of two players, who both have two pair, has the best hand. The player with the highest
     * pair wins. If two players have the same two pair, then the fifth card kicker determines the winner.
     *
     * @param indexPlayer1 The index of the first player.
     * @param h1           The hand the first player has.
     * @param indexPlayer2 The index of the second player.
     * @param h2           The hand the second player has.
     * @return The index of the player who has the best hand. If they both have the exact same rank of the cards -1 is
     * returned.
     */
    private int decideBestTwoPair(int indexPlayer1, HoleCards h1, int indexPlayer2, HoleCards h2)
    {

        Card[] hand1;
        Card[] hand2;

        hand1 = makeSortedArray7cards(h1, cardsOnTable);
        hand2 = makeSortedArray7cards(h2, cardsOnTable);

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

        for (int i = 0; i < 6; i++)
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

        // Return the index of the player with the highest pair.
        if (highestPairHand1.ordinal() > highestPairHand2.ordinal())
        {
            return indexPlayer1;
        }
        else if (highestPairHand1.ordinal() < highestPairHand2.ordinal())
        {
            return indexPlayer2;
        }
        // If the two players have the same highest pair, decide which one that has the second highest pair.
        else if (highestPairHand1 == highestPairHand2)
        {

            if (lowestPairHand1.ordinal() > lowestPairHand2.ordinal())
            {
                return indexPlayer1;
            }
            else if (lowestPairHand1.ordinal() < lowestPairHand2.ordinal())
            {
                return indexPlayer2;
            }
            // If the two players have the same two pairs, the player with the highest kicker wins.
            else if (lowestPairHand1 == lowestPairHand2)
            {

                int indexContHand1 = 6;
                int indexContHand2 = 6;
                for (int i = 6; i > 3; i--)
                {

                    if ((i == idxHighestPairHand1) || (i == idxLowestPairHand1))
                    {
                        indexContHand1 -= 2;
                    }
                    if ((i == idxHighestPairHand2) || (i == idxLowestPairHand2))
                    {
                        indexContHand2 -= 2;
                    }

                    if (hand1[indexContHand1].getRank().ordinal() > hand2[indexContHand2].getRank().ordinal())
                    {
                        return indexPlayer1;
                    }
                    else if (hand1[indexContHand1].getRank().ordinal() < hand2[indexContHand2].getRank().ordinal())
                    {
                        return indexPlayer2;
                    }
                    indexContHand1--;
                    indexContHand2--;
                }
            }
        }

        return -1;
    }

    /**
     * Method that decides which of two players, who both have three of a kind, has the best hand. The player with the
     * highest ranked cards that makes up three of a kind wins. If two players have the same three of a kind, then the
     * highest of the two remaining card kickers determines the winner.
     *
     * @param indexPlayer1 The index of the first player.
     * @param h1           The hand the first player has.
     * @param indexPlayer2 The index of the second player.
     * @param h2           The hand the second player has.
     * @return The index of the player who has the best hand. If they both have the exact same rank of the cards -1 is
     * returned.
     */
    private int decideBestThreeOfAKind(int indexPlayer1, HoleCards h1, int indexPlayer2, HoleCards h2)
    {

        Card[] hand1;
        Card[] hand2;

        hand1 = makeSortedArray7cards(h1, cardsOnTable);
        hand2 = makeSortedArray7cards(h2, cardsOnTable);

        Card.Rank rankThreeOfAKindHand1 = hand1[0].getRank();
        Card.Rank rankThreeOfAKindHand2 = hand2[0].getRank();

        int indexThreeOfAKindHand1 = 0;
        int indexThreeOfAKindHand2 = 0;

        for (int i = 0; i < 5; i++)
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
            return indexPlayer1;
        }
        else if (rankThreeOfAKindHand1.ordinal() < rankThreeOfAKindHand2.ordinal())
        {
            return indexPlayer2;
        }
        // If the two players have the same three of a kind, the player with the highest kicker wins.
        else if (rankThreeOfAKindHand1 == rankThreeOfAKindHand2)
        {

            int indexContHand1 = 6;
            int indexContHand2 = 6;
            for (int i = 6; i > 4; i--)
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
                    return indexPlayer1;
                }
                else if (hand1[indexContHand1].getRank().ordinal() < hand2[indexContHand2].getRank().ordinal())
                {
                    return indexPlayer2;
                }
                indexContHand1--;
                indexContHand2--;
            }
        }

        return -1;
    }

    /**
     * Method that decides which of two players, who both have a straight, has the best hand. The player with the
     * highest ranked card wins.
     *
     * @param indexPlayer1 The index of the first player.
     * @param indexPlayer2 The index of the second player.
     * @return The index of the player who has the best hand. If they both have the exact same rank of the cards -1 is
     * returned.
     */
    private int decideBestStraight(int indexPlayer1, int indexPlayer2)
    {

        if (highestCardStraight[indexPlayer1].getRank().ordinal() >
                highestCardStraight[indexPlayer2].getRank().ordinal())
        {
            return indexPlayer1;
        }
        else if (highestCardStraight[indexPlayer1].getRank().ordinal() <
                highestCardStraight[indexPlayer2].getRank().ordinal())
        {
            return indexPlayer2;
        }

        return -1;
    }


    /**
     * Method that checks if five cards make a straight. Also checking the special case with straight from ACE to FIVE.
     *
     * @param fiveCards The hand that may or may not make up a straight.
     * @return A boolean that is true if the cards make a straight and is false otherwise.
     */
    private boolean checkStraight(Card[] fiveCards, int indexPlayer)
    {

        /* Check if the cards make a straight. Also checking the special case with a straight from ACE to FIVE. */
        boolean gotStraight = false;

        if (fiveCards[4].getRank().ordinal() == fiveCards[3].getRank().ordinal() + 1 &&
                fiveCards[3].getRank().ordinal() == fiveCards[2].getRank().ordinal() + 1 &&
                fiveCards[2].getRank().ordinal() == fiveCards[1].getRank().ordinal() + 1 &&
                fiveCards[1].getRank().ordinal() == fiveCards[0].getRank().ordinal() + 1)
        {
            gotStraight = true;
            if (highestCardStraight[indexPlayer].getRank().ordinal() < fiveCards[4].getRank().ordinal())
            {
                highestCardStraight[indexPlayer] = fiveCards[4];
            }

        }
        else if (Card.Rank.TWO == fiveCards[0].getRank() &&
                Card.Rank.THREE == fiveCards[1].getRank() &&
                Card.Rank.FOUR == fiveCards[2].getRank() &&
                Card.Rank.FIVE == fiveCards[3].getRank() &&
                Card.Rank.ACE == fiveCards[4].getRank())
        {
            gotStraight = true;
            if (highestCardStraight[indexPlayer].getRank().ordinal() < fiveCards[4].getRank().ordinal())
            {
                highestCardStraight[indexPlayer] = fiveCards[3];
            }
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

        boolean gotFlush = (fiveCards[0].getSuit() == fiveCards[1].getSuit() &&
                fiveCards[0].getSuit() == fiveCards[2].getSuit() &&
                fiveCards[0].getSuit() == fiveCards[3].getSuit() &&
                fiveCards[0].getSuit() == fiveCards[4].getSuit());

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


        boolean gotFullHouse = ((fiveCards[0].getRank() == fiveCards[1].getRank() &&
                fiveCards[1].getRank() == fiveCards[2].getRank() &&
                fiveCards[2].getRank() != fiveCards[3].getRank() &&
                fiveCards[3].getRank() == fiveCards[4].getRank()) ||

                (fiveCards[0].getRank() == fiveCards[1].getRank() &&
                        fiveCards[1].getRank() != fiveCards[2].getRank() &&
                        fiveCards[2].getRank() == fiveCards[3].getRank() &&
                        fiveCards[3].getRank() == fiveCards[4].getRank()));

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


    //public static void main(String[] args) {


    /* Test case */

        /*
        Card[] tableCards = new Card[5];
        common.HoleCards[] arrHands = new HoleCards[2];

        tableCards[0] = new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN, null);
        tableCards[1] = new Card(Card.Suit.DIAMONDS, Card.Rank.SIX, null);
        tableCards[2] = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE, null);
        tableCards[3] = new Card(Card.Suit.HEARTS, Card.Rank.FOUR, null);
        tableCards[4] = new Card(Card.Suit.CLUBS, Card.Rank.THREE, null);


        Card p1c1 = new Card(Card.Suit.DIAMONDS, Card.Rank.NINE, null);
        Card p1c2 = new Card(Card.Suit.CLUBS, Card.Rank.EIGHT, null);

        Card p2c1 = new Card(Card.Suit.HEARTS, Card.Rank.TEN, null);
        Card p2c2 = new Card(Card.Suit.CLUBS, Card.Rank.EIGHT, null);

        common.HoleCards hand1 = new HoleCards(p1c1, p1c2);


        common.HoleCards hand2 = new HoleCards(p2c1, p2c2);


        arrHands[0] = hand1;
        arrHands[1] = hand2;

        PokerRules p = new PokerRules();
        int[] a; 
        a = p.determineBestHand(arrHands, tableCards );
        System.out.println(a[0]);*/


    //}
}
