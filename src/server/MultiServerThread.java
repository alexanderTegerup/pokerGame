package server;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiServerThread implements Runnable {
    private Socket socket = null;
    private player.Players players;

    public MultiServerThread(Socket socket, player.Players players) {
        this.socket = socket;
        this.players = players;
    }

    public void run() {
        try {
            String outputLine, inputLine;
            player.Player user;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            managers.LoginManager loginManager = new managers.LoginManager(out, in, players);

            user = loginManager.loginFunction();

            if(user == null) {
                socket.close();
            }
            else {
                //open game with user
                managers.GameManager gameManager = new managers.GameManager(out, in, players);
                player.setGameManager(gameManager);
                gameManager.playingTheGame();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}*/