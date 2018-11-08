package server;

import managers.GameManager;
import managers.LoginManager;
import player.Player;
import player.Players;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {
    public static void main(String[] args) throws IOException {

        Player user;

        Players players = new Players(4, 200);
        LoginManager loginManager = new LoginManager(/*out, in, */players);

        //open game with user
        GameManager gameManager = new GameManager(/*out, in, */players);

        //user = loginManager.loginFunction();
        loginManager.loginFunction("Mario");
        //loginManager.loginFunction("jonathan");
        //loginManager.loginFunction("Aziz");
        //loginManager.loginFunction("alex");
        //players.setGameManager(gameManager);
        //gameManager.playingTheGame();
    }

}


//later
/*
        if (args.length != 1) {
            System.err.println("Usage: java server.MultiServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        player.Players players = new player.Players(4, 200);
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
               new Thread(new MultiServerThread(serverSocket.accept(), players)).start();
            }

            System.out.println("TABLE FILLED");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
        */