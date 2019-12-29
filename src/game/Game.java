package game;

import common.PlayerMove;
import player.HoleCards;
//import common.*;
import player.Player;
import remove_later.Observer;
import remove_later.Table;

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

    
    /**
     * receives two cards from the game manager to build a playing hand
     * Should maybe be a part of play method?
     * @param card1
     * @param card2
     */
    public void dealCards(Card card1, Card card2)
    {
        hand = new HoleCards(card1, card2);

        System.out.println(name);
        System.out.println("Card 1: " + hand.getCard1().getRank() + " " + hand.getCard1().getSuit());
        System.out.println("Card 2: " + hand.getCard2().getRank() + " " + hand.getCard2().getSuit() + "\n");
    }
    
    

    private void play() 
    {
        // TODO Auto-generated method stub
        // TO DO move "playingTheGame" into here


        /**
         * The game which is being playerBets between players through the server**/

            boolean foldedWinner = false;
            int foldedWinnerID = 999;

            for (int p = 0; p < playerIDs.length; p++)
            {
                playerIDs[p] = players.get(p).getObserverID();
                playerNames[p] = players.get(p).getName();
                playersLeftInTheGame.add(players.get(p).getObserverID());
            }

            cleanStateArray();
            cleanPlayedArray();

            raise = smallblind;
            table = new Table();

            updateDealerBigandSmall();
            //dealHandsToPlayers();

            while (round < 5)
            {
                newRound = false;

                // Deal one or three cards to the table, depending on if it is the 
                // flop, turn or the river.
                if (round >= 2)
                {
                    table.dealCard();
                    tableCards = table.showAllCards();
                    int tmpAmount = table.returnNrOfCards();
                    for (Observer player : players)
                    {
                        player.flipOfCardT(tableCards, tmpAmount);
                    }
                }
                while
                (playersession)
                {

                    if (allAreFoldedOrAllIn())
                    {
                        round++;
                        break;
                    }

                    int exist = 0;
                    int incFirstPlayer = 1;
                    do
                    {
                        if (!playersLeftInTheGame.contains(playerIDs[(playerTurn % playerIDs.length)]))
                        {
                            playerTurn++;
                        }
                        else if (stateOfPlayersArr[playerIDs[(playerTurn % playerIDs.length)]] == PlayerMove.FOLD ||
                                 stateOfPlayersArr[playerIDs[(playerTurn % playerIDs.length)]] == PlayerMove.ALL_IN)
                        {
                            playerTurn++;
                        }
                        else if (!playersLeftInTheGame.contains(playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]))
                        {
                            incFirstPlayer++;
                        }
                        else if (stateOfPlayersArr[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == PlayerMove.FOLD ||
                                 stateOfPlayersArr[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == PlayerMove.ALL_IN)
                        {
                            incFirstPlayer++;
                        }
                        else
                        {
                            playerTurn = (playerTurn % playerIDs.length);
                            exist = 1;
                        }
                    }
                    while (exist == 0);

                    if (minimumState != PlayerMove.BIG && minimumState != PlayerMove.SMALL)
                    {
                        if (playerBets[playerIDs[(playerTurn + incFirstPlayer) % playerIDs.length]] == raise &&
                            playerBets[playerIDs[playerTurn % playerIDs.length]]                    == raise )
                        {
                            minimumState = PlayerMove.CHECK;
                        }
                    }
                    for (Observer observer : players)
                    {
                        observer.updateTurnAndOptions(playerIDs[playerTurn % playerIDs.length],
                                                      playerNames[playerIDs[playerTurn % playerIDs.length]],
                                                      minimumState,
                                                      (raise - (playerBets[playerIDs[playerTurn % playerIDs.length]])));
                    }
                    //saved for implementation of client-server solution
                    /*   for (int i = 0; i < 10; i++) {
                        if (!playerHavePlayed && i < 10) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } 
                        } else if (!playerHavePlayed) {
                            players.get((playerTurn + 1) % amountOfPlayers).foldRequestFromServer();
                            foldedPlayers[((playerTurn + 1) % amountOfPlayers)] = true;
                            playerTurn += 1;
                        } else {
                            if (minimumState == States.BIG) {
                                minimumState = States.SMALL;
                                playerTurn += 1;
                            } else if (minimumState == States.SMALL) {
                                minimumState = States.RAISE;
                                dealHandsToPlayers();
                            } else {
                                playerTurn += 1;
                            }
                            break;
                        }
                    }*/

                    if (playerHavePlayed)
                    {
                        if (minimumState == PlayerMove.SMALL)
                        {
                            minimumState = PlayerMove.BIG;
                            playerTurn += 1;
                        }
                        else if (minimumState == PlayerMove.BIG)
                        {
                            minimumState = PlayerMove.RAISE;
                            dealHandsToPlayers();
                            playerTurn += 1;
                        }
                        else
                        {
                            playerTurn += 1;
                        }

                        playerHavePlayed = false;

                        if (newRound)
                        {
                            round++;
                            if (round == 5)
                            {
                                newRound = false;
                            }
                            else
                            {
                                int notFolded = 0;
                                for (int j = 0; j < playerIDs.length; j++)
                                {
                                    if (stateOfPlayersArr[playerIDs[j]] != PlayerMove.FOLD)
                                    {
                                        notFolded++;
                                        foldedWinnerID = playerIDs[j];
                                    }
                                }
                                if (notFolded == 1)
                                {
                                    newRound = false;
                                    round = 5;
                                    foldedWinner = true;
                                }
                            }
                            if (round > 1)
                            {
                                playerTurn = initialSmallID;
                            }

                            resetNotFoldedOrAllInArray();
                            break;
                        }
                    }
                }
            }
            if (newRound == false && foldedWinner == false)
            {
                calculateWinnerAndPot();
            }
            else if (newRound == false && foldedWinner == true)
            {
                calculateWinnerFoldPot(foldedWinnerID);
            }
        }
        


    /**
     * Determines which player who has the best hand.
     *
     * @return The ID(s) of a player/players with the best hand.
     */
    public int[] determineBestHand(HoleCards[] arrHands, Card[] tableCards)
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
                rankPlayer = players.get(iPlayer).getHoleCards().getRank();
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
                                                               players.get(playerWithBestHand).getHoleCards(),
                                                               iPlayer, 
                                                               players.get(iPlayer).getHoleCards());
                        break;

                    case PAIR:

                        playerWithBestHand = decideBestPair(playerWithBestHand, 
                                                            players.get(playerWithBestHand).getHoleCards(),
                                                            iPlayer, 
                                                            players.get(iPlayer).getHoleCards());
                        break;

                    case TWO_PAIR:
                        playerWithBestHand = decideBestTwoPair(playerWithBestHand,
                                                               players.get(playerWithBestHand).getHoleCards(),
                                                               iPlayer, 
                                                               players.get(iPlayer).getHoleCards());
                        break;

                    case THREE_OF_A_KIND:
                        playerWithBestHand = decideBestThreeOfAKind(playerWithBestHand,
                                                                    players.get(playerWithBestHand).getHoleCards(),
                                                                    iPlayer, 
                                                                    players.get(iPlayer).getHoleCards());
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
                System.out.println("player " + iPlayer + ": " + players.get(iPlayer).getHoleCards().getRank());
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


    
    
    /**
     * Decide who wins and how much if rest of players have folded
     *
     * @param ID
     */
    private void calculateWinnerFoldPot(int ID)
    {
        String[] winnerName = new String[1];
        int[] winnerId = new int[1];
        winnerId[0] = ID;
        winnerName[0] = playerNames[winnerId[0]];

        for (Observer player : players)
        {
            player.updateWinner(winnerId, winnerName, pot);
        }
    }
    
    
    /**
     * Calculation of who wins and how much based on the information sent to the PokerRules class
     */
    private void calculateWinnerAndPot()
    {

        for (Observer player : players)
        {
            player.getHand();
        }

        int winnerId[] = pokerRules.determineBestHand(playerHands, tableCards);
        String[] winnerNames = new String[winnerId.length];

        for (int i = 0; i < winnerId.length; i++)
        {
            winnerNames[i] = playerNames[winnerId[i]];
        }

        for (Observer player : players)
        {
            player.updateWinner(winnerId, winnerNames, pot);
        }
    }
    
    
    /**
     * Notify players at the beginning of the round who will act as dealer, small blind or big blind
     */
    private void notifyPlayerMoves(){
        //TO DO
    }
    
    /**
     * Update the collected pot and minimum required state with bets collected from a players move
     *
     * @param ID     - of specific player
     * @param player - name of specific player who made a move
     * @param bets   - bets of player who made a move
     * @param move   - state of the player which made a move
     */
    public static void severUpdatePot(int ID, String player, double bets, PlayerMove move)
    {
        //pot += bets;

        System.out.println(ID + " " + player + " " + bets + " " + move);

        playerHavePlayed = true;

        //if player choose to raise
        if (move == PlayerMove.RAISE)
        {
            if (raise < (playerBets[ID] + bets))
            {
                raise = (playerBets[ID] + bets);
                pot += bets;
                stateOfPlayersArr[ID] = PlayerMove.RAISE;
                minimumState = PlayerMove.RAISE;
                playerBets[ID] += bets;
            }

            if (initialBigID == ID)
            {
                initialBigID = 999;
            }
            //if player choose to go allin
        }
        else if (move == PlayerMove.ALL_IN)
        {
            pot += bets;
            playerBets[ID] += bets;
            stateOfPlayersArr[ID] = PlayerMove.ALL_IN;
            raise = (playerBets[ID] + bets);
            int count = 0;
            for (int i = 0; i < playerBets.length; i++)
            {
                if (playerBets[i] == raise || stateOfPlayersArr[i] == PlayerMove.FOLD || stateOfPlayersArr[i] == PlayerMove.ALL_IN)
                {
                    if (stateOfPlayersArr[i] != PlayerMove.GO)
                    {
                        count++;
                    }
                }
            }

            if (count == amountOfPlayers)
            {
                newRound = true;
            }

            if (initialBigID == ID)
            {
                initialBigID = 999;
            }
            //if player choose to call
        }
        else if (move == PlayerMove.CALL)
        {
            if (raise == (playerBets[ID] + bets))
            {
                pot += bets;
                playerBets[ID] += bets;
                stateOfPlayersArr[ID] = PlayerMove.CALL;
                int count = 0;
                for (int j = 0; j < playerBets.length; j++)
                {
                    if (playerBets[j] == raise || stateOfPlayersArr[j] == PlayerMove.FOLD || stateOfPlayersArr[j] == PlayerMove.ALL_IN)
                    {
                        if (stateOfPlayersArr[j] != PlayerMove.GO)
                        {
                            count++;
                        }
                    }
                }

                if (count == amountOfPlayers)
                {
                    newRound = true;
                }

                if (initialBigID == ID)
                {
                    initialBigID = 999;
                }
            }

            //if player choose to check
        }
        else if (move == PlayerMove.CHECK)
        {
            int count = 0;
            minimumState = PlayerMove.CHECK;
            stateOfPlayersArr[ID] = PlayerMove.CHECK;
            if (initialBigID == ID)
            {
                initialBigID = 999;
                newRound = true;
            }
            for (int j = 0; j < playerBets.length; j++)
            {
                if (stateOfPlayersArr[j] == PlayerMove.GO)
                {
                    //do nothing
                }
                else if (playerBets[j] == raise || stateOfPlayersArr[j] == PlayerMove.FOLD || stateOfPlayersArr[j] == PlayerMove.ALL_IN)
                {
                    if (stateOfPlayersArr[j] != PlayerMove.GO)
                    {
                        count++;
                    }
                }
            }
            if (count == amountOfPlayers)
            {
                newRound = true;
            }

            ////if player choose to fold
        }
        else if (move == PlayerMove.FOLD)
        {
            int count = 0;
            stateOfPlayersArr[ID] = PlayerMove.FOLD;
            if (initialBigID == ID)
            {
                initialBigID = 999;
            }
            for (int j = 0; j < playerBets.length; j++)
            {
                if (playerBets[j] == raise || stateOfPlayersArr[j] == PlayerMove.FOLD || stateOfPlayersArr[j] == PlayerMove.ALL_IN)
                {
                    if (stateOfPlayersArr[j] != PlayerMove.GO)
                    {
                        count++;
                    }
                }
                System.out.println(playerBets[j] + " " + stateOfPlayersArr[j] + " " + raise);
            }

            if (count == amountOfPlayers)
            {
                newRound = true;
            }
            //if the player is bigblind and bets for the first time
        }
        else if (move == PlayerMove.BIG)
        {
            pot += bets;
            playerBets[ID] += bets;
            raise = bets;
            newRound = true;

            //if the player is smallblind and bets for the first time
        }
        else if (move == PlayerMove.SMALL)
        {
            pot += bets;
            playerBets[ID] += bets;
            initialSmallID = ID;
        }

        for (Observer observer : players)
        {
            observer.updateLastPlayersMove(player, move);
        }

        int count = 1;

        for (int i = 0; i < playerBets.length; i++)
        {
            if (stateOfPlayersArr[i] == PlayerMove.FOLD)
            {
                count++;
            }
        }

        if (count == amountOfPlayers)
        {
            newRound = true;
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