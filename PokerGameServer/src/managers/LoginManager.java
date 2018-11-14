package managers;

import common.Observer;
import player.Player;
import player.Players;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LoginManager {

    private String outputLine, inputLine;
    private PrintWriter out;
    private BufferedReader in;
    private Players players;
    Player player;

    public LoginManager(/*PrintWriter o, BufferedReader i, */Players players) {
        //out = o;
        //in = i;
        this.players = players;

    }

    public Player loginFunction(String username) {
       // String username = "";
        //try {
         //   outputLine = "Vänligen skriv ett gästnamn";
         //   out.println(outputLine);

         //   if ((inputLine = in.readLine()) != null) {
              //  username = inputLine;

                if (players.addPlayerToTable(username)) {
                    System.out.println("Lyckat");
                }
                else {
                    System.out.println("tyvärr nåt fel");
                    return null;
                }
           // }

            ArrayList<Observer> listPlayers = players.getPlayers();

            System.out.println("Listing all players");
          /*  for (int i = 0; i < listPlayers.size(); i++) {
                System.out.println(listPlayers.get(i).getUserName());
            }*/

            while (true) {
                if (!players.isGoodToGo()) {
                    System.out.println("Waiting");
                   // outputLine = "Waiting";
                   // out.println(outputLine);
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Its time");
                  //  outputLine = "its time!";
                  //  out.println(outputLine);
                    break;
                }
            }
     //   } catch (InterruptedException e) {
       //     e.printStackTrace();
        //} catch (IOException e) {
        //    e.printStackTrace();
       // }
        return player;
    }

}
