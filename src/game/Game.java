package game;

/* Java lib */
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

/* Own modules */
import common.*;

import player.Player;


public class Game {

    private enum BettingRound
    {
        PRE_FLOP,
        FLOP,
        TURN,
        RIVER,
        SHOWDOWN
    }

    public enum StateOfPlayers {
        PLAYERS_MAKING_MOVES,
        MOVES_DONE,
        ALL_PLAYERS_FINISHED
    }

    private BettingRound bettingRound;
    private StateOfPlayers stateOfPlayers;
    private int bigBlind;
    private int smallBlind;
    private int minimumBet;
    private int playedRounds;
    int pot;
    int sidePot;
    private boolean gameFinished = false;
    private boolean roundFinished = false;

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
        communityCards = new Card[5];
        playedRounds = 0;
        bettingRound = BettingRound.PRE_FLOP;
        stateOfPlayers = StateOfPlayers.PLAYERS_MAKING_MOVES;

        smallBlind = 5;
        bigBlind = smallBlind *2;
        minimumBet = bigBlind;

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
            dealCommunityCards();

            /* Start a new round */
            playRound();

            gameFinished = true;
        }
    }

    /**
     * Play one round of poker.
     */
    private void playRound() {

        while (!roundFinished) {

            switch (stateOfPlayers) {

                case PLAYERS_MAKING_MOVES:
                    makeMoves();
                    break;

                case MOVES_DONE:
                    showCommunityCards();
                    updateBettingRound();
                    break;

                case ALL_PLAYERS_FINISHED:
                    pokerRules.setHandRank();
                    break;

            }

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
        for(int player=0; player < numberOfPlayers; player++)
        {
            card1 = deck.getTopCard();
            card2 = deck.getTopCard();
            players.get(player).setHoleCards(card1, card2);

            cards = players.get(player).getHoleCards();
            System.out.println("Player " + players.get(player).getName() + " has the cards: ");
            System.out.println("Card 1: " + cards.getCard1().getRank() + " " + cards.getCard1().getSuit());
            System.out.println("Card 2: " + cards.getCard2().getRank() + " " + cards.getCard2().getSuit() + "\n");
        }

    }

    /**
     * Deal the cards on the table (i.e. the community cards).
     */
    private void dealCommunityCards(){
        for(int i=0; i<5; i++){
            communityCards[i] = deck.getTopCard();
        }
    }


    private void showCommunityCards(){

        switch (bettingRound)
        {
            case FLOP:
                System.out.println("--- The FLOP ---");
                System.out.println("Cards on table: " );
                for(int i=0; i<3; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                break;

            case TURN:
                System.out.println("--- The TURN ---");
                System.out.println("Cards on table: " );
                for(int i=0; i<4; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                break;

            case RIVER:
                System.out.println("--- The RIVER ---");
                System.out.println("Cards on table: " );
                for(int i=0; i<5; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                break;

            default:
                break;
        }
    }

    private void updateBettingRound(){

        switch (bettingRound)
        {
            case PRE_FLOP:
                bettingRound = BettingRound.FLOP;
                break;

            case FLOP:
                bettingRound = BettingRound.TURN;
                break;

            case TURN:
                bettingRound = BettingRound.RIVER;
                break;

            case RIVER:

                bettingRound = BettingRound.SHOWDOWN;
                break;

            default:
                break;
        }
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
    private void updateBlinds(int small, int big){
        smallBlind = small;
        bigBlind = big;
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
     * Make the moves of the players.
     */
    private void makeMoves(){
        PlayerMove move;
        for(int player=0; player < numberOfPlayers; player++){


            move = getNextMoveFromPlayer();
            switch (move){

                case CALL:
                    players.get(player).setState(PlayerState.IN);
                    break;
                case FOLD:
                    players.get(player).setState(PlayerState.FOLD);
                    break;
                case CHECK:
                    players.get(player).setState(PlayerState.IN);
                    break;
                case RAISE:
                    players.get(player).setState(PlayerState.IN);
                    break;
                case GO_ALL_IN:
                    players.get(player).setState(PlayerState.ALL_IN);
                    break;

            }

            System.out.println("Player " + players.get(0).getName() + " did the move " + move);


        }

    }

}



