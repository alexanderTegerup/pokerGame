package managers;

import common.Observer;
import common.States;
import player.Players;
import table.Card;
import table.Table;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A manager which holds a game session where players (users) play a game of poker
 */

public class GameManager
{
    private boolean playersession = true, playerHavePlayed = false, newRound;

    private double bigblind, smallblind;
    private double pot = 0, round = 1, raise = 0;
//    private PrintWriter out;
//    private BufferedReader in;
    private int amountOfPlayers, dealer;
    private /*static*/ int playerTurn;

    private int[] playerIDs;
    private States[] foldedOrAllInBets;
    private String[] playerNames;
    private Hand[] playerHands;
    private double[] playerBets;

    private Players playersObj;
    private States minimumState = States.BIG;
    private Table table;

    ArrayList<Observer> players;

    /**
     * Constructor for GameManager class
     * @param playersObj using instance of players to notify the playersObj of the changes in the gamemanager
     */
    public GameManager(/*PrintWriter o, BufferedReader i, */Players playersObj)
    {
        this.playersObj = playersObj;
        amountOfPlayers = playersObj.getCurrentAmount();
     //   in = i;
     //   out = o;
        playerTurn = 0;
        bigblind = 100;
        smallblind = 50;
        players = playersObj.getPlayers();
        playerNames = new String[amountOfPlayers];
        playerIDs = new int[amountOfPlayers];

    }

    /**
     * The game which is being playerBets between players through the server
     */
    public void playingTheGame()
    {

        for(int p = 0; p < playerIDs.length; p++) {
            playerIDs[p] = players.getObserverID();
            playerNames[p] = players.getUserName();
        }

        cleanFoldedArray();

        raise = bigblind;
        table = new Table();

        updateDealerBigandSmall();
        dealHandsToPlayers();

        while (round < 5)
        {
            newRound = false;

            cleanPlayedArray();

            // Deal one or three cards to the table, depending on if it is the 
            // flop, turn or the river. 
            table.dealCard();

            while
            (playersession)
            {
                //dealer deals cards, the blind ones must put out money
                //görs med index of modulus antal spelare för att se vems tur de är. 1 dealer, 2 big blind, 3 small blind.
                //table show 3 first cards

                if (playerTurn >= playerIDs.length)
                    playerTurn = (playerTurn % playerIDs.length);

                for (Observer observer : players)
                    observer.updateTurnAndOptions(playerIDs[playerTurn + 1 % amountOfPlayers], minimumState, raise);

                dealer = playerIDs[playerTurn];
             /*   for (int i = 0; i < 10; i++) {
                    if (!playerHavePlayed && i < 10) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (!playerHavePlayed) {
                        players.get((playerTurn + 1) % amountOfPlayers).updateFoldFromServer();
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
                    if (minimumState == States.BIG)
                    {
                        minimumState = States.SMALL;
                        playerTurn += 1;
                    }

                    else if (minimumState == States.SMALL)
                    {
                        minimumState = States.RAISE;
                        dealHandsToPlayers();
                    }

                    else
                        playerTurn += 1;

                    playerHavePlayed = false;

                    if (newRound)
                    {
                        round++;
                        raise = 0;
                        if(round == 5)
                            newRound=false;
                        else {
                            int tmp = 0;
                            for(int j = 0; j < playerIDs.length; j++)
                            {
                                if(foldedOrAllInBets[playerIDs[j]] != States.FOLD)
                                {
                                    tmp++;
                                }

                                if(tmp == 1)
                                {
                                    newRound = false;
                                }
                            }

                        }
                        break;
                    }
                }
            }

            if(newRound == false) {
                calculateWinnerAndPot();
            }
            //LASTROUND

        }
    }

    private void calculateWinnerAndPot() {

        for(int i = 0; i < playerBets.length; i++) {
            if(foldedOrAllInBets[i] == States.FOLD) {

            }
        }
    }

    /**
     * Notify players at the beginning of the round who will act as dealer, small blind or big blind
     */
    private void updateDealerBigandSmall()
    {
        if (playerTurn >= playerIDs.length)
            playerTurn = (playerTurn % playerIDs.length);

        for (Observer observer : players)
        {
            observer.updateDealerBigSmalBlinds(playerIDs[playerTurn], playerIDs[(playerTurn + 1) % playerIDs.length], playerIDs[(playerTurn + 2) % playerIDs.length], bigblind, smallblind);
        }

    }

    /**
     * Update the collected pot with bets collected from a players move
     * @param ID - of specific player
     * @param player - name of specific player who made a move
     * @param bets - bets of player who made a move
     * @param move - state of the player which made a move
     */
    public void severUpdatePot(int ID, String player, double bets, States move) {
        pot += bets;
        playerHavePlayed = true;

        if (stateOfBet == States.RAISE)
        {
            if (raise < (playerBets[ID] + bets))
            {
                raise = (playerBets[ID] + bets);
                pot += bets;
                foldedOrAllInBets[ID] = States.RAISE;
                minimumState = States.RAISE;
                playerBets[ID] += bets;
            }
            else if(stateOfBet == States.ALLIN)
            {
                pot += bets;
                foldedOrAllInBets[ID] = States.ALLIN;
                int tmp1 = 0;
                for(int i = 0; i < playerBets.length;i++) {
                    if(playerBets[i] == raise || foldedOrAllInBets[i] == States.FOLD || foldedOrAllInBets[i] == States.ALLIN)
                        tmp1++;
                }
                if(tmp1 == amountOfPlayers)
                    newRound = true;
            }
        }

        else if (stateOfBet == States.CALL)
        {
            if (raise == (playerBets[ID] + bets))
            {
                pot += bets;
                foldedOrAllInBets[ID] = States.CALL;
                int tmp2 = 0;
                for(int i = 0; i < playerBets.length;i++) {
                    if(playerBets[i] == raise || foldedOrAllInBets[i] == States.FOLD || foldedOrAllInBets[i] == States.ALLIN)
                        tmp2++;
                }
                if(tmp2 == amountOfPlayers)
                    newRound = true;
            }

        }

        else if (stateOfBet == States.CHECK)
        {
            minimumState = States.CHECK;
            foldedOrAllInBets[ID] = States.CHECK;
        }

        for (Observer observer : players)
        {
            observer.updateLastPlayersMove(player, stateOfBet);
        }
    }

    /**
     * Deal two cards to each player
     */
    private void dealHandsToPlayers()
    {
        for (common.Observer observer : players)
            observer.dealCards(table.getCardFromDeck(),table.getCardFromDeck());
    }

    /**
     * clean up the array responsible for tracking folded players
     */
    private void cleanFoldedArray()
    {
        for(int i = 0; i < playerIDs.length; i++)
        {
            foldedOrAllInBets[playerIDs[i]] = States.FOLD;
            playerBets[playerIDs[i]] = 0;
        }
    }

    /**
     * Clean the array responsible for tracking players bets
     */
    private void cleanPlayedArray()
    {
        for(int i = 0; i < playerIDs.length; i++)
        {
            if(foldedOrAllInBets[playerIDs[i]] != States.ALLIN || foldedOrAllInBets[playerIDs[i]] != States.FOLD)
            playerBets[playerIDs[i]] = 0;
        }
    }
}