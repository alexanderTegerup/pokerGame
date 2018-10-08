/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *<h1> The poker table.</h1>
 * Handles the flop, turn and river. Keeps track on big and small blind and the 
 * dealer button. Keeps track of the players around the table. 
 * 
 * @author Alexander Tegerup 
 */
public class Table {

    private enum UpcomingCards {
        FLOP,
        TURN,
        RIVER,
    }
    
    private UpcomingCards upcomingCards;
    public Deck deck;
    public Card[] turnedCards;
    public Players players; 
    /**
     * A no argument constructor.
     */
    public Table() {
        upcomingCards = UpcomingCards.FLOP;
        deck = new Deck();
        turnedCards = new Card[5];
    }
    
    /**
    * Burns the card at the top of the deck.  
    */
    private void burnCard()
    {
        deck.getTopCard(); // Do I need to typecast the return value to void
                           // or handle the return value somehow? 
    }
    
   /**
    * Deals the flop, the turn and the river to the table. 
    */
   public void turnCard()
   {   
        switch(upcomingCards)
        {
            case FLOP: 
                burnCard();
                turnedCards[0] = deck.getTopCard();
                turnedCards[1] = deck.getTopCard();
                turnedCards[2] = deck.getTopCard();
                upcomingCards = UpcomingCards.TURN;
                break;
                
            case TURN: 
                burnCard();
                turnedCards[4] = deck.getTopCard();
                upcomingCards = UpcomingCards.RIVER;
                break;
                
            case RIVER: 
                burnCard();
                turnedCards[5] = deck.getTopCard();
                break;
            default: 
                // The state machine should never be in this state. 
                break;
        }
   }
   
    /**
    * Move the big and small blind to the next players. 
    */
   public void moveBlinds()
   {
       
   }
   
    /**
    * Move the dealer button to the next player. 
    */
   public void moveDealerButton()
   {
       
   }
   
    /**
    * Populating players to the poker table. 
    */
   public void populateTable()
   {
       
   }
                   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
       
    }
    
}
