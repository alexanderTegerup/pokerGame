package managers;

import calculations.PokerRules;
import common.Hand;
import common.Observer;
import common.States;
import player.Players;
import table.Card;
import table.Table;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A manager which holds a game session where players (users) play a game of poker
 */

public class GameManager
{
    private boolean playersession = true, playerHavePlayed = false, newRound;

    private double bigblind, smallblind;
    private double pot = 0, round = 0, raise = 0;
    //private PrintWriter out;
    //private BufferedReader in;
    private int amountOfPlayers, dealer;
    private /*static*/ int playerTurn;
    private int initialBigID;
    private int initialSmallID = 0;

    private int[] playerIDs;
    private States[] stateOfPlayersArr;
    private String[] playerNames;
    private Hand[] playerHands;
    private double[] playerBets;
    private Card[] tableCards;

    private Players playersObj;
    private States minimumState = States.SMALL;
    private Table table;
    private PokerRules pokerRules;

    ArrayList<Observer> players;
    ArrayList<Integer> playersLeftInTheGame;

    /**
     * Constructor for GameManager class
     *
     * @param playersObj using instance of players to notify the playersObj of a related change in the gamemanager
     */
    public GameManager(/*PrintWriter o, BufferedReader i, */Players playersObj)
    {
        this.playersObj = playersObj;
        //   in = i;
        //   out = o;
        playerTurn = 0;
        bigblind = 10;
        smallblind = 5;

        playersLeftInTheGame = new ArrayList<>();
        pokerRules = new PokerRules();
    }

    /**
     * initialize variables with necessary information from the players object
     */
    public void initilizePlayersObj()
    {
        amountOfPlayers = playersObj.getCurrentAmount();
        players = playersObj.getPlayers();

        playerNames = new String[amountOfPlayers];
        playerIDs = new int[amountOfPlayers];
        playerHands = new Hand[amountOfPlayers];
        playerBets = new double[amountOfPlayers];
    }

    /**
     * The game which is being playerBets between players through the server
     */
    public void playingTheGame()
    {

        boolean foldedWinner = false;
        int foldedWinnerID = 999;

        for (int p = 0; p < playerIDs.length; p++)
        {
            playerIDs[p] = players.get(p).getObserverID();
            playerNames[p] = players.get(p).getUserName();
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
                    else if (stateOfPlayersArr[playerIDs[(playerTurn % playerIDs.length)]] == States.FOLD ||
                             stateOfPlayersArr[playerIDs[(playerTurn % playerIDs.length)]] == States.ALL_IN)
                    {
                        playerTurn++;
                    }
                    else if (!playersLeftInTheGame.contains(playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]))
                    {
                        incFirstPlayer++;
                    }
                    else if (stateOfPlayersArr[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == States.FOLD ||
                             stateOfPlayersArr[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == States.ALL_IN)
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

                if (minimumState != States.BIG && minimumState != States.SMALL)
                {
                    if (playerBets[playerIDs[(playerTurn + incFirstPlayer) % playerIDs.length]] == raise &&
                        playerBets[playerIDs[playerTurn % playerIDs.length]]                    == raise )
                    {
                        minimumState = States.CHECK;
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
                    if (minimumState == States.SMALL)
                    {
                        minimumState = States.BIG;
                        playerTurn += 1;
                    }
                    else if (minimumState == States.BIG)
                    {
                        minimumState = States.RAISE;
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
                                if (stateOfPlayersArr[playerIDs[j]] != States.FOLD)
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
    private void updateDealerBigandSmall()
    {
        playerTurn = (playerTurn % playerIDs.length);
        int exist = 0;
        int incBig = 1;
        int incSmall = 1;
        do
        {
            if (!playersLeftInTheGame.contains(playerTurn % playerIDs.length))
            {
                playerTurn = ((playerTurn + 1) % playerIDs.length);
            }
            else if (!playersLeftInTheGame.contains((playerTurn + incBig) % playerIDs.length))
            {
                incBig++;
            }
            else if (!playersLeftInTheGame.contains((playerTurn + incBig + incSmall) % playerIDs.length))
            {
                incSmall++;
            }
            else
            {
                exist = 1;
            }
        }
        while (exist == 0);

        initialBigID = playerIDs[(playerTurn + 2) % playerIDs.length];
        for (Observer observer : players)
        {
            observer.updateDealerBigSmalBlinds(playerIDs[playerTurn], playerIDs[(playerTurn + 1) % playerIDs.length], playerIDs[(playerTurn + 2) % playerIDs.length], smallblind, bigblind);
        }
        playerTurn += 1;
    }

    /**
     * Update the collected pot and minimum required starte with bets collected from a players move
     *
     * @param ID     - of specific player
     * @param player - name of specific player who made a move
     * @param bets   - bets of player who made a move
     * @param move   - state of the player which made a move
     */
    public void severUpdatePot(int ID, String player, double bets, States move)
    {
        //pot += bets;

        System.out.println(ID + " " + player + " " + bets + " " + move);

        playerHavePlayed = true;

        //if player choose to raise
        if (move == States.RAISE)
        {
            if (raise < (playerBets[ID] + bets))
            {
                raise = (playerBets[ID] + bets);
                pot += bets;
                stateOfPlayersArr[ID] = States.RAISE;
                minimumState = States.RAISE;
                playerBets[ID] += bets;
            }

            if (initialBigID == ID)
            {
                initialBigID = 999;
            }
            //if player choose to go allin
        }
        else if (move == States.ALL_IN)
        {
            pot += bets;
            playerBets[ID] += bets;
            stateOfPlayersArr[ID] = States.ALL_IN;
            raise = (playerBets[ID] + bets);
            int count = 0;
            for (int i = 0; i < playerBets.length; i++)
            {
                if (playerBets[i] == raise || stateOfPlayersArr[i] == States.FOLD || stateOfPlayersArr[i] == States.ALL_IN)
                {
                    if (stateOfPlayersArr[i] != States.GO)
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
        else if (move == States.CALL)
        {
            if (raise == (playerBets[ID] + bets))
            {
                pot += bets;
                playerBets[ID] += bets;
                stateOfPlayersArr[ID] = States.CALL;
                int count = 0;
                for (int j = 0; j < playerBets.length; j++)
                {
                    if (playerBets[j] == raise || stateOfPlayersArr[j] == States.FOLD || stateOfPlayersArr[j] == States.ALL_IN)
                    {
                        if (stateOfPlayersArr[j] != States.GO)
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
        else if (move == States.CHECK)
        {
            int count = 0;
            minimumState = States.CHECK;
            stateOfPlayersArr[ID] = States.CHECK;
            if (initialBigID == ID)
            {
                initialBigID = 999;
                newRound = true;
            }
            for (int j = 0; j < playerBets.length; j++)
            {
                if (stateOfPlayersArr[j] == States.GO)
                {
                    //do nothing
                }
                else if (playerBets[j] == raise || stateOfPlayersArr[j] == States.FOLD || stateOfPlayersArr[j] == States.ALL_IN)
                {
                    if (stateOfPlayersArr[j] != States.GO)
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
        else if (move == States.FOLD)
        {
            int count = 0;
            stateOfPlayersArr[ID] = States.FOLD;
            if (initialBigID == ID)
            {
                initialBigID = 999;
            }
            for (int j = 0; j < playerBets.length; j++)
            {
                if (playerBets[j] == raise || stateOfPlayersArr[j] == States.FOLD || stateOfPlayersArr[j] == States.ALL_IN)
                {
                    if (stateOfPlayersArr[j] != States.GO)
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
        else if (move == States.BIG)
        {
            pot += bets;
            playerBets[ID] += bets;
            raise = bets;
            newRound = true;

            //if the player is smallblind and bets for the first time
        }
        else if (move == States.SMALL)
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
            if (stateOfPlayersArr[i] == States.FOLD)
            {
                count++;
            }
        }

        if (count == amountOfPlayers)
        {
            newRound = true;
        }
    }

    /**
     * Deal two cards to each player
     */
    private void dealHandsToPlayers()
    {
        for (common.Observer observer : players)
        {
            observer.dealCards(table.getCardFromDeck(), table.getCardFromDeck());
        }
    }

    /**
     * clean up the array responsible for tracking the state of players
     */
    private void cleanStateArray()
    {
        stateOfPlayersArr = new States[playerIDs.length];
        Arrays.fill(stateOfPlayersArr, States.GO);

        for (int i = 0; i < playerIDs.length; i++)
        {
            //stateOfPlayersArr[playerIDs[i]] = States.GO;
            playerBets[playerIDs[i]] = 0;
        }
    }

    /**
     * Clean the array responsible for tracking players bets
     */
    private void cleanPlayedArray()
    {
        for (int i = 0; i < playerIDs.length; i++)
        {
            if (stateOfPlayersArr[playerIDs[i]] != States.ALL_IN || stateOfPlayersArr[playerIDs[i]] != States.FOLD)
            {
                playerBets[playerIDs[i]] = 0;
            }
        }
    }

    /**
     * Reset the states of players who still can play turns
     */
    private void resetNotFoldedOrAllInArray()
    {
        for (int i = 0; i < playerIDs.length; i++)
        {
            if (stateOfPlayersArr[playerIDs[i]] != States.FOLD && stateOfPlayersArr[playerIDs[i]] != States.ALL_IN)
            {
                stateOfPlayersArr[playerIDs[i]] = States.GO;
            }
        }
    }

    /**
     * Collecting all the hand from players still not folded, to calculate who wins
     *
     * @param ID
     * @param hand
     */
    public void collectHandsFromPlayers(int ID, Hand hand)
    {
        if (stateOfPlayersArr[ID] != States.FOLD)
        {
            playerHands[ID] = hand;
        }
    }

    /**
     * Check if all players still in the game are all in, then proceed with showing cards and calculate winner
     *
     * @return
     */
    private boolean allAreFoldedOrAllIn()
    {
        int count = 0;
        for (int i = 0; i < playerIDs.length; i++)
        {
            if (stateOfPlayersArr[playerIDs[i]] == States.FOLD || stateOfPlayersArr[playerIDs[i]] == States.ALL_IN)
            {
                count++;
            }
        }

        if (count == amountOfPlayers)
        {
            return true;
        }
        return false;
    }
}