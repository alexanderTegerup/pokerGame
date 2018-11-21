package player;

import common.Observer;
import common.Subject;
import managers.GameManager;

import java.util.ArrayList;

public class Players implements Subject {

        private int MaxAmountOfPlayers;
        private double defaultStakes;
        private /*static*/ ArrayList<Observer> players;
        private static int currentAmount = 0;
        private /*static*/ boolean goodToGo;
        private /*static*/ GameManager gameManager;


    public Players(int playerAmount, double defaultStakesPerPlayer) {
            MaxAmountOfPlayers = playerAmount;
            defaultStakes = defaultStakesPerPlayer;
            players = new ArrayList<>();
            goodToGo = false;
        }

    public boolean addPlayerToTable(String userName) {
            if(currentAmount<MaxAmountOfPlayers) {
                register(new Player(userName, defaultStakes, gameManager));
                System.out.println("Du har joint");
                currentAmount++;

                if(MaxAmountOfPlayers==currentAmount) {
                    System.out.println("Den som ska få go är...");
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
        currentAmount--;
    }

    public /*static*/ int getCurrentAmount() {
        return currentAmount;
    }

    public /*static*/ boolean isGoodToGo() {
        return goodToGo;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
}
