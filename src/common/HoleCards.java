package common;

/**
 * <h1> Class used for generating the hole cards for a player (i.e. the two unique cards that each player has). </h1>
 */
public class HoleCards
{
    /** The first hole card. */
    private Card card1;
    /** The second hole card. */
    private Card card2;

    /**
     * Constructor.
     * @param c1 The first hole card.
     * @param c2 The second hole card.
     */
    public HoleCards(Card c1, Card c2)
    {
        card1 = c1;
        card2 = c2;
    }

    /**
     * Get the first hole card.
     * @return The first hole card.
     */
    public Card getCard1() { return card1; }

    /**
     * Get the second hole card.
     * @return The second hole card.
     */
    public Card getCard2() { return card2; }

}
