import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GameManager {

    private boolean playersession = true;
    private Player player;
    private boolean bigblind, smallblind, dealer;
    private double stack, round;
    private ArrayList<Player> players;
    private PrintWriter out;
    private BufferedReader in;

    public GameManager(PrintWriter o, BufferedReader i, Player user) {
        player = user;
        in = i;
        out = o;
    }

    public void playingTheGame() {
        while(playersession) {
            round = 0;

            //dealer deals cards, the blind ones must put out money
            //görs med index of modulus antal spelare för att se vems tur de är. 1 dealer, 2 big blind, 3 small blind.
            //table show 3 first cards

            while(round <=3 || player.getState()!=States.FOLD) {

            }
        }
    }

}