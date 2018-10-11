import java.util.ArrayList;

public class Players {

    private static int minMaxAmountOfPlayers;
    private static int amountOfPlayersOnBoard;
    private static double defaultStakes;
    private static ArrayList<Player> boardOfPlayers;

    public Players(int playerAmount, double defaultStakesPerPlayer) {
        minMaxAmountOfPlayers = playerAmount;
        defaultStakes = defaultStakesPerPlayer;
        boardOfPlayers = new ArrayList<>();
        amountOfPlayersOnBoard = 0;
    }

    public static boolean addPlayerToTable(String userName) {
        int currentAmount = 0;
        for (Player p:boardOfPlayers) {
            currentAmount++;
            if(p.getUserName().equals(userName))
                return false;
        }
        if(currentAmount<minMaxAmountOfPlayers) {
            boardOfPlayers.add(new Player(userName, defaultStakes));
            System.out.println("Du har joint");
            amountOfPlayersOnBoard++;

            if(minMaxAmountOfPlayers==amountOfPlayersOnBoard) {
                System.out.println("Den som ska få go är...");
                setStateOnPlayer(boardOfPlayers.get(0).getUserName(), States.GO);
                System.out.println("Nu har han staten... " + getStateOfPlayer(boardOfPlayers.get(0).getUserName()));
            }
            return true;
        }
        return false;
    }

    public static Player getPlayer(String username) {
        int i = 0;

        for (Player p:boardOfPlayers) {
            if(p.getUserName().equals(username)) {
                return  boardOfPlayers.get(i);
            }
            i++;
        }
        return null;
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
        return minMaxAmountOfPlayers;
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
