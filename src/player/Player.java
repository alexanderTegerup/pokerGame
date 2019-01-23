package player;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;

import common.Hand;
import common.Observer;
import common.States;
import managers.GameManager;
import table.Card;


public class Player implements Observer
{

    private Hand hand;
    private String name;
    private double wealth;
    private double smallB = 0, bigB = 0;

    private States state;
    private boolean dealer;
    private GameManager gameManager;
    private static int observerIDTracker = 0;
    // Used to track the observers
    private int observerID;

    public Player(String playerName, double playerWealth, GameManager gm)
    {
        name = playerName;
        wealth = playerWealth;
        state = States.WAITING;
        observerID = observerIDTracker++;
        gameManager = gm;
    }

    /**
     * Calls the function collectHandsFromPlayers to show it's hand to the game manager
     */
    public void getHand()
    {
        gameManager.collectHandsFromPlayers(observerID, hand);
    }


    /**
     * Get the players name
     *
     * @return name - Player's Username
     */
    public String getUserName()
    {
        return name;
    }


    /**
     * Get the players Wealth
     *
     * @return wealth Amount of player's current chips
     */
    public double getWealth()
    {
        return wealth;
    }


    /**
     * Get the players State
     *
     * @return state - Player's current/selected move e.g. FOLD, CALL...
     */
    public States getState()
    {
        return state;
    }


    /**
     * Get the players Observer ID
     *
     * @return observerID - Player's observerID used of the server to track the players in the game
     */
    public int getObserverID()
    {
        return observerID;
    }

    /**
     * receives two cards from the game manager to build a playing hand
     *
     * @param card1
     * @param card2
     */
    @Override
    public void dealCards(Card card1, Card card2)
    {
        hand = new Hand(card1, card2);

        System.out.println(name);
        System.out.println("Card 1: " + hand.getCard1().getRank() + " " + hand.getCard1().getSuit());
        System.out.println("Card 2: " + hand.getCard2().getRank() + " " + hand.getCard2().getSuit() + "\n");
    }

    /**
     * receive information on which player played what move
     *
     * @param playerName
     * @param move
     */
    @Override
    public void updateLastPlayersMove(String playerName, States move)
    {
        System.out.println("Player " + playerName + " did " + move);
    }


    /**
     * Determine the game actions dependent on the "move" player selects
     *
     * @param playerID    - The players observer ID
     * @param minReqState - The minimum requested state in game, expected to be matched of the
     *                    player, reported from the server
     * @param callCost    - The minimum amount of chips needed to CALL
     */
    @Override
    public void updateTurnAndOptions(int playerID, String playerName, States minReqState, double callCost)
    {

        Scanner stateScan = new Scanner(System.in);
        Scanner raiseScan = new Scanner(System.in);

        double playerBet = 0;

        if (state == States.BIG)
        {
            playerBet = bigB;
        }
        if (state == States.SMALL)
        {
            playerBet = smallB;
        }

        if (playerID == observerID)
        {

            int i = 0;
            System.out.println(name + "s turn now! Please make a move");

            switch (minReqState)
            {
                case CHECK:
                    System.out.println("You can " + States.FOLD + " " + States.CHECK + " " + States.RAISE + " " + States.ALL_IN);
                    break;
                case RAISE:
                    System.out.println("You can " + States.FOLD + " " + States.CALL + " " + States.RAISE + " " + States.ALL_IN);
                    break;
                case ALL_IN:
                    System.out.println("You can " + States.FOLD + " " + States.CALL + " " + States.RAISE + " " + States.ALL_IN);
            }

            String playerMove = "";
            int rightResponse = 0;

            if (minReqState != States.BIG && minReqState != States.SMALL)
            {
                while (rightResponse == 0)
                {
                    try
                    {
                        if (stateScan.hasNextLine())
                        {
                            playerMove = stateScan.nextLine();
                            playerMove = playerMove.toUpperCase();
                        }

                    }
                    catch (IllegalFormatException e)
                    {
                        System.out.println("Please use a valid move");
                        break;
                    }

                    double raise = 0;

                    // (minReqState == (States.FOLD)
                    // (minReqState == (States.CHECK)

                    switch (playerMove)
                    {

                        case "FOLD":
                            state = States.FOLD;
                            bet(playerBet);
                            rightResponse = 1;
                            break;

                        case "CHECK":
                            if (minReqState == States.CHECK)
                            {
                                state = States.CHECK;
                                bet(playerBet);
                                rightResponse = 1;
                            }
                            else
                            {
                                System.out.println("You cannot Check!");
                            }
                            break;


                        case "CALL":
                            if ((minReqState == States.BIG)    ||
                                (minReqState == States.CALL)   ||
                                (minReqState == States.RAISE)  ||
                                (minReqState == States.ALL_IN) )
                            {

                                if (wealth > callCost)
                                {
                                    state = States.CALL;
                                    playerBet = callCost;
                                }
                                else if (wealth <= callCost)
                                {
                                    state = States.ALL_IN;
                                    playerBet = wealth;
                                }
                                bet(callCost);
                                rightResponse = 1;
                            }
                            else
                            {
                                System.out.println("You cannot Call!");
                            }
                            break;

                        case "RAISE":
                            state = States.RAISE;
                            System.out.println("How much do you want to bet?");
                            raise = raiseScan.nextDouble();
                            if (wealth > (callCost + raise))
                            {
                                playerBet = callCost + raise;
                            }
                            else if (wealth <= (callCost + raise))
                            {
                                state = States.ALL_IN;
                                playerBet = wealth;
                            }
                            bet(playerBet);
                            rightResponse = 1;
                            break;

                        case "ALL_IN":
                            state = States.ALL_IN;
                            playerBet = wealth;
                            bet(playerBet);
                            rightResponse = 1;
                            break;

                        default:
                            System.out.println("Please select a valid move");
                            break;
                    }
                }
                //stateScan.close();
                //raiseScan.close();

            }
            else
            {
                bet(playerBet);
            }
        }
    }


    /**
     * Play the big and small blinds
     *
     * @param dealerID - The player ID for the dealer for the round
     * @param bigID    - The player ID for the big blind for the round
     * @param smallID  - The player ID for the small blind for the round
     * @param big      - The amount of chips big blind costs for the round
     * @param small    - The amount of chips small blind costs for the round
     */
    @Override
    public void updateDealerBigSmalBlinds(int dealerID, int smallID, int bigID, double small, double big)
    {
        dealer = false;

        if (observerID == dealerID)
        {
            dealer = true;
        }

        if (bigID == observerID)
        {
            state = States.BIG;
            bigB = big;
//			bet(big);
        }
        else if (smallID == observerID)
        {
            smallB = small;
            state = States.SMALL;
//			bet(small);
        }

    }


    /**
     * Add the chips won to the winning players wealth and prompt the winner of the current for the round to the game
     *
     * @param playerIds   - The players id
     * @param playerNames - The players username
     * @param winningPot  - The pot player have won
     */
    @Override
    public void updateWinner(int[] playerIds, String[] playerNames, double winningPot)
    {
        for (int i = 0; i < playerIds.length; i++)
        {
            if (playerIds[i] == observerID && playerNames[i] == name)
            {
                wealth += winningPot;
            }
            System.out.println("Winner is " + playerNames[i] + " and won " + winningPot);
        }
    }

    /**
     * The generic function which handles chips transactions between the player and server
     * It also updates the server with all the actions players does
     *
     * @param playerBet - The amount of chips player bets, when Call, Raise or All In
     */
    public void bet(double playerBet)
    {
        if (wealth >= playerBet)
        {
            gameManager.severUpdatePot(observerID, name, playerBet, state);
            wealth -= playerBet;
        }
        else
        {
            //Player goes ALL_IN if chips(wealth) are not enough to call or raise
            gameManager.severUpdatePot(observerID, name, wealth, States.ALL_IN);
            wealth = 0;
        }
    }


    /**
     * Prompt the game with the cards on the table
     *
     * @param tableCards - The game cards on the table
     */
    @Override
    public void flipOfCardT(Card[] tableCards, int amountOfCards)
    {
        // for (int i = 0; i < amountOfCards; i++) {
        //    System.out.println(tableCards[i].getRank() + " " + tableCards[i].getSuit());
        // }
    }

    /**
     * Fold as per requested from the server
     */
    @Override
    public void foldRequestFromServer()
    {
        state = States.FOLD;
    }

}

//TODO: Let the server know when a player have no chips left, 
//so that server can unsubscribe him from the game
