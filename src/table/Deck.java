package table;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1> Class which generates a deck </h1>
 * Each card in the deck has a reference to the card below in the deck. This
 * means that the deck is a linked list of cards.
 */
public class Deck
{

    private Card topCard;
    private Card cardBelow;
    private Card.Suit suit;
    private Card.Rank rank;
    private int numCardsInDeck = 52;

    private ArrayList<Integer> randomNumbers;

    /**
     * A no argument constructor.
     */
    public Deck()
    {
        generateDeck();
    }

    /**
     * Returns the card at the top of the deck.
     *
     * @return The card at the top of the deck.
     */
    public Card getTopCard()
    {
        Card removedCard = topCard;
        topCard = topCard.getReference();
        return removedCard;
    }

    /**
     * Generates an array list with the numbers 0-51 placed in a random order.
     * Each number in the array list maps to a unique combination of a suit and
     * rank of a card.
     */
    private void generateRandomNumbers()
    {

        randomNumbers = new ArrayList<Integer>();
        for (int i = 0; i < numCardsInDeck; i++)
        {
            randomNumbers.add(i);
        }
        Collections.shuffle(randomNumbers);
    }

    /**
     * Generates a deck where its cards are a linked list.
     */
    private void generateDeck()
    {

        generateRandomNumbers();
        topCard = null;
        for (int index = 0; index < numCardsInDeck; index++)
        {

            randomizeSuitAndRank(index);
            cardBelow = topCard;
            topCard = new Card(suit, rank, cardBelow);
        }
    }

    /**
     * Generates a random combination of suit and rank which has not been
     * generated before.
     *
     * @param index The index in the array with random numbers.
     */
    private void randomizeSuitAndRank(int index)
    {

        int randomNumber = randomNumbers.get(index);
        if (randomNumber < 13)
        {
            suit = Card.Suit.CLUBS;
            rank = Card.Rank.values()[randomNumber];
        }
        else if (13 <= randomNumber && randomNumber <= 25)
        {
            suit = Card.Suit.DIAMONDS;
            rank = Card.Rank.values()[randomNumber - 13];
        }
        else if (26 <= randomNumber && randomNumber <= 38)
        {
            suit = Card.Suit.HEARTS;
            rank = Card.Rank.values()[randomNumber - 26];
        }
        else
        {
            suit = Card.Suit.SPADES;
            rank = Card.Rank.values()[randomNumber - 39];
        }
    }
}