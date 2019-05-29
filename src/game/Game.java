package game;

import player.Hand;
//import common.*;
import player.Player;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

import common.Card;

public class Game
{
    private double bigBlindCost;
    private double smallBlindCost;
    private int iD = 0;
    private double wealth = 1000;
    //private int maxAmountPlayers;

    private PokerRules pokerRules;

    private Card[] tableCards;

    ArrayList<Player> players;
    
    
    /** Pokerrules Globals */
    int numberOfPlayers = players.size();


    public void login()
    {
        Scanner nameScan = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = nameScan.nextLine();

        players.add(new Player(name, wealth, iD++));

    }


    private void play() 
    {
        // TODO Auto-generated method stub
    }


    /**
     * Determines which player who has the best hand.
     *
     * @return The ID(s) of a player/players with the best hand.
     */
    public int[] determineBestHand(Hand[] arrHands, Card[] tableCards)
    {
        /* Preparations */
        cardsOnTable = tableCards;

        highestCardStraight = new Card[numberOfPlayers + 1];
        for (int i = 0; i < numberOfPlayers + 1; i++)
        {
            highestCardStraight[i] = new Card(Card.Suit.CLUBS, Card.Rank.TWO, null);
        }


        /* Find out which poker hand there is on the table. */
        tableCardRank = determineHandRanking(cardsOnTable, numberOfPlayers);
        System.out.println("Cards on table " + tableCardRank);

        /* Set the best combination each player can have with their hands. */
        setPlayersBestRanking();

        common.HandRank rankPlayer;
        int playerWithBestHand = -1; // Make sure we don't get null at player zero
        common.HandRank highestRank = common.HandRank.NOTHING;
        for (int iPlayer = 0; iPlayer < numberOfPlayers; iPlayer++)
        {
                rankPlayer = players.get(iPlayer).getHand().getRank();
                /* Save the player with the highest rank this far in the for-loop */
                if (rankPlayer.ordinal() > highestRank.ordinal())
                {
                    highestRank = rankPlayer;
                    playerWithBestHand = iPlayer;
                }
                /* If several players have the same hand */
                else if ((rankPlayer == highestRank) && (iPlayer > 0)) //&& (playerWithBestHand != -1))
                {
                    switch (rankPlayer)
                    {
                    case NOTHING:
                        playerWithBestHand = decideHighestCard(playerWithBestHand, 
                                                               players.get(playerWithBestHand).getHand(),
                                                               iPlayer, 
                                                               players.get(iPlayer).getHand());
                        break;

                    case PAIR:

                        playerWithBestHand = decideBestPair(playerWithBestHand, 
                                                            players.get(playerWithBestHand).getHand(),
                                                            iPlayer, 
                                                            players.get(iPlayer).getHand());
                        break;

                    case TWO_PAIR:
                        playerWithBestHand = decideBestTwoPair(playerWithBestHand,
                                                               players.get(playerWithBestHand).getHand(),
                                                               iPlayer, 
                                                               players.get(iPlayer).getHand());
                        break;

                    case THREE_OF_A_KIND:
                        playerWithBestHand = decideBestThreeOfAKind(playerWithBestHand,
                                                                    players.get(playerWithBestHand).getHand(),
                                                                    iPlayer, 
                                                                    players.get(iPlayer).getHand());
                        break;

                    case STRAIGHT:
                        playerWithBestHand = decideBestStraight(playerWithBestHand, iPlayer);
                        break;

                    case FLUSH:
                    case FULL_HOUSE:
                    case FOUR_OF_A_KIND:
                    case STRAIGHT_FLUSH:
                    case ROYAL_FLUSH:
                    default:
                        break;
                    }
                }
                System.out.println("player " + iPlayer + ": " + players.get(iPlayer).getHand().getRank());
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
    private void setPlayersBestRanking()
    {

        common.HandRank rankPlayer;
        common.HandRank bestCombination;
        Card[] cardCombination = new Card[5];

        /* Decide the best combination each player can get */
        for (int indexPlayer = 0; indexPlayer < numberOfPlayers; indexPlayer++)
        {
            if (arrayWithHands[indexPlayer] != null)
            {
                bestCombination = common.HandRank.NOTHING;
                for (int i = 0; i < 20; i++)
                {

                    /* i=0:9 tests all combinations with two cards from the player and three cards from the table
                     *  (The number of combinations are 5 choose 3 = 10) */
                    if (i < 5)
                    {
                        cardCombination[0] = cardsOnTable[i % 5];
                        cardCombination[1] = cardsOnTable[(i + 1) % 5];
                        cardCombination[2] = cardsOnTable[(i + 2) % 5];
                        cardCombination[3] = arrayWithHands[indexPlayer].getCard1();
                        cardCombination[4] = arrayWithHands[indexPlayer].getCard2();
                    }
                    else if (i < 10)
                    {
                        cardCombination[0] = cardsOnTable[i % 5];
                        cardCombination[1] = cardsOnTable[(i + 1) % 5];
                        cardCombination[2] = cardsOnTable[(i + 3) % 5];
                        cardCombination[3] = arrayWithHands[indexPlayer].getCard1();
                        cardCombination[4] = arrayWithHands[indexPlayer].getCard2();
                    }
                    /* Test all combinations with the first card from the player and four cards from the table
                     *  (The number of combinations are 5 choose 4 = 5)*/
                    else if (i < 15)
                    {

                        cardCombination[0] = cardsOnTable[i % 5];
                        cardCombination[1] = cardsOnTable[(i + 1) % 5];
                        cardCombination[2] = cardsOnTable[(i + 2) % 5];
                        cardCombination[3] = cardsOnTable[(i + 3) % 5];
                        cardCombination[4] = arrayWithHands[indexPlayer].getCard1();
                    }
                    /* Test all combinations with the second card from the player and four cards from the table
                     *  (The number of combinations are 5 choose 4 = 5)*/
                    else
                    {

                        cardCombination[0] = cardsOnTable[i % 5];
                        cardCombination[1] = cardsOnTable[(i + 1) % 5];
                        cardCombination[2] = cardsOnTable[(i + 2) % 5];
                        cardCombination[3] = cardsOnTable[(i + 3) % 5];
                        cardCombination[4] = arrayWithHands[indexPlayer].getCard2();
                    }

                    /* Save the highest combination */
                    rankPlayer = determineHandRanking(cardCombination, indexPlayer);
                    if (rankPlayer.ordinal() > bestCombination.ordinal())
                    {
                        bestCombination = rankPlayer;
                    }
                }

                if (bestCombination.ordinal() > tableCardRank.ordinal())
                {
                    arrayWithHands[indexPlayer].setRank(bestCombination);
                }
                else
                {
                    arrayWithHands[indexPlayer].setRank(tableCardRank);
                    if ((common.HandRank.STRAIGHT == tableCardRank) &&
                            (highestCardStraight[indexPlayer].getRank().ordinal() <
                                    highestCardStraight[numberOfPlayers].getRank().ordinal()))
                    {
                        highestCardStraight[indexPlayer] = highestCardStraight[numberOfPlayers];
                    }
                }
            }
        }
    }

}


public static void main(String[] args)
{
    Game game = new Game();
    for (int i=0; i<5; i++)
    {
        game.login();
    }

    game.play();
}