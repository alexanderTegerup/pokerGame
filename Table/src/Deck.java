/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
/**
 * <h1> Stub class </h1>
 * 
 */
public class Deck {
    
    private Card topCard;
    private Card cardBelow;
    private Card.Suit suit;
    private Card.Rank rank;
    private int numCardsInDeck = 52;
    
    ArrayList<Integer> randomNumbers;
    
     /**
     * A no argument constructor.
     */
    public Deck() {
        generateDeck();   
    }
    
    /**
    * Returns the card at the top of the deck. 
    * @return The card at the top of the deck. 
    */
    public Card getTopCard(){
        
        return topCard;
    }
    
    public void shuffleDeck(){
       
    }
    
    /**
    * Generates an array list with the numbers 0-51 placed in a random order. 
    * Each number in the array list maps to a unique combination of a suit and 
    * rank of a card. 
    */
    private void generateRandomNumers() {
        
        randomNumbers = new ArrayList<Integer>();
        for(int i=0; i<numCardsInDeck; i++) {
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);
    }
    
    /**
    * Generates a deck where its cards are a linked list. 
    */
    private void generateDeck() {
        
        generateRandomNumers();
        topCard = null;
        for(int index=0; index<numCardsInDeck; index++){
            
            randomSuitAndRank(index);
            cardBelow = topCard;
            topCard = new Card(suit, rank, cardBelow);
        }
    }
    
    /**
    * Generates a random combination of suit and rank which has not been 
    * generated before. 
    * @param index The index in the array with random numbers.
    */
    private void randomSuitAndRank(int index) {
        
        int randomNumber = randomNumbers.get(index);
        if (randomNumber < 13){
            suit = Card.Suit.CLUBS;
            rank = Card.Rank.values()[randomNumber];            
        }
        else if (13 <= randomNumber && randomNumber <= 25){
            suit = Card.Suit.DIAMONDS;
            rank = Card.Rank.values()[randomNumber-13];       
        }
        else if (26 <= randomNumber && randomNumber <= 38){
            suit = Card.Suit.HEARTS;
            rank = Card.Rank.values()[randomNumber-26];    
        }
        else{
            suit = Card.Suit.SPADES;
            rank = Card.Rank.values()[randomNumber-39];
        }  
    }
}