package common;

/**
 * <h1> Class used for creating a poker hand of five cards, for each player. </h1>
 */
public class Hand {

    /** The five cards of the hand. */
    private Card[] cards;
    /** The ranking of the hand. */
    private HandRank rank;

    /**
     * Constructor.
     */
    public Hand()
    {
        cards = new Card[5];
        rank = HandRank.NOTHING;
    }

    /**
     * Get the ranking of the poker hand.
     * @return The ranking of the poker hand.
     */
    public HandRank getRank() { return rank; }

    /**
     * Set the ranking of the poker hand.
     * @param handRank The ranking of the poker hand.
     */
    public void setRank(HandRank handRank)
    {
        rank = handRank;
    }

    /**
     * Get the cards in the poker hand.
     * @return The cards in the poker hand.
     */
    public Card[] getCards(){ return cards; }

    /**
     * Set the cards of the poker hand.
     * @param cardArray The cards of the poker hand.
     */
    public void setCards(Card[] cardArray){

        cards[0] = cardArray[0];
        cards[1] = cardArray[1];
        cards[2] = cardArray[2];
        cards[3] = cardArray[3];
        cards[4] = cardArray[4];
    }

    /**
     * Remove the cards of the poker hand.
     */
    public void resetHand()
    {
        for(int i=0; i<5; i++){
            cards[i] = null;
        }
        rank = HandRank.NOTHING;

    }

}
