package common;

import java.io.BufferedReader;
import java.util.ArrayList;

import table.Card;

/**
 * Observer interface for notifying observers when a change occurs
 */
public interface Observer
{

    public void updateLastPlayersMove(String playerName, States move);

    public void dealCards(Card card1, Card card2);

    public void updateTurnAndOptions(int player, String playerName, States minimumState, double raise);

    public void updateDealerBigSmalBlinds(int dealerID, int bigID, int smallID, double big, double small);

    public void updateWinner(int[] playerID, String[] winnerName, double winningPot);

    public void flipOfCardT(Card[] tableCards, int amountOfCards);

    public void foldRequestFromServer();

    public String getUserName();

    public int getObserverID();

    public void getHand();

}

