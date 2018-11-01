import java.util.ArrayList;

public interface Observer {

    public void updateLastPlayersMove(String playerName, States move);

    public void dealCards(Card card1, Card card2);

    public void updateTurnAndOptions(int player, States minimumState, double raise);

    public void updateDealerBigSmalBlinds(int dealerID, int bigID, int smallID, double big, double small);

    public void updateWinner(int playername, double winningPot);

    public void updatePot(double currentPot);

    public void CurrentTurnPotRaises(double raises);

    public void flipOfCardT(ArrayList<Card>());

    public void updateFoldFromServer();
}
