package player;

import common.Card;
import common.HandRank;

public class Hand
{

    private Card card1;
    private Card card2;
    private HandRank rank;


    public Hand(Card c1, Card c2)
    {
        card1 = c1;
        card2 = c2;
        rank = HandRank.NOTHING;
    }


    public Card getCard1()
    {
        return card1;
    }


    public Card getCard2()
    {
        return card2;
    }


    public void setRank(HandRank playerRank)
    {
        rank = playerRank;
    }


    public HandRank getRank()
    {
        return rank;
    }

}
