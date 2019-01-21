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

public class GameManager {
    private boolean playersession = true, playerHavePlayed = false, newRound;

    private double bigblind, smallblind;
    private double pot = 0, round = 0, raise = 0;
    //    private PrintWriter out;
//    private BufferedReader in;
    private int amountOfPlayers, dealer;
    private /*static*/ int playerTurn;

    private int initialBig;
    private int smallBlindID = 0;
    private int[] playerIDs;
    private States[] foldedOrAllInBets;
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
     * @param playersObj using instance of players to notify the playersObj of the changes in the gamemanager
     */
    public GameManager(/*PrintWriter o, BufferedReader i, */Players playersObj) {
        this.playersObj = playersObj;
        //   in = i;
        //   out = o;
        playerTurn = 0;
        bigblind = 10;
        smallblind = 5;

        playersLeftInTheGame = new ArrayList<>();
        pokerRules = new PokerRules();

    }


    public void initilizePlayersObj() {
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
    public void playingTheGame() {

        boolean foldedWinner = false;
        int foldedWinnerID = 999;

        for (int p = 0; p < playerIDs.length; p++) {
            playerIDs[p] = players.get(p).getObserverID();
            playerNames[p] = players.get(p).getUserName();
            playersLeftInTheGame.add(players.get(p).getObserverID());
        }

        cleanFoldedArray();
        cleanPlayedArray();

        raise = smallblind;
        table = new Table();

        updateDealerBigandSmall();
        //dealHandsToPlayers();

        while (round < 5) {
            newRound = false;

            // Deal one or three cards to the table, depending on if it is the 
            // flop, turn or the river.
            if (round >= 2) {
                table.dealCard();
                tableCards = table.showAllCards();
                int tmpAmount = table.returnNrOfCards();
                for (Observer player : players) {
                    player.flipOfCardT(tableCards, tmpAmount);
                }
            }
            while
            (playersession) {
                //dealer deals cards, the blind ones must put out money
                //görs med index of modulus antal spelare för att se vems tur de är. 1 dealer, 2 big blind, 3 small blind.
                //table show 3 first cards
                int exist = 0;
                int incFirstPlayer = 1;
                do {
                    if (!playersLeftInTheGame.contains(playerIDs[(playerTurn % playerIDs.length)]))
                        playerTurn++;
                    else if (foldedOrAllInBets[playerIDs[(playerTurn % playerIDs.length)]] == States.FOLD || foldedOrAllInBets[playerIDs[(playerTurn % playerIDs.length)]] == States.ALL_IN) {
                        playerTurn++;
                    } else if (!playersLeftInTheGame.contains(playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]))
                        incFirstPlayer++;
                    else if (foldedOrAllInBets[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == States.FOLD || foldedOrAllInBets[playerIDs[((playerTurn + incFirstPlayer) % playerIDs.length)]] == States.ALL_IN) {
                        incFirstPlayer++;
                    } else {
                        playerTurn = (playerTurn % playerIDs.length);
                        exist = 1;
                    }
                } while (exist == 0);

                System.out.println("player turn is " + (playerTurn));
                System.out.println(playerIDs.length);
                System.out.println("rond = " + round);

                if (minimumState != States.BIG && minimumState != States.SMALL)
                    if (playerBets[playerIDs[(playerTurn + incFirstPlayer) % playerIDs.length]] == raise && playerBets[playerIDs[playerTurn % playerIDs.length]] == raise)
                        minimumState = States.CHECK;

                System.out.println("minimumstate i server är " + minimumState);
                for (Observer observer : players)
                    observer.updateTurnAndOptions(playerIDs[playerTurn % playerIDs.length], playerNames[playerIDs[playerTurn % playerIDs.length]], minimumState, (raise - (playerBets[playerIDs[playerTurn % playerIDs.length]])));
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

                if (playerHavePlayed) {
                    if (minimumState == States.SMALL) {
                        minimumState = States.BIG;
                        playerTurn += 1;
                    } else if (minimumState == States.BIG) {
                        minimumState = States.RAISE;
                        dealHandsToPlayers();
                        playerTurn += 1;
                    } else
                        playerTurn += 1;

                    playerHavePlayed = false;

                    if (newRound) {
                        round++;
                        if (round == 5)
                            newRound = false;
                        else {
                            int notFolded = 0;
                            for (int j = 0; j < playerIDs.length; j++) {
                                if (foldedOrAllInBets[playerIDs[j]] != States.FOLD) {
                                    notFolded++;
                                    foldedWinnerID = playerIDs[j];
                                }
                            }
                            System.out.println("tmp i newround " + notFolded);
                            if (notFolded == 1) {
                                newRound = false;
                                round = 5;
                                foldedWinner = true;
                            }
                        }
                        if (round > 1)
                            playerTurn = smallBlindID;
                        cleanArray();
                        break;
                    }
                }
            }
        }
        if (newRound == false && foldedWinner == false) {
            calculateWinnerAndPot();
        } else if(newRound == false && foldedWinner == true)
            calculateWinnerFoldPot(foldedWinnerID);
    }

    private void calculateWinnerFoldPot(int ID) {
        String[] winnerName = new String[1];
        int[] winnerId = new int[1];
        winnerId[0] = ID;
        winnerName[0] = playerNames[winnerId[0]];

        for (Observer player : players) {
            player.updateWinner(winnerId, winnerName, pot);
        }
    }

    private void calculateWinnerAndPot() {

        for (Observer player : players) {
            player.getHand();
        }

        int winnerId[] = pokerRules.determineBestHand(playerHands, tableCards);
        String[] winnerNames = new String[winnerId.length];

        for (int i = 0; i < winnerId.length; i++) {
            winnerNames[i] = playerNames[winnerId[i]];
        }

        for (Observer player : players) {
            player.updateWinner(winnerId, winnerNames, pot);
        }
    }

    /**
     * Notify players at the beginning of the round who will act as dealer, small blind or big blind
     */
    private void updateDealerBigandSmall() {
        playerTurn = (playerTurn % playerIDs.length);
        int exist = 0;
        int incBig = 1;
        int incSmall = 1;
        do {
            if (!playersLeftInTheGame.contains(playerTurn % playerIDs.length)) {
                playerTurn = ((playerTurn + 1) % playerIDs.length);
            } else if (!playersLeftInTheGame.contains((playerTurn + incBig) % playerIDs.length)) {
                incBig++;
            } else if (!playersLeftInTheGame.contains((playerTurn + incBig + incSmall) % playerIDs.length)) {
                incSmall++;
            } else {
                exist = 1;
            }
        } while (exist == 0);

        initialBig = playerIDs[(playerTurn + 2) % playerIDs.length];
        for (Observer observer : players) {
            observer.updateDealerBigSmalBlinds(playerIDs[playerTurn], playerIDs[(playerTurn + 1) % playerIDs.length], playerIDs[(playerTurn + 2) % playerIDs.length], smallblind, bigblind);
        }
        playerTurn += 1;
    }

    /**
     * Update the collected pot with bets collected from a players move
     *
     * @param ID     - of specific player
     * @param player - name of specific player who made a move
     * @param bets   - bets of player who made a move
     * @param move   - state of the player which made a move
     */
    public void severUpdatePot(int ID, String player, double bets, States move) {
        System.out.println(ID + player + bets + move);
        //pot += bets;
        playerHavePlayed = true;
            if (move == States.RAISE) {
                if (raise < (playerBets[ID] + bets)) {
                    raise = (playerBets[ID] + bets);
                    pot += bets;
                    foldedOrAllInBets[ID] = States.RAISE;
                    minimumState = States.RAISE;
                    playerBets[ID] += bets;
                }

            } else if (move == States.ALL_IN) {
                pot += bets;
                foldedOrAllInBets[ID] = States.ALL_IN;
                int tmp1 = 0;
                for (int i = 0; i < playerBets.length; i++) {
                    if (playerBets[i] == raise || foldedOrAllInBets[i] == States.FOLD || foldedOrAllInBets[i] == States.ALL_IN)
                        if (foldedOrAllInBets[i] != States.GO)
                            tmp1++;
                }

                playerBets[ID] += bets;

                if (tmp1 == amountOfPlayers)
                    newRound = true;
            } else if (move == States.CALL) {
                if (raise == (playerBets[ID] + bets)) {
                    pot += bets;
                    foldedOrAllInBets[ID] = States.CALL;
                    int tmp2 = 0;
                    for (int j = 0; j < playerBets.length; j++) {
                        if (playerBets[j] == raise || foldedOrAllInBets[j] == States.FOLD || foldedOrAllInBets[j] == States.ALL_IN)
                            if (foldedOrAllInBets[j] != States.GO)
                                tmp2++;
                    }

                    playerBets[ID] += bets;

                    if (tmp2 == amountOfPlayers)
                        newRound = true;

                    if (initialBig == ID)
                        initialBig = 999;
                }
            } else if (move == States.CHECK) {
                int tmp2 = 0;
                minimumState = States.CHECK;
                foldedOrAllInBets[ID] = States.CHECK;
                if (initialBig == ID) {
                    initialBig = 999;
                    newRound = true;
                }
                for (int j = 0; j < playerBets.length; j++) {
                    if (foldedOrAllInBets[j] == States.GO) {
                        //do nothing
                    } else if (playerBets[j] == raise || foldedOrAllInBets[j] == States.FOLD || foldedOrAllInBets[j] == States.ALL_IN) {
                        if (foldedOrAllInBets[j] != States.GO)
                            tmp2++;
                    }
                }
                if (tmp2 == amountOfPlayers)
                    newRound = true;

            } else if (move == States.FOLD) {
                int tmp2 = 0;
                System.out.println("heeej");
                foldedOrAllInBets[ID] = States.FOLD;
                if (initialBig == ID) {
                    initialBig = 999;
                    newRound = true;
                }
                for (int j = 0; j < playerBets.length; j++) {
                    if (playerBets[j] == raise || foldedOrAllInBets[j] == States.FOLD || foldedOrAllInBets[j] == States.ALL_IN)
                        if (foldedOrAllInBets[j] != States.GO)
                            tmp2++;
                }
                if (tmp2 == amountOfPlayers)
                    newRound = true;
                System.out.println(foldedOrAllInBets[ID]);
            } else if (move == States.BIG) {
                pot += bets;
                playerBets[ID] += bets;
                raise = bets;
                newRound = true;
            } else if (move == States.SMALL) {
                pot += bets;
                playerBets[ID] += bets;
                smallBlindID = ID;
            }

            for (Observer observer : players) {
                observer.updateLastPlayersMove(player, move);
            }

        int tmp = 1;

        for (int i = 0; i < playerBets.length; i++) {
            if (foldedOrAllInBets[i] == States.FOLD)
                tmp++;
        }

        System.out.println("tmp " +tmp);
        System.out.println(amountOfPlayers);
        if (tmp == amountOfPlayers) {
            newRound = true;
        }
        System.out.println("tmp" + tmp);
    }

    /**
     * Deal two cards to each player
     */
    private void dealHandsToPlayers() {
        for (common.Observer observer : players)
            observer.dealCards(table.getCardFromDeck(), table.getCardFromDeck());
    }

    /**
     * clean up the array responsible for tracking folded players
     */
    private void cleanFoldedArray() {
        foldedOrAllInBets = new States[playerIDs.length];
        Arrays.fill(foldedOrAllInBets, States.GO);

        for (int i = 0; i < playerIDs.length; i++) {
            foldedOrAllInBets[playerIDs[i]] = States.GO;
            playerBets[playerIDs[i]] = 0;
            System.out.println(foldedOrAllInBets[playerIDs[i]]);
        }
    }

    /**
     * Clean the array responsible for tracking players bets
     */
    private void cleanPlayedArray() {
        for (int i = 0; i < playerIDs.length; i++) {
            if (foldedOrAllInBets[playerIDs[i]] != States.ALL_IN || foldedOrAllInBets[playerIDs[i]] != States.FOLD)
                playerBets[playerIDs[i]] = 0;
        }
    }

    private void cleanArray() {
        for (int i = 0; i < playerIDs.length; i++) {
            if (foldedOrAllInBets[playerIDs[i]] != States.FOLD && foldedOrAllInBets[playerIDs[i]] != States.ALL_IN)
                foldedOrAllInBets[playerIDs[i]] = States.GO;
        }
    }

    public void collectHandsFromPlayers(int ID, Hand hand) {
        if (foldedOrAllInBets[ID] != States.FOLD)
            playerHands[ID] = hand;
    }

    /* public boolean checkIfAllCalled() {
        int arrayIDIndexes;
        int tmp = 0;
        int tmp2 = 0;
        int tmp3 = 0;
        double[] allInBets = new double[amountOfPlayers];

        double bettedBefore = 0;
        for (int i = 0; i < playersLeftInTheGame.size(); i++) {
            arrayIDIndexes = playerIDs[playersLeftInTheGame.get(i)];
            if (foldedOrAllInBets[arrayIDIndexes] == States.FOLD)
                tmp++;
            else if (foldedOrAllInBets[arrayIDIndexes] == States.ALL_IN) {
                tmp++;
                allInBets[tmp3] = playerBets[arrayIDIndexes];
                tmp3++;
            } else if (bettedBefore == playerBets[arrayIDIndexes])
                tmp++;
        }

        if (tmp == (amountOfPlayers - 1)) {

            for (double bets : allInBets)
                if (bets <= bettedBefore)
                    tmp2++;
            if (tmp2 == tmp3)
                return true;
        }
        return false;
    }*/
}