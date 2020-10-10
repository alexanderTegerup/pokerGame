package player;

import common.*;

/**
 * <h1> Class used for instantiating the player objects of the poker game. </h1>
 */
public class Player {

    /** A poker hand the player has. */
    public Hand hand;
    /** The hole cards of the player. */
    private HoleCards holeCards;
    /** The name of the player. */
    private String name;
    /** The amount of chips the player has. */
    private int chips;
    /** The amount of chips the player has currently bet. */
    private int chipsOnTable;
    /** Unique id of the player. */
    private int id;
    /** States if the player has to put in small blind, big blind or no blind at all. */
    private Blind blind;
    /** States if the player is dealer. */
    private boolean dealer;
    /** The state the player is currently in. */
    private PlayerState state;
    /** The move the player choose to make. */
    private PlayerMove move;

    /**
     * Constructor.
     * @param playerName Name of the player
     * @param initChips The number of chips the player has at the beginning of the game
     * @param playerID Unique id of the player.
     */
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
     * @return The hole cards of the player.
     */
    public HoleCards getHoleCards() {
        return holeCards;
    }

    /**
     * Sets the hole cards of the player.
     * @param card1 One of the hole cards of the player
     * @param card2 One of the hole cards of the player
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
     * @param newChips The amount of chips that is given to the player.
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
     * @param lostChips The amount of chips that is removed from the player.
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
     * @param playerBlind The blind of the player (small, big or none)
     */
    public void setBlind (Blind playerBlind){
        blind = playerBlind;
    }

    /**
     * @return True if the player is dealer, otherwise false.
     */
    public boolean isDealer(){ return dealer; }

    /**
     * Set if the player shall be dealer or not.
     * @param deal True if the player shall be dealer, otherwise false.
     */
    public void setDealer(boolean deal){ dealer = deal; }

    /**
     * Get the state the player is currently in (in, all in, fold or lost)
     * @return The state of the player
     */
    public PlayerState getState(){ return state; }

    /**
     * Set the state the player shall switch to (in, all in, fold or lost)
     * @param newState The state the player shall switch to
     */
    public void setState(PlayerState newState){ state = newState; }

    /**
     * Get the move the player has chosen to make
     * @return The move of the player
     */
    public PlayerMove getMove() { return move; }

    /**
     * Set the move the player has chosen to make
     * @param nextMove The move of the player
     */
    public void setMove(PlayerMove nextMove) { move = nextMove; }

    /**
     * Show the amount of chips the player currently has bet. Note, this method does not remove any chips from the
     * player.
     * @return The amount of chips the player has bet at the moment.
     */
    public int getChipsOnTable() {return chipsOnTable;}

    /**
     * This function takes chips the player has and makes them to chips the player has bet.
     * @param chips_ The number of chips the player shall bet.
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
     * @return The chips the player has bet.
     */
    public int takeChipsOnTable() {
        int tmpChips = chipsOnTable;
        chipsOnTable = 0;
        return tmpChips;
    }
}