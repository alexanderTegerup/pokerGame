package remove_later;

import common.Card;
import game.Deck;

/**
 * <h1> The poker table </h1>
 * Handles the flop, the turn and the river. Those cards are kept in the array 'turnedCards'.
 * @author Alexander Tegerup
 */
public class Table
{

    private enum UpcomingCards
    {
        FLOP,
        TURN,
        RIVER;
    }

    /** Indicates if it is the flop, the turn or the river that will come up next on the table. */
    private UpcomingCards upcomingCards;
    /** The shuffled deck from which the cards in the game come from. */
    private Deck deck;
    /** The cards that are shown on the table. */
    private Card[] turnedCards;
    /** The card that is thrown away before new cards are shown on the table (this is how it is done in a real poker game).*/
    private Card burnedCard;
    /** Number of cards that are shown on the table. */
    private int amountOfCards = 0;

    /**
     * A no argument constructor.
     */
    public Table()
    {
        upcomingCards = UpcomingCards.FLOP;
        deck = new Deck();
        turnedCards = new Card[5];
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
    public void dealCard()
    {
        switch (upcomingCards)
        {
            case FLOP:
                burnCard();
                System.out.println("--- The FLOP ---");
                turnedCards[0] = deck.getTopCard();
                turnedCards[1] = deck.getTopCard();
                turnedCards[2] = deck.getTopCard();
                upcomingCards = UpcomingCards.TURN;
                amountOfCards = 3;
                break;

            case TURN:
            	System.out.println("--- The TURN ---");
                burnCard();
                turnedCards[3] = deck.getTopCard();
                upcomingCards = UpcomingCards.RIVER;
                amountOfCards++;
                break;

            case RIVER:
            	System.out.println("--- The RIVER ---");
                burnCard();
                turnedCards[4] = deck.getTopCard();
                upcomingCards = UpcomingCards.FLOP;
                amountOfCards++;
                break;
            default:
                // The state machine should never be in this state. 
                break;
        }

        for (int i = 0; i < amountOfCards; i++)
        {
            System.out.println(turnedCards[i].getRank() + " " +  turnedCards[i].getSuit());
        }

    }

    /**
     * Get the number of cards that are shown on the table.
     *
     * @return The number of cards that are shown on the table.
     */
    public int returnNrOfCards()
    {
        return amountOfCards;
    }

    /**
     * Get the cards which are shown on the table.
     *
     * @return The all flopped cards.
     */
    public Card[] showAllCards()
    {
        return turnedCards;
    }

    /**
     * Get one of the cards on the table.
     *
     * @param index Which one of the five cards (0 - 4) that will be returned.
     * @return A flopped card.
     */
    public Card showCard(int index)
    {
        try
        {
            return turnedCards[index];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Warning: ArrayIndexOutOfBoundsException");
        }
        return null;
    }

    /**
     * Remove the flop, turn and river from the table. This is simulated by
     * assigning all those cards to cards to null.
     */
    public void removeCardsFromTable()
    {
        for (int i = 0; i < 5; i++)
        {
            turnedCards[i] = null;
        }
    }

    /**
     * Returns the card at the top of the deck on the table.
     *
     * @return The card at the top of the deck on the table.
     */
    public Card getCardFromDeck()
    {
        return deck.getTopCard();
    }

}