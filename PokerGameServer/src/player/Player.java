

import common.Observer;
import common.States;
import managers.GameManager;
import table.Card;

public class Player implements Observer {

  // private Card hand;
    private String userName;
    private double stakes;
    private States state;
    private boolean dealer;
    private double bettingCash = 0;
    private /*static*/ GameManager gameManager;
    private static int observerIDTracker = 0;
        // Used to track the observers
    private int observerID;

    public Player(String uname, double gameStakes, GameManager gm) {
        userName = uname;
        stakes = gameStakes;
        state = States.WAITING;
        this.observerID = observerIDTracker;
        gameManager = gm;
    }

    public String getUserName() {
        return userName;
    }

    public double getStakes() {
        return stakes;
    }

    private void setStakes(double stakes) {
        this.stakes = stakes;
    }

    public States getState() {
        return state;
    }

    private void setState(States state) {
        this.state = state;
    }

    @Override
    public void updateLastPlayersMove(String playerName, States move) {

    }

  //  @Override
  //  public void dealCards(Card card1, Card card2) {

  //  }

    @Override
    public void updateTurnAndOptions(int player, States minimumState, double raisedPot) {
        if (player == observerID)
            state = minimumState;
        if(state == States.BIG)
            updatePot(raisedPot);
    }

    @Override
    public void updateDealerBigSmalBlinds(int dealerID, int bigID, int smallID, double big, double small) {
        dealer = false;

        if(observerID == dealerID) {
            dealer = true;
        }
        if(bigID == observerID) {
            state = States.BIG;
        }
        if(smallID == observerID) {
            state = States.SMALL;
        }
        if(state == States.BIG) {
            bettingCash += big;
        }
        else if(state == States.SMALL) {
            bettingCash += small;
        }

        }

    @Override
    public void updateWinner(int playername, double winningPot) {
        bettingCash = 0;
    }

    @Override
    public void updatePot(double currentRaisedPot) {
        bettingCash+=currentRaisedPot;
        gameManager.updatePot(observerID, userName, bettingCash, state);
        stakes -= bettingCash;
        bettingCash = 0;
    }

    @Override
    public void CurrentTurnPotRaises(double raises) {

    }

 //   @Override
 //   public void flipOfCardT() {

//    }

    @Override
    public void updateFoldFromServer() {
        state = States.FOLD;
    }

    public int getObserverID() {
        return observerID;
    }

    @Override
    public void dealCards(Card card1, Card card2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

//kalla på server härifrån

//get winning pot

//announce state and followup info

//server måste veta om man är fold