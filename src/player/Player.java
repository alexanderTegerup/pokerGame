package player;

import java.util.IllegalFormatException;
import java.util.Scanner;

import common.Card;
import common.Moves;
import remove_later.GameManager;


public class Player
{

    private Hand hand;
    private String name;
    private double wealth;
    private int iD;
    private Moves move;


    public Player(String playerName, double playerWealth, int playerID)
    {
        name = playerName;
        wealth = playerWealth;
        iD = playerID;
        move = Moves.WAITING; // Ta bort denna state?
        
    }

    /**
     * Calls the function collectHandsFromPlayers to show it's hand to the game manager
     */
    public Hand getHand()
    {
    	return hand;
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
    public Moves getMove()
    {
        return move;
    }


    /**
     * Get the players Observer ID
     *
     * @return observerID - Player's observerID used of the server to track the players in the game
     */
    public int getID()
    {
        return iD;
    }
    

    /**
     * receive information on which player played what move
     *
     * @param playerName
     * @param move
     */
    public void logPlayersMove(String playerName, Moves move)
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
    public void updateTurnAndOptions(int playerID, String playerName, Moves minReqState, double callCost)
    {

        Scanner stateScan = new Scanner(System.in);
        Scanner raiseScan = new Scanner(System.in);

        double playerBet = 0;

        if (move == Moves.BIG)
        {
            playerBet = GameManager.bigblind;
        }
        if (move == Moves.SMALL)
        {
            playerBet = GameManager.smallblind;
        }

        if (playerID == iD)
        {

            int i = 0;
            System.out.println(name + "s turn now! Please make a move");

            switch (minReqState)
            {
                case CHECK:
                    System.out.println("You can " + Moves.FOLD + " " + Moves.CHECK + " " + Moves.RAISE + " " + Moves.ALL_IN);
                    break;
                case RAISE:
                    System.out.println("You can " + Moves.FOLD + " " + Moves.CALL + " " + Moves.RAISE + " " + Moves.ALL_IN);
                    break;
                case ALL_IN:
                    System.out.println("You can " + Moves.FOLD + " " + Moves.CALL + " " + Moves.RAISE + " " + Moves.ALL_IN);
            }

            String playerMove = "";
            int rightResponse = 0;

            if (minReqState != Moves.BIG && minReqState != Moves.SMALL)
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
                            move = Moves.FOLD;
                            bet(playerBet);
                            rightResponse = 1;
                            break;

                        case "CHECK":
                            if (minReqState == Moves.CHECK)
                            {
                                move = Moves.CHECK;
                                bet(playerBet);
                                rightResponse = 1;
                            }
                            else
                            {
                                System.out.println("You cannot Check!");
                            }
                            break;


                        case "CALL":
                            if ((minReqState == Moves.BIG)    ||
                                (minReqState == Moves.CALL)   ||
                                (minReqState == Moves.RAISE)  ||
                                (minReqState == Moves.ALL_IN) )
                            {

                                if (wealth > callCost)
                                {
                                    move = Moves.CALL;
                                    playerBet = callCost;
                                }
                                else if (wealth <= callCost)
                                {
                                    move = Moves.ALL_IN;
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
                            move = Moves.RAISE;
                            System.out.println("How much do you want to bet?");
                            raise = raiseScan.nextDouble();
                            if (wealth > (callCost + raise))
                            {
                                playerBet = callCost + raise;
                            }
                            else if (wealth <= (callCost + raise))
                            {
                                move = Moves.ALL_IN;
                                playerBet = wealth;
                            }
                            bet(playerBet);
                            rightResponse = 1;
                            break;

                        case "ALL_IN":
                            move = Moves.ALL_IN;
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
    public void updateDealerBigSmalBlinds(int dealerID, int smallID, int bigID, double small, double big)
    {
        if (bigID == iD)
        {
            move = Moves.BIG;
//            Gamemanager.big = big;
//			bet(big);
        }
        else if (smallID == iD)
        {
 //           smallB = small;
            move = Moves.SMALL;
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
    public void updateWinner(int[] playerIds, String[] playerNames, double winningPot)
    {
        for (int i = 0; i < playerIds.length; i++)
        {
            if (playerIds[i] == iD && playerNames[i] == name)
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
            GameManager.severUpdatePot(iD, name, playerBet, move);
            wealth -= playerBet;
        }
        else
        {
            //Player goes ALL_IN if chips(wealth) are not enough to call or raise
            GameManager.severUpdatePot(iD, name, wealth, Moves.ALL_IN);
            wealth = 0;
        }
    }


    /**
     * Prompt the game with the cards on the table
     *
     * @param tableCards - The game cards on the table
     */
    public void flipOfCardT(Card[] tableCards, int amountOfCards)
    {
        // for (int i = 0; i < amountOfCards; i++) {
        //    System.out.println(tableCards[i].getRank() + " " + tableCards[i].getSuit());
        // }
    }

    /**
     * Fold as per requested from the server
     */
    public void foldRequestFromServer()
    {
        move = Moves.FOLD;
    }

}

//TODO: Let the server know when a player have no chips left, 
//so that server can unsubscribe him from the game
