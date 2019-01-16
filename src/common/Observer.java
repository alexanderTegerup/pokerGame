package common;

import java.util.ArrayList;

import table.Card;

/**
 * Observer interface for notifying observers when a change occurs
 */
public interface Observer {

    public void updateLastPlayersMove(String playerName, States move);

    public void dealCards(Card card1, Card card2);

    public void updateTurnAndOptions(int player, States minimumState, double raise);

    public void updateDealerBigSmalBlinds(int dealerID, int bigID, int smallID, double big, double small);

    public void updateWinner(int playername, double winningPot);

    public void bet(double playerBet);

    public void flipOfCardT(ArrayList<Card> tableCards);

    public void foldRequestFromServer();
    
    public String getUserName();
    
    public int getObserverID();
    
    public Hand getHand();
    
}

