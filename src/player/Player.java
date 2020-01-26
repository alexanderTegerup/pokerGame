package player;

import common.*;


public class Player {

    public Hand hand;

    private HoleCards holeCards;
    private String name;
    private int chips;
    private int chipsOnTable;

    private int id;
    private Blind blind;
    private boolean dealer;
    private PlayerState state;
    private PlayerMove move;


    public Player(String playerName, int initChips, int playerID) {
        name = playerName;
        chips = initChips;
        id = playerID;
        hand = new Hand();

        blind = Blind.NONE;
        dealer = false;
        state = PlayerState.IN;
        move = PlayerMove.NONE;
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
    public int getChips() {
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

        if (lostChips < 0) {
            System.out.println("You cannot remove negative chips! ");
        }
        else if (chips >= lostChips){
            chips -= lostChips;
        }
        else{
            System.out.println("You have " + chips + " chips, so you cannot remove " + lostChips + " chips. ");
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

    public PlayerMove getMove() { return move; }
    public void setMove(PlayerMove nextMove) { move = nextMove; }

    public int getChipsOnTable() {return chipsOnTable;}
    /**
     * This function takes chips the player has and makes them to chips the player has bet.
     */
    public void moveChipsToTable(int chips_) {

        if (chips_ < 0) {
            System.out.println("You cannot bet negative chips! ");
        }
        else if (chips >= chips_){
            chips -= chips_;
            chipsOnTable += chips_;
        }
        else{
            System.out.println("You have " + chips + " chips, so you cannot bet " + chips_ + " chips. ");
        }

    }

    /**
     * This function returns the chips the player has bet and sets the chips the player has bet to zero.
     */
    public int takeChipsOnTable() {
        int tmpChips = chipsOnTable;
        chipsOnTable = 0;
        return tmpChips;
    }
}