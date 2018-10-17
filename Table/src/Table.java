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
    private Card burnedCard;
    
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
        burnedCard = deck.getTopCard();
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
                upcomingCards = UpcomingCards.FLOP;
                break;
            default: 
                // The state machine should never be in this state. 
                break;
        }
   }
   
    /**
    * Get the cards which are shown on the table. 
    * @return The all flopped cards. 
    */
   public Card[] getAllFloppedCards()
   {
       return turnedCards;
   }
   
   /**
    * Get one of the cards on the table. 
    * @return A flopped card. 
    * @param index Which one of the five cards (0 - 4) that will be returned.
    */
   public Card getFloppedCard(int index)
   {
       try{
           return turnedCards[index];
       }
       catch(ArrayIndexOutOfBoundsException e)
       {
           System.out.println("Warning: ArrayIndexOutOfBoundsException");
       }
       return null;
   }
           
    /**
    * Move the big and small blind to the next players. 
    */
   public void moveBlinds()
   {
       this.players.moveBlinds();
   }
   
    /**
    * Move the dealer button to the next player. 
    */
   public void moveDealerButton()
   {
       this.players.moveDealerButton();
   }
   
    /**
    * Populating players to the poker table. 
    * @param pPlayers The players participating in the poker game.
    */
   public void populateTable(Players pPlayers)
   {
       this.players = pPlayers;
   }
      
    /**
    * Remove the flop, turn and river from the table. This is simulated by 
    * assigning all those cards to cards to null. 
    */
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