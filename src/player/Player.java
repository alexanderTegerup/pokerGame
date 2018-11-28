package player;

import java.util.Scanner;

import common.Observer;
import common.States;
import managers.GameManager;
import table.Card;

public class Player implements Observer {

  // private Card hand;
    private String name;
    private double wealth;
    private States state;
    private boolean dealer;
    private double bet = 0;
    private /*static*/ GameManager gameManager;
    private static int observerIDTracker = 0;
        // Used to track the observers
    private int observerID;

    public Player(String uname, double gameStakes, GameManager gm) {
        name = uname;
        wealth = gameStakes;
        state = States.WAITING;
        this.observerID = observerIDTracker;
        gameManager = gm;
    }
    

    public String getUserName() {
        return name;
    }

    
    public double getWealth() {
        return wealth;
    }


    public States getState() {
        return state;
    }


    @Override
    public void updateLastPlayersMove(String playerName, States move) {
    	
    	double callCost = 0; //callCost dummy --- TO BE Implemented  
    	
		Scanner raiseScan = new Scanner(System.in);
		double raise = raiseScan.nextDouble();
		//TO DO parseouble or throw excp. to control that input is double
		//TO DO shutdown the scanners
		
		Scanner stateScan = new Scanner(System.in);
		States selectedState = States.valueOf(stateScan.next().toUpperCase());
		
		double bet = 0;
		
		switch(selectedState) {
		
		case FOLD:
		case CHECK:
			state = selectedState;
		case CALL:
			bet = callCost;
			state = selectedState;
		
		case RAISE:
			bet = callCost + raise;
			state = selectedState;
			
		case ALLIN:
			//bet = wealth;
			state = selectedState;
			
		default:
			}

		
		wealth -= bet;
		gameManager.updatePot(observerID, name, bet, state);

    }
    
/*
    @Override
    public void dealCards(Card card1, Card card2) {

    }
*/
    
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
            bet += big;
        }
        else if(state == States.SMALL) {
            bet += small;
        }
        
    }
    

    @Override
    public void updateWinner(int playername, double winningPot) {
        bet = 0;
    }
    

    @Override
    public void updatePot(double currentRaisedPot) {
        bet+=currentRaisedPot;
        gameManager.updatePot(observerID, name, bet, state);
        wealth -= bet;
        bet = 0;
    }
    

    @Override
    public void CurrentTurnPotRaises(double raises) {

    }
    

    /*
   @Override
   public void flipOfCardT() {
   
   }
 */

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

//announce state and followup info - KLAR!

//server måste veta om man är fold