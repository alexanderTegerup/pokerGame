package player;

import common.*;


public class Player {

    public Hand hand;

    private HoleCards holeCards;
    private String name;
    private int chips;
    private int id;
    private Blind blind;
    private boolean dealer;
    private PlayerState state;


    public Player(String playerName, int initChips, int playerID) {
        name = playerName;
        chips = initChips;
        id = playerID;
        hand = new Hand();

        blind = Blind.NONE;
        dealer = false;
        state = PlayerState.MUST_MAKE_MOVE;
    }

    /**
     * Returns the hole cards of the player.
     */
    public HoleCards getHoleCards() {
        return holeCards;
    }

    /**
     * Sets the hole cards of the player.
     */
    public void setHoleCards(Card card1, Card card2){
        holeCards = new HoleCards(card1, card2);
    }

    /**
     * Get the players name.
     *
     * @return name - Player's Username.
     */
    public String getName() {
        return name;
    }


    /**
     * Get the number of chips the player has.
     *
     * @return chips Amount of player's current chips.
     */
    public double showChips() {
        return chips;
    }

    /**
     * Give chips to the player.
     */
    public void addChips(int newChips) {

        try {
            chips += newChips;
            if (newChips <= 0) {
                throw new Exception("Trying to add negative chips");
            }
        } catch (Exception exc) {
            System.out.println("Error: " + exc.getMessage());
        }
    }

    /**
     * Remove chips from the player.
     */
    public void removeChips (int lostChips){

        try {
            chips -= lostChips;
            if (lostChips <= 0) {
                throw new Exception("Trying to remove negative chips");
            }
        } catch (Exception exc) {
            System.out.println("Error: " + exc.getMessage());
        }
    }

    /**
     * Get the ID of the player.
     *
     * @return ID - Player's ID
     */
    public int getID () {
        return id;
    }

    /**
     * Get the blind of the player.
     *
     * @return blind - Player's blind
     */
    public Blind getBlind () {
        return blind;
    }
    /**
     * Set the blind of the player.
     */
    public void setBlind (Blind playerBlind){
        blind = playerBlind;
    }

    public boolean isDealer(){ return dealer; }
    public void setDealer(boolean deal){ dealer = deal; }

    public PlayerState getState(){ return state; }
    public void setState(PlayerState newState){ state = newState; }

}