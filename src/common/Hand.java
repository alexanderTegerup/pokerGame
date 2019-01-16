package common;

import table.Card;

public class Hand {

	private Card card1;
	private Card card2;
	private Ranks rank;


	public Hand (Card c1, Card c2) {
		card1 = c1;
		card2 = c2;
		rank = Ranks.NOTHING;
	}


	public Card getCard1() { 	
		return card1;
	}


	public Card getCard2() { 	
		return card2;
	}


	public void setRank(Ranks playerRank) { 
		rank = playerRank; 
	}


	public Ranks getRank() {
		return rank; 
	}

}
