/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

/**
 *
 * @author ATESXB
 */
public class Table {

    private enum DealerState {
        FLOP,
        TURN,
        RIVER,
        ALL_CARDS_ON_TABLE
    }
    
    private DealerState dealerState;
    
    public Deck deck;
    public Card[] turnedCards;
    
    
    public String addDeck()
    {
        // Enter code here ...
        return "A deck is added on the table\n";
    }
 
    
   public void turnCard()
   {   
        switch(dealerState)
        {
            case FLOP : 
                dealerState = DealerState.TURN;
                break;
            case TURN : 
                dealerState = DealerState.RIVER;
                break;
            case RIVER : 
                dealerState = DealerState.ALL_CARDS_ON_TABLE;
                break;
        }
   }
   
   public void moveBlinds()
   {
       
   }
   
    /**
    * 
    */
   public void moveDealerButton()
   {
       
   }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {   
       
    }
    
}
