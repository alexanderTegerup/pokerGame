package table;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * <h1> Class that creates Card objects </h1>
 * Each card has a suit, rank and a reference
 * to an other card.
 */
public class Card
{

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

    /** The suit of the card */
    private Suit suit;
    /** The rank of the card */
    private Rank rank;
    /** A reference to the card below in the deck */
    private Card reference;

    /**
     * A parameterized constructor which generates a card.
     *
     * @param suit_      The suit of the card.
     * @param rank_      The rank of the card.
     * @param reference_ A reference to another card.
     */
    public Card(Suit suit_, Rank rank_, Card reference_)
    {

        suit = suit_;
        rank = rank_;
        reference = reference_;
    }

    /**
     * Method which returns the reference of the card object.
     *
     * @return The reference of the card object
     */
    public Card getReference()
    {
        return reference;
    }

    /**
     * Method which returns the suit of the card object.
     *
     * @return The suit of the card object
     */
    public Suit getSuit()
    {
        return suit;
    }

    /**
     * Method which returns the rank of the card object.
     *
     * @return The rank of the card object
     */
    public Rank getRank()
    {
        return rank;
    }

}
