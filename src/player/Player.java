package player;

import java.util.ArrayList;
import java.util.Scanner;

import common.Observer;
import common.States;
import managers.GameManager;
import table.Card;


class Hand {
	
	private Card card1;
	private Card card2;
	
    public void setHand(Card c1, Card c2) {
    	card1 = c1;
    	card2 = c2;
    }
    
    public Card getCard1() { 	
    	return card1;
    }
    
    public Card getCard2() { 	
    	return card2;
    }
}


public class Player implements Observer {

	private Hand hand;
    private String name;
    private double wealth;
    private States state;
    private boolean dealer;
    private double bet = 0;
    private GameManager gameManager;
    private static int observerIDTracker = 0;
    // Used to track the observers
    private int observerID;

    public Player(String playerName, double playerWealth, GameManager gm) {
        name = playerName;
        wealth = playerWealth;
        state = States.WAITING;
        observerID = observerIDTracker;
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
    	System.out.println("Player " + playerName + " did " + move);
    }

    
    @Override
    public void updateTurnAndOptions(int player, States minimumState, double callCost) {
    	if (player == observerID) {
    		state = minimumState;

    		Scanner stateScan = new Scanner(System.in);
    		States playerMove = States.valueOf(stateScan.next().toUpperCase());
    		//TO DO parsedouble or throw exception to control that the input is double

    		Scanner raiseScan = new Scanner(System.in);
    		double raise = 0;
    		double bet = 0;
    		

    		switch(playerMove) {
    		
    		case FOLD:
    		case CHECK:
    			state = playerMove;
    			
    		case CALL:
    			bet(callCost);
    			
    			bet = callCost;
    			state = playerMove;

    		case RAISE:
    			raise = raiseScan.nextDouble();
    			bet = callCost + raise;
    			state = playerMove;

    		case ALLIN:
    			bet = wealth;
    			state = playerMove;

    		default:
    		}


    		wealth -= bet;
    		gameManager.severUpdatePot(observerID, name, bet, state);
    		
    		stateScan.close();
    		raiseScan.close();

    	}
    }
    

    @Override
    public void updateDealerBigSmalBlinds(int dealerID, int bigID, int smallID, double big, double small) {
        dealer = false;

        if(observerID == dealerID) {
            dealer = true;
        }
        if(bigID == observerID) {
        	bet(big);
        }
        else if(smallID == observerID) {
        	bet(small);
        }
        
    }
    

    @Override
    public void updateWinner(int player, double winningPot) {
    	if (player == observerID)
    		wealth += winningPot;
    	
    	System.out.println(player);
    }
    

    @Override
    public void bet(double raise) {
    	
        bet+=raise;
        gameManager.severUpdatePot(observerID, name, bet, state);
        wealth -= bet;
        bet = 0;
    }
    

    @Override
    public void CurrentTurnPotRaises(double raises) {

    }
    

    
   @Override
   public void flipOfCardT(ArrayList<Card> tableCards) {
	   for(int i = 0; i < tableCards.size(); i++)
	   {
		   System.out.println(tableCards.get(i).getRank() + " " + tableCards.get(i).getSuit());
	   }
   }
 

    @Override
    public void updateFoldFromServer() {
        state = States.FOLD;
    }
    

    public int getObserverID() {
        return observerID;
    }

    @Override
    public void dealCards(Card card1, Card card2) {
    	hand.setHand(card1, card2);
    	
    	System.out.println("Card 1: " + hand.getCard1().getRank() + " " + hand.getCard1().getSuit());
    	System.out.println("Card 2: " + hand.getCard2().getRank() + " " + hand.getCard2().getSuit());  	
    }
}

