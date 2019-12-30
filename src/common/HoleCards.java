package common;

public class HoleCards
{

    private Card card1;
    private Card card2;
    private HandRank rank;


    public HoleCards(Card c1, Card c2)
    {
        card1 = c1;
        card2 = c2;
        rank = HandRank.NOTHING;
    }

    public void setRank(HandRank handRank)
    {
        rank = handRank;
    }

    public Card getCard1() { return card1; }
    public Card getCard2() { return card2; }
    public HandRank getRank() { return rank; }

}
