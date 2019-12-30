package game;

/* Java lib */
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

/* Own modules */
import common.HoleCards;
import common.Card;
import common.PlayerMove;

import player.Player;


public class Game {

    private int bigBlindCost;
    private int smallBlindCost;

    private boolean gameFinished = false;
    private boolean roundFinished = false;

    int pot;
    int sidePot;

    Deck deck;
    private PokerRules pokerRules;
    private Card[] communityCards;
    
    ArrayList<Player> players ;
    int numberOfPlayers;

    Scanner moveScan,nameScan;

    /**
     * A no argument constructor.
     */
    public Game(){
        players = new ArrayList();
        deck = new Deck();
        moveScan = new Scanner(System.in);
        nameScan = new Scanner(System.in);
    }

    /**
     * Create the players of the poker game.
     */
    public void login(int id) {

        int initChips = 1000;

        System.out.println("Enter your name: ");
        String name = nameScan.nextLine();

        players.add(new Player(name, initChips, id));
    }

    /**
     * Start the poker game.
     */
    public void play() {


        while (!gameFinished) {
            /* A new round will begin. */
            /* Deal new cards to the players, shuffle the deck, update the numbers of players etc... */
            numberOfPlayers = players.size();
            dealHoleCards();

            /* Start a new round */
            playRound();

            gameFinished = true;
        }
    }

    /**
     * Play one round of poker.
     */
    private void playRound() {
        PlayerMove nextMove;
        while (!roundFinished) {

            nextMove = getNextMoveFromPlayer();
            System.out.println("Player " + players.get(0).getName() + " did the move " + nextMove);
            roundFinished = true;
        }

    }


    /**
     * Gives each player two cards (i.e. the hole cards).
     */
    private void dealHoleCards() {

        Card card1;
        Card card2;
        HoleCards cards;
        for(int i=0; i < numberOfPlayers; i++)
        {
            card1 = deck.getTopCard();
            card2 = deck.getTopCard();
            players.get(i).setHoleCards(card1, card2);

            cards = players.get(i).getHoleCards();
            System.out.println("Player " + players.get(i).getName() + " has the cards: ");
            System.out.println("Card 1: " + cards.getCard1().getRank() + " " + cards.getCard1().getSuit());
            System.out.println("Card 2: " + cards.getCard2().getRank() + " " + cards.getCard2().getSuit() + "\n");
        }

    }

    /**
     * Deal the cards on the table (i.e. the community cards).
     */
    private void dealCommunityCards(){
        
    }

    /**
     * Update the main pot.
     * @param chips - number of chips added to the pot.
     */
    private void updatePot(int chips) {
        pot += chips;
    }

    /**
     * Update the side pot.
     * @param chips - number of chips added to the side pot.
     */
    private void updateSidePot(int chips) {
        sidePot += chips;
    }

    /**
     * Move big and small blind to the next players.
     */
    private void moveBlinds(){

    }

    /**
     * Update the value of big and small blind.
     */
    private void updateBlinds(){

    }

    /**
     * Read in the move a player want to make.
     *
     * @return The move of a player.
     */
    private PlayerMove getNextMoveFromPlayer(){

        System.out.println("Enter your next move. You can: \n" +
                " 1. Fold,\n" +
                " 2. Check,\n" +
                " 3. Call,\n" +
                " 4. Raise,\n" +
                " 5. Go all in ");

        int move = moveScan.nextInt();
        PlayerMove[] possibleMoves = PlayerMove.values();

        return possibleMoves[move-1];
    }

    /**
     * Make a move a player said she/he wanted to make.
     */
    private void makePlayersMove(){

    }

}



