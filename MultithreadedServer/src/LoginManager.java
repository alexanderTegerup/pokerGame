import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginManager {

    private String outputLine, inputLine;
    private PrintWriter out;
    private BufferedReader in;
    private Players players;
    Player player;

    public LoginManager(PrintWriter o, BufferedReader i, Players players) {
        out = o;
        in = i;
        this.players = players;
    }

    public Player loginFunction() {
        String username = "";
        try {
            outputLine = "Vänligen skriv ett gästnamn";
            out.println(outputLine);

            if ((inputLine = in.readLine()) != null) {
                int pl = players.getAmountOfPlayersOnBoard();
                System.out.println("antalet players är " + pl);
                username = inputLine;

                if (players.addPlayerToTable(username)) {
                    System.out.println("Lyckat");
                }
                else {
                    System.out.println("tyvärr nåt fel");
                    return null;
                }
            }

            ArrayList<Player> listPlayers = Players.getBoardOfPlayers();

            System.out.println("Listing all players");
            for (int i = 0; i < listPlayers.size(); i++) {
                System.out.println(listPlayers.get(i).getUserName());
            }

            while (true) {
                if (!players.getStateOfPlayer(username).equals(States.GO)) {
                    outputLine = "Waiting";
                    out.println(outputLine);
                    TimeUnit.SECONDS.sleep(3);
                } else {
                    outputLine = "its time!";
                    out.println(outputLine);
                    player = players.getPlayer(username);
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

}
