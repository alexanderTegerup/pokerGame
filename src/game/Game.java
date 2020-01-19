package game;

/* Java lib */
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

/* Own modules */
import common.*;

import player.Player;

import static common.PlayerMove.*;


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
    private int minimumRaise;

    private int playedRounds;
    int pot;
    int sidePot;
    private boolean gameFinished = false;
    private boolean somebodyHasRaised = false;


    Deck deck;
    private PokerRules pokerRules;
    private Card[] communityCards;
    ArrayList<Player> players ;
    int numberOfPlayers;
    Scanner stringScan, intScan;


    /**
     * A no argument constructor.
     */
    public Game(){

        players = new ArrayList();
        deck = new Deck();
        communityCards = new Card[5];
        pokerRules = new PokerRules();

        playedRounds = 0;
        bettingRound = BettingRound.PRE_FLOP;
        stateOfPlayers = StateOfPlayers.PLAYERS_MAKING_MOVES;

        smallBlind = 5;
        bigBlind = smallBlind * 2;
        minimumRaise = bigBlind;

        stringScan = new Scanner(System.in);
        intScan = new Scanner(System.in);
    }

    /**
     * Create the players of the poker game.
     */
    public void login(int id) {

        int initChips = 1000;

        System.out.println("Enter your name: ");
        String name = stringScan.nextLine();

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
            moveBlinds();
            updateDealer();
            for(Player player : players){
                player.setState(PlayerState.IN);
            }

            /* Start a new round */
            playRound();

            gameFinished = true;
        }
    }

    /**
     * Play one round of poker.
     */
    private void playRound() {

        boolean roundFinished = false;
        int[] winnerId;

        while (!roundFinished) {

            switch (stateOfPlayers) {

                case PLAYERS_MAKING_MOVES:
                    stateOfPlayers = StateOfPlayers.MOVES_DONE;
                    makeMoves();
                    stateOfPlayers = StateOfPlayers.MOVES_DONE;
                    break;

                case MOVES_DONE:
                    showCommunityCards();
                    updateBettingRound();

                    if (BettingRound.SHOWDOWN == bettingRound){
                        stateOfPlayers = StateOfPlayers.ALL_PLAYERS_FINISHED;
                    }
                    else {
                        stateOfPlayers = StateOfPlayers.PLAYERS_MAKING_MOVES;
                    }
                    break;

                case ALL_PLAYERS_FINISHED:

                    for( Player player : players ){
                        if( player.getState() == PlayerState.IN  ||
                            player.getState() == PlayerState.ALL_IN )
                        {
                            setHand( player );
                        }
                    }

                    for (Player player : players){
                        System.out.println("Player " + player.getName() + " has cards ");
                        System.out.println(player.getHoleCards().getCard1().getRank() + " " + player.getHoleCards().getCard1().getSuit() );
                        System.out.println(player.getHoleCards().getCard2().getRank() + " " + player.getHoleCards().getCard2().getSuit());
                        System.out.println("The player " + player.getName() + " has hand " + player.hand.getRank() +"\n");
                    }
                    winnerId = determineWinner();
                    System.out.println("The winner is: " + winnerId[0]);
                    roundFinished = true;

                    break;
                default:
                    break;
            }
        }
    }


    /**
     * Gives each player two cards (i.e. the hole cards).
     */
    private void dealHoleCards() {

        Card card1;
        Card card2;
        HoleCards cards;
        for(Player player : players)
        {
            card1 = deck.getTopCard();
            card2 = deck.getTopCard();
            player.setHoleCards(card1, card2);

            cards = player.getHoleCards();
            System.out.println("Player " + player.getName() + " has the cards: ");
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
                System.out.println("\n--- The FLOP ---\n");
                System.out.println("Cards on table: \n" );
                for(int i=0; i<3; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                System.out.println( "\n");
                break;

            case TURN:
                System.out.println("\n--- The TURN ---\n");
                System.out.println("Cards on table: \n" );
                for(int i=0; i<4; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                System.out.println( "\n");
                break;

            case RIVER:
                System.out.println("\n--- The RIVER ---\n");
                System.out.println("Cards on table: \n" );
                for(int i=0; i<5; i++){
                    System.out.println(communityCards[i].getRank() + " " + communityCards[i].getSuit());
                }
                System.out.println( "\n");
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

    private void updateDealer(){

    }


    /**
     * Make the moves of the players.
     */
    private void makeMoves(){

        PlayerMove move;
        int playersTurn = 0;
        int requiredDecisionsMade = 0;

        while(requiredDecisionsMade < numberOfPlayers) {

            Player player = players.get(playersTurn);

            if ( PlayerState.IN == player.getState() ) {

                chooseNextMove(player);
                move = player.getMove();

                switch (move) {

                    case CALL:
                        player.setState(PlayerState.IN);
                        break;
                    case FOLD:
                        player.setState(PlayerState.FOLD);
                        break;
                    case CHECK:
                        player.setState(PlayerState.IN);
                        break;
                    case RAISE:
                        player.setState(PlayerState.IN);
                        raise(player);
                        requiredDecisionsMade = 0;
                        break;
                    case GO_ALL_IN:
                        player.setState(PlayerState.ALL_IN);
                        goAllIn(player);
                        requiredDecisionsMade = 0;
                        break;

                }
                System.out.println("Player " + player.getName() + " did the move " + move);
            }
            requiredDecisionsMade++;
            playersTurn = (playersTurn+1) % numberOfPlayers;
        }

    }

    /**
     * The player chooses the move (among the possible moves) she/he wants to make.
     */
    private void chooseNextMove(Player player){

        int playersChips = player.getChips();
        ArrayList<PlayerMove> possibleMoves = new ArrayList<>();

        int chosenMove;
        PlayerMove nextMove;

        if ( (!somebodyHasRaised) && (playersChips > minimumRaise) ){
            possibleMoves.add(FOLD);
            possibleMoves.add(CHECK);
            possibleMoves.add(RAISE);
            possibleMoves.add(GO_ALL_IN);

            System.out.println("Enter your next move. You can: \n" +
                    " 1. Fold,\n" +
                    " 2. Check,\n" +
                    " 3. Raise,\n" +
                    " 4. Go all in ");
        }
        else if ( (!somebodyHasRaised) && (playersChips <= minimumRaise) ){
            possibleMoves.add(FOLD);
            possibleMoves.add(CHECK);
            possibleMoves.add(GO_ALL_IN);

            System.out.println("Enter your next move. You can: \n" +
                    " 1. Fold,\n" +
                    " 2. Check,\n" +
                    " 3. Go all in ");
        }
        else if ( (somebodyHasRaised) && (playersChips > (2* minimumRaise)) ){
            possibleMoves.add(FOLD);
            possibleMoves.add(CALL);
            possibleMoves.add(RAISE);
            possibleMoves.add(GO_ALL_IN);

            System.out.println("Enter your next move. You can: \n" +
                    " 1. Fold,\n" +
                    " 2. Call,\n" +
                    " 3. Raise,\n" +
                    " 4. Go all in ");
        }
        else if ( (somebodyHasRaised) && (playersChips > (minimumRaise)) ){
            possibleMoves.add(FOLD);
            possibleMoves.add(CALL);
            possibleMoves.add(GO_ALL_IN);

            System.out.println("Enter your next move. You can: \n" +
                    " 1. Fold,\n" +
                    " 2. Call,\n" +
                    " 3. Go all in ");
        }
        else{
            possibleMoves.add(FOLD);
            possibleMoves.add(GO_ALL_IN);

            System.out.println("Enter your next move. You can: \n" +
                    " 1. Fold,\n" +
                    " 2. Go all in ");
        }

        chosenMove = intScan.nextInt();
        nextMove = possibleMoves.get(chosenMove-1);
        player.setMove(nextMove);
    }

    private void raise(Player player){

        if (player.getChips() >= minimumRaise){
            System.out.println("Enter the amount you want to bet. The minimum amount you can bet is " + minimumRaise);
            int bet = intScan.nextInt();
            while (bet < minimumRaise){
                System.out.println("That is not enough chips, you have to bet at least " + minimumRaise);
                System.out.println("How much do you want to bet?");
                bet = intScan.nextInt();
            }
            player.removeChips(bet);
            somebodyHasRaised = true;
            minimumRaise = bet; // In this app, the rules are that if you want to make a raise, you have to raise at least
        }                     // as much as the previous raise.
        else{
            System.out.println("You don't have enough chips to make a raise. Do you want to go all in?");
            System.out.println("1: Yes.   2: No. ");
            int allIn = intScan.nextInt();
            if (1 == allIn){
                goAllIn(player);
            }
        }

    }

    private void goAllIn(Player player){
        int allChips = player.getChips();
        player.removeChips(allChips);
        somebodyHasRaised = true;
    }


    /**
     * Method that computes the rank of each possible combination of the five cards on the table plus the two cards a
     * player has. The highest of those ranks is saved in the attribute 'rank' in the hand those two cards belong to.
     */
    public void setHand(Player player)
    {
        HoleCards holeCards = player.getHoleCards();
        Card[] cardCombination = new Card[5];

        common.HandRank handRank;
        common.HandRank bestHandRank = common.HandRank.NOTHING;
        Card[] bestCards = communityCards;

        // Looping through all the possible combinations of the seven cards to find the best combination.
        for (int i = 0; i < 21; i++)
        {

            /* i=0:9 tests all combinations with two cards from the player and three cards from the table
             *  (The number of combinations are 5 choose 3 = 10) */
            if (i < 5)
            {
                cardCombination[0] = communityCards[i % 5];
                cardCombination[1] = communityCards[(i + 1) % 5];
                cardCombination[2] = communityCards[(i + 2) % 5];
                cardCombination[3] = holeCards.getCard1();
                cardCombination[4] = holeCards.getCard2();
            }
            else if (i < 10)
            {
                cardCombination[0] = communityCards[i % 5];
                cardCombination[1] = communityCards[(i + 1) % 5];
                cardCombination[2] = communityCards[(i + 3) % 5];
                cardCombination[3] = holeCards.getCard1();
                cardCombination[4] = holeCards.getCard2();
            }
            /* Test all combinations with the first card from the player and four cards from the table
             *  (The number of combinations are 5 choose 4 = 5)*/
            else if (i < 15)
            {
                cardCombination[0] = communityCards[i % 5];
                cardCombination[1] = communityCards[(i + 1) % 5];
                cardCombination[2] = communityCards[(i + 2) % 5];
                cardCombination[3] = communityCards[(i + 3) % 5];
                cardCombination[4] = holeCards.getCard1();
            }
            /* Test all combinations with the second card from the player and four cards from the table
             *  (The number of combinations are 5 choose 4 = 5)*/
            else if(i < 20)
            {
                cardCombination[0] = communityCards[i % 5];
                cardCombination[1] = communityCards[(i + 1) % 5];
                cardCombination[2] = communityCards[(i + 2) % 5];
                cardCombination[3] = communityCards[(i + 3) % 5];
                cardCombination[4] = holeCards.getCard2();
            }
            /* Test only the community cards. */
            else
            {
                cardCombination[0] = communityCards[0];
                cardCombination[1] = communityCards[1];
                cardCombination[2] = communityCards[2];
                cardCombination[3] = communityCards[3];
                cardCombination[4] = communityCards[4];
            }

            handRank = pokerRules.determineHandRank(cardCombination);

            /* Save the highest combination */
            if (handRank.ordinal() > bestHandRank.ordinal())
            {
                bestHandRank = handRank;
                bestCards = cardCombination;
            }
            else if(handRank.ordinal() == bestHandRank.ordinal()){
                bestCards = pokerRules.getBestCardsSameRank(bestCards, cardCombination,handRank);
            }
        }

        player.hand.setRank(bestHandRank);
        player.hand.setCards(bestCards);
    }

    /**
     * Determines which player who has the best hand.
     *
     * @return The ID(s) of a player/players with the best hand.
     */


    public int[] determineWinner()
    {

        common.HandRank handRank;
        common.HandRank highestRank = common.HandRank.NOTHING;
        Card[] bestCards = communityCards;
        int playerWithBestHand = players.get(0).getID();

        for ( Player player : players)
        {
            handRank = player.hand.getRank();

            /* Save the player with the highest rank this far in the for-loop */
            if (handRank.ordinal() > highestRank.ordinal())
            {
                highestRank = handRank;
                bestCards = player.hand.getCards();
                playerWithBestHand = player.getID();
            }

            /* If several players have the same hand */
            else if ( handRank == highestRank )
            {
                bestCards = pokerRules.getBestCardsSameRank(bestCards, player.hand.getCards(), handRank);
            }

        }

        int numberOfWinners = 1;
        int[] winners = new int[numberOfWinners];
        for (int i = 0; i < numberOfWinners; i++)
        {
            winners[i] = playerWithBestHand;
        }

        return winners;
    }

}



