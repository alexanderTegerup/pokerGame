/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiServerThread implements Runnable {
    private Socket socket = null;
    private Players players;

    public MultiServerThread(Socket socket, Players players) {
        this.socket = socket;
        this.players = players;
    }

    public void run() {
        try {
            String outputLine, inputLine;
            Player user;

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            LoginManager loginManager = new LoginManager(out, in, players);

            user = loginManager.loginFunction();

            if(user == null) {
                socket.close();
            }
            else {
                //open game with user
                GameManager gameManager = new GameManager(out, in);
                gameManager.playingTheGame();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}*/