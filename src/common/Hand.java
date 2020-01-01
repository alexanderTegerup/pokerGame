package common;

public class Hand {

    private Card[] cards;
    private HandRank rank;

    public Hand()//Card c1, Card c2, Card c3, Card c4, Card c5)
    {
        cards = new Card[5];
/*
        cards[0] = c1;
        cards[1] = c2;
        cards[2] = c3;
        cards[3] = c4;
        cards[4] = c5;*/
        rank = HandRank.NOTHING;
    }

    public HandRank getRank() { return rank; }
    public void setRank(HandRank handRank)
    {
        rank = handRank;
    }

    public Card[] getCards(){ return cards; }
    public void setCards(Card[] cardArray){

        cards[0] = cardArray[0];
        cards[1] = cardArray[1];
        cards[2] = cardArray[2];
        cards[3] = cardArray[3];
        cards[4] = cardArray[4];
    }

    public void resetHand()
    {
        for(int i=0; i<5; i++){
            cards[i] = null;
        }
        rank = HandRank.NOTHING;

    }

}
