/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *<h1> The poker table </h1>
 * Handles the flop, turn and river. Keeps track on big and small blind and the 
 * dealer button. Keeps track of the players around the table. 
 * 
 * @author Alexander Tegerup 
 */
public class Table {

    private enum UpcomingCards {
        FLOP,
        TURN,
        RIVER;
    }
    
    private UpcomingCards upcomingCards;
    private Deck deck;
    private Card[] turnedCards;
    private Players players; 
    
    /**
     * A no argument constructor.
     */
    public Table() {
        this.upcomingCards = UpcomingCards.FLOP;
        this.deck = new Deck();
        this.turnedCards = new Card[5];
    }
    
     /**
     * A parameterized constructor which populates the table. 
     * @param pPlayers The players participating in the poker game.
     */
    public Table(Players pPlayers) {
        this.upcomingCards = UpcomingCards.FLOP;
        this.deck = new Deck();
        this.turnedCards = new Card[5];
        this.players = pPlayers;
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
    * @return The flopped cards. 
    */
   public Card[] turnCard()
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
                upcomingCards = UpcomingCards.FLOP;
                break;
            default: 
                // The state machine should never be in this state. 
                break;
        }
        return turnedCards;
   }
   
    /**
    * Move the big and small blind to the next players. 
    */
   public void moveBlinds()
   {
       Player nextPlayer = this.players.nextPlayer();
       
   }
   
    /**
    * Move the dealer button to the next player. 
    */
   public void moveDealerButton()
   {
       
   }
   
    /**
    * Populating players to the poker table. 
    * @param pPlayers The players participating in the poker game.
    */
   public void populateTable(Players pPlayers)
   {
       this.players = pPlayers;
   }
             
   public void removeCardsFromTable()
   {
       for(int i=0; i<5; i++)
       {
           turnedCards[i] = null;
       }
   }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
        
    }
    
}
