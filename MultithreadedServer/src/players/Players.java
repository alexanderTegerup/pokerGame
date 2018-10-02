package players;

import java.util.ArrayList;

public class Players {

    private static int amountOfPlayers;
    private static int amountOfPlayersOnBoard;
    private static double defaultStakes;
    private static ArrayList<Player> boardOfPlayers;

    public Players(int playerAmount, double defaultStakesPerPlayer) {
        amountOfPlayers = playerAmount;
        defaultStakes = defaultStakesPerPlayer;
        boardOfPlayers = new ArrayList<>();
        amountOfPlayersOnBoard = 0;
    }

    public static boolean addPlayerToTable(String userName) {
        int i = 0;
        for (Player p:boardOfPlayers) {
            i++;
        }
        if(i<amountOfPlayers) {
            boardOfPlayers.add(new Player(userName, defaultStakes));
            System.out.println("Du har joint");
            amountOfPlayersOnBoard++;

            for (Player p:boardOfPlayers) {
                if(p.getUserName().equals(userName))
                    if(amountOfPlayers== i)
                        setStateOnPlayer(boardOfPlayers.get(0).getUserName(), States.GO);
            }
            return true;
        }
        return false;
    }

    public static boolean deletePlayerFromTable(String userName) {
        int i = 0;
        for (Player p:boardOfPlayers) {
            if(p.getUserName().equals(userName)) {
                boardOfPlayers.remove(i);

                return true;
            }
            i++;
        }
        return false;
    }

    public static int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public static int getAmountOfPlayersOnBoard() {
        return amountOfPlayersOnBoard;
    }

    public static ArrayList<Player> getBoardOfPlayers() {
        return boardOfPlayers;
    }

    public static void setStateOnPlayer(String username, States state) {
        for(int i = 0; i<boardOfPlayers.size();i++) {
            if(boardOfPlayers.get(i).getUserName().equals(username))
                boardOfPlayers.get(i).setState(state);
        }
    }

    public static States getStateOfPlayer(String username) {
        for (int i = 0; i < boardOfPlayers.size(); i++) {
            if (boardOfPlayers.get(i).getUserName().equals(username))
                return boardOfPlayers.get(i).getState();
        }
        return null;
    }
}
