/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *<h1> Class that creates Card objects </h1>
 * Each card has a suit, rank and a reference 
 * to an other card. 
 */
public class Card {
    
    public enum Suit
    {
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS;
    }
    
    public enum Rank
    {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE;   
    }
    
    private Suit suit;
    private Rank rank;
    private Card cReference; 
    
    /**
    * A parameterized constructor which generates a card. 
    * @param suit The suit of the card.
    * @param rank The rank of the card.
    * @param cReference A reference to another card. 
    */
    public Card(Suit suit, Rank rank, Card cReference){
        
        this.suit       = suit;
        this.rank       = rank; 
        this.cReference = cReference; 
    }
    
    /** 
     * Method which returns the reference of the card object. 
     * @return The reference of the card object
     */
    public Card getReference(){
        return cReference;
    }
    
    /** 
     * Method which returns the suit of the card object. 
     * @return The suit of the card object
     */
    public Suit getSuit(){
        return this.suit;
    }
    
    /** 
     * Method which returns the rank of the card object. 
     * @return The rank of the card object
     */    
    public Rank getRank(){
        return this.rank;
    }
    
}
