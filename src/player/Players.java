package player;

import common.Observer;
import common.Subject;
import managers.GameManager;

import java.util.ArrayList;

public class Players implements Subject {

        private int MaxAmountOfPlayers;
        private double wealth;
        private /*static*/ ArrayList<Observer> players;
        private static int AmountOfPlayers = 0;
        private /*static*/ boolean goodToGo;
        private /*static*/ GameManager gameManager;


    public Players(int playerAmount, double playerWealth) {
            MaxAmountOfPlayers = playerAmount;
            wealth = playerWealth;
            players = new ArrayList<>();
            goodToGo = false;
        }

    public boolean addPlayerToTable(String userName) {
            if (AmountOfPlayers<MaxAmountOfPlayers) {
                register(new Player(userName, wealth, gameManager));
                System.out.println("You joined the game!");
                AmountOfPlayers++;

                if (MaxAmountOfPlayers == AmountOfPlayers) {
                    System.out.println(userName + "is starting");
                    goodToGo = true;
                }
                return true;
            }
            return false;
        }

        public /*static*/ ArrayList<Observer> getPlayers() {
            return players;
        }

    @Override
    public void register(Observer newObserver) {
        players.add(newObserver);
    }

    @Override
    public void unregister(Observer deleteObserver) {
        // Get the index of the observer to delete
        int observerIndex = players.indexOf(deleteObserver);
        // Print out message (Have to increment index to match)
        System.out.println("common.Observer " + (observerIndex+1) + " deleted");
        // Removes observer from the ArrayList
        players.remove(observerIndex);
        AmountOfPlayers--;
    }

    public /*static*/ int getCurrentAmount() {
        return AmountOfPlayers;
    }

    public /*static*/ boolean isGoodToGo() {
        return goodToGo;
    }

    public void setGameManager(GameManager gm) {
        gameManager = gm;
    }
}
