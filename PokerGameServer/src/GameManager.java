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
    ArrayList<Observer> listPlayers;

    public GameManager(PrintWriter o, BufferedReader i, Players players) {
        this.players = players;
        amountOfPlayers = players.getCurrentAmount();
        in = i;
        out = o;
        playerTurn = 0;
        bigblind = 100;
        smallblind = 50;
        listPlayers = players.getPlayers();
    }

    public void playingTheGame() {

        raise = bigblind;

        updateDealerBigandSmall();

        while (round < 5) {

            newRound = false;

            cleanPlayedArray();

            //deal three cards to table

            //deal 1 card to table

            //deal 1 card to table

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

    private void updateDealerBigandSmall() {
        if (playerTurn >= amountOfPlayers)
            playerTurn = (playerTurn % amountOfPlayers);
        for (Observer observer : listPlayers) {
            observer.updateDealerBigSmalBlinds(playerTurn, (playerTurn + 1) % amountOfPlayers, (playerTurn + 2) % amountOfPlayers, bigblind, smallblind);
        }

    }

    public void updatePot(int ID, String player, double bets, States stateOfBet) {
        pot += bets;
        playerPlayed = true;

        if (stateOfBet == States.RAISE) {
            if (raise < (played[ID] + bets)) {
                raise = (played[ID] + bets);
                pot += bets;
                minimumState = States.RAISE;
                played[ID] += bets;
            }
        } else if (stateOfBet == States.CALL) {
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
        } else if (stateOfBet == States.CHECK) {
            minimumState = States.CHECK;
        }
        for (Observer observer : listPlayers) {
            observer.updateLastPlayersMove(player, stateOfBet);
        }
    }

    private void dealHandsToPlayers() {
        for (Observer observer : listPlayers)
            observer.dealCards(/*dealCard(), dealcard()))*/);
    }

    private void cleanFoldedArray() {
        for(int i = 0; i < amountOfPlayers; i++) {
            foldedPlayers[i] = false;
            played[i] = 0;
        }
    }

    private void cleanPlayedArray() {
        for(int i = 0; i < amountOfPlayers; i++) {
            played[i] = 0;
        }
    }
}