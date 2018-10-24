/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *<h1> Stub class </h1>
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
    private Card cardBelow; 
    
    public Card(Suit suit, Rank rank, Card cardBelow){
        
        this.suit      = suit;
        this.rank      = rank; 
        this.cardBelow = cardBelow; 
    }
    
}
