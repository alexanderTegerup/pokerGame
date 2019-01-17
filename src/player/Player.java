package player;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;

import common.Hand;
import common.Observer;
import common.States;
import managers.GameManager;
import table.Card;


public class Player implements Observer {

	private Hand hand;
	private String name;
	private double wealth;
	private States state;
	private boolean dealer;
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


	/**
	 * Get the players Hand
	 * @return The cards in players hand
	 */
	public void getHand() {
		gameManager.collectHandsFromPlayer(observerID,hand);
	}


	/**
	 * Get the players name
	 * @return name - Player's Username
	 */
	public String getUserName() {
		return name;
	}


	/**
	 * Get the players Wealth
	 * @return wealth Amount of player's current chips 
	 */
	public double getWealth() {
		return wealth;
	}


	/**
	 * Get the players State
	 * @return state - Player's current/selected move e.g. FOLD, CALL...
	 */
	public States getState() {
		return state;
	}


	/**
	 * Get the players Observer ID
	 * @return observerID - Player's observerID used of the server to track the players in the game
	 */
	public int getObserverID() {
		return observerID;
	}


	/**
	 * Deal two cards to the player and prompt those out for the player
	 * @param card1
	 * @param card2 
	 */
	@Override
	public void dealCards(Card card1, Card card2) {
		hand = new Hand(card1, card2);

		System.out.println("Card 1: " + hand.getCard1().getRank() + " " + hand.getCard1().getSuit());
		System.out.println("Card 2: " + hand.getCard2().getRank() + " " + hand.getCard2().getSuit());  	
	}


	/**
	 * Update the server with the player's last move
	 * @param playerName
	 * @param move 
	 */
	@Override
	public void updateLastPlayersMove(String playerName, States move) {
		System.out.println("Player " + playerName + " did " + move);
	}


	/**
	 * Determine the game actions dependent on the "move" player selects
	 * @param playerID		- The players observer ID
	 * @param minReqState	- The minimum requested state in game, expected to be matched of the 
	 * 		 				  player, reported from the server
	 * @param callCost		- The minimum amount of chips needed to CALL
	 */
	@Override
	public void updateTurnAndOptions(int playerID, States minReqState, double callCost) {
		if (playerID == observerID) {

			System.out.println(name + "'s turn now! Please make a move");

			while (true) {
				System.out.println("Enter your move:");

				Scanner stateScan = new Scanner(System.in);

				States playerMove;
				try {
					playerMove = States.valueOf(stateScan.next().toUpperCase());
				} catch (IllegalFormatException e) {
					System.out.println("Please use a valid move");
					e.printStackTrace();
					break;
				}

				Scanner raiseScan = new Scanner(System.in);
				double raise = 0;
				double playerBet = 0;

				// (minReqState == (States.FOLD)
				// (minReqState == (States.CHECK)

				switch(playerMove) {

				case FOLD:
					state = playerMove;
					bet(playerBet);
					break;

				case CHECK:
					if (minReqState == States.CHECK) {
						state = playerMove;
						bet(playerBet);
					} else {
						System.out.println("You cannot Check!");
					}
					break;


				case CALL:
					if ((minReqState == States.BIG)   ||
						(minReqState == States.CALL)  ||
						(minReqState == States.RAISE) ||
						(minReqState == States.ALL_IN))
					{
						state = playerMove;
						playerBet = callCost;
						bet(callCost);
					}
					else {
						System.out.println("You cannot Call!");
					}
					break;

					case RAISE:
						state = playerMove;
						raise = raiseScan.nextDouble();
						playerBet = callCost + raise;
						bet(playerBet);
						break;

					case ALL_IN:
						state = playerMove;
						playerBet = wealth;
						bet(playerBet);
						break;

					default:
						System.out.println("Please select a valid move");
						break;
					}

					stateScan.close();
					raiseScan.close();
				}

			}
		}


		/**
		 * Play the big and small blinds
		 * @param dealerID	- The player ID for the dealer for the round
		 * @param bigID		- The player ID for the big blind for the round
		 * @param smallID	- The player ID for the small blind for the round
		 * @param big		- The amount of chips big blind costs for the round
		 * @param small		- The amount of chips small blind costs for the round
		 */
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


		/**
		 * Add the chips won to the winning players wealth and prompt the winner of the current for the round to the game
		 * @param playerID	 - The players observer ID
		 * @param winningPot - The pot player have won
		 */
		@Override
		public void updateWinner(int playerID, double winningPot) {
			if (playerID == observerID)
				wealth += winningPot;

			System.out.println(playerID); //TODO: Game manager have to be able to return players Name instead of ObserverID
		}


		/**
		 * The generic function which handles chips transactions between the player and server
		 * It also updates the server with all the actions players does
		 * @param playerBet	 - The amount of chips player bets, when Call, Raise or All In
		 */
		@Override
		public void bet(double playerBet) {

			if (wealth >= playerBet) {
				gameManager.severUpdatePot(observerID, name, playerBet, state);
				wealth -= playerBet;
			} 
			else {
				//Player goes ALL_IN if chips(wealth) are not enough to call or raise
				gameManager.severUpdatePot(observerID, name, wealth, States.ALL_IN);
				wealth = 0;
			}
		}	


		/**
		 * Prompt the game with the cards on the table
		 * @param tableCards - The game cards on the table
		 */
		@Override
		public void flipOfCardT(ArrayList<Card> tableCards) {
			for(int i = 0; i < tableCards.size(); i++)
			{
				System.out.println(tableCards.get(i).getRank() + " " + tableCards.get(i).getSuit());
			}
		}


		/**
		 * Fold as per requested from the server
		 */
		@Override
		public void foldRequestFromServer() {
			state = States.FOLD;
		}

	}

	//TODO: Let the server know when a player have no chips left, 
	//so that server can unsubscribe him from the game
