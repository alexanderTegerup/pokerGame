package managers;

import common.Observer;
import common.States;
import player.Players;
import table.Table;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private boolean playersession = true, playerPlayed = false, newRound;
    private double bigblind, smallblind;
    private double pot = 0, round = 1, raise = 0;
    private PrintWriter out;
    private BufferedReader in;
    private int amountOfPlayers, dealer;
    private static int playerTurn;
    private boolean[] foldedPlayers;
    private double[] played;
    private Players players;
    private States minimumState = States.BIG;
    private Table table;
    ArrayList<Observer> listPlayers;

    /**
     * Constructor for GameManager class
     * @param players using instance of players to notify the players of the changes in the gamemanager
     */
    public GameManager(/*PrintWriter o, BufferedReader i, */Players players) {
        this.players = players;
        amountOfPlayers = players.getCurrentAmount();
     //   in = i;
     //   out = o;
        playerTurn = 0;
        bigblind = 100;
        smallblind = 50;
        listPlayers = players.getPlayers();
    }

    /**
     * The game which is being played between players through the server
     */
    public void playingTheGame() {

        raise = bigblind;
        table = new Table();

        updateDealerBigandSmall();
        dealHandsToPlayers();

        while (round < 5) {

            newRound = false;

            cleanPlayedArray();

            // Deal one or three cards to the table, depending on if it is the 
            // flop, turn or the river. 
            table.dealCard();

            while (playersession) {
                //dealer deals cards, the blind ones must put out money
                //görs med index of modulus antal spelare för att se vems tur de är. 1 dealer, 2 big blind, 3 small blind.
                //table show 3 first cards

                if (playerTurn >= amountOfPlayers)
                    playerTurn = (playerTurn % amountOfPlayers);
                for (Observer observer : listPlayers)
                    observer.updateTurnAndOptions((playerTurn + 1) % amountOfPlayers, minimumState, raise);

                dealer = playerTurn;
                for (int i = 0; i < 10; i++) {
                    if (!playerPlayed && i < 10) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (!playerPlayed) {
                        listPlayers.get((playerTurn + 1) % amountOfPlayers).updateFoldFromServer();
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
                }

                playerPlayed = false;

                if (newRound) {
                    round++;
                    raise = 0;
                    break;
                }
            }
        }

    }

    /**
     * Notify players at the beginning of the round who will act as dealer, small blind or big blind
     */
    private void updateDealerBigandSmall() {
        if (playerTurn >= amountOfPlayers)
            playerTurn = (playerTurn % amountOfPlayers);
        for (Observer observer : listPlayers) {
            observer.updateDealerBigSmalBlinds(playerTurn, (playerTurn + 1) % amountOfPlayers, (playerTurn + 2) % amountOfPlayers, bigblind, smallblind);
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
        playerPlayed = true;

        if (move == States.RAISE) {
            if (raise < (played[ID] + bets)) {
                raise = (played[ID] + bets);
                pot += bets;
                minimumState = States.RAISE;
                played[ID] += bets;
            }
        } else if (move == States.CALL) {
            if (raise == (played[ID] + bets)) {
                pot += bets;
                int tmp = 0;
                for(int i = 0; i < played.length;i++) {
                    if(played[i] == raise || foldedPlayers[i])
                        tmp++;
                }
                if(tmp == amountOfPlayers)
                    newRound = true;
            }
        } else if (move == States.CHECK) {
            minimumState = States.CHECK;
        }
        for (Observer observer : listPlayers) {
            observer.updateLastPlayersMove(player, move);
        }
    }

    /**
     * Deal two cards to each player
     */
    private void dealHandsToPlayers() {
        for (common.Observer observer : listPlayers)
            observer.dealCards(table.getCardFromDeck(),table.getCardFromDeck());
    }

    /**
     * clean up the array responsible for tracking folded players
     */
    private void cleanFoldedArray() {
        for(int i = 0; i < amountOfPlayers; i++) {
            foldedPlayers[i] = false;
            played[i] = 0;
        }
    }

    /**
     * Clean the array responsible for tracking players bets
     */
    private void cleanPlayedArray() {
        for(int i = 0; i < amountOfPlayers; i++) {
            played[i] = 0;
        }
    }
}