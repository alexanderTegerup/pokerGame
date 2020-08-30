package game;

/* Java lib */
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

/* Own modules */
import common.*;

import player.Player;

import static common.PlayerMove.*;

/**
 * <h1> Class that runs the major events in the poker game and interacts with all the players of the game. </h1>
 */
public class Game {

    private enum BettingRound {
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
    private int latestRaise;
    private int highestBid;

    private int playedRounds;
    int pot;
    int sidePot;
    private boolean gameFinished = false;
    private boolean somebodyMadeABet = false;


    Deck deck;
    private PokerRules pokerRules;
    private Card[] communityCards;
    ArrayList<Player> players;
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
     * @param id - the id each player gets.
     */
    public void login(int id) {

        int initChips = 1000;
        System.out.println("Enter your name: ");
        String name = stringScan.nextLine();

        while ( name.isEmpty() ){
            System.out.println("You have to enter a name! Please enter your name: ");
            name = stringScan.nextLine();
        }

        players.add(new Player(name, initChips, id));
    }

    /**
     * Start the poker game.
     */
    public void play() {

        while (!gameFinished) {
            /* A new round will begin. */
            /* Deal new cards to the players, shuffle the deck, update the numbers of players etc... */
            updateStateOfPlayers();
            numberOfPlayers = players.size();
            dealHoleCards();
            dealCommunityCards();
            moveBlinds();
            updateDealer();

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
                    makeMoves();
                    stateOfPlayers = StateOfPlayers.MOVES_DONE;
                    break;

                case MOVES_DONE:
                    updatePot();
                    updateBettingRound();
                    resetGlobalVariables();
                    showCommunityCards();

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
                        showStatus(player);
                        switch (player.getState()){
                            case IN:
                            case ALL_IN:
                                System.out.println("The player " + player.getName() + " has hand " + player.hand.getRank() +"\n");
                                break;
                            case FOLD:
                                System.out.println("The player " + player.getName() + " has folded. ");
                                break;
                            case LOST_THE_GAME:
                                System.out.println("The player " + player.getName() + " has lost the game. ");
                        }
                    }
                    winnerId = determineWinner();
                    System.out.println("The winner is: " + players.get(winnerId[0]).getName());
                    roundFinished = true;

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Function used for resetting variables.
     */
    private void resetGlobalVariables(){
        highestBid = 0;
        latestRaise = 0;
    }

    /**
     * Remove the players that are out of the game. Reset the state of the other players.
     */
    private void updateStateOfPlayers(){

        for (Player player : players){

            if (PlayerState.LOST_THE_GAME == player.getState() ){
                int index = players.indexOf(player);
                System.out.println("Player " + player.getName() + " lost and left the game.");

                players.remove(index);
            }
            else {
                player.setState(PlayerState.IN);
            }
        }
    }

    /**
     * Gives each player two cards (i.e. the hole cards).
     */
    private void dealHoleCards() {

        Card card1;
        Card card2;
        for(Player player : players)
        {
            card1 = deck.getTopCard();
            card2 = deck.getTopCard();
            player.setHoleCards(card1, card2);
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

    /**
     * This function prints 3, 4 or 5 community cards, depending on how far in the round the game is.
     */
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
            case SHOWDOWN:
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

    /**
     * Updates the betting round.
     */
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
     * Update the main pot by collecting all the chips on the table.
     */
    private void updatePot() {
        for (Player player : players){
            pot += player.takeChipsOnTable();
        }
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
     * @param small - new value of small blind.
     * @param big - new value of big blind.
     */
    private void updateValuesOfBlinds(int small, int big){
        smallBlind = small;
        bigBlind = big;
    }

    /**
     * Switch the dealer role, so that the next player becomes the dealer.
     */
    private void updateDealer(){

    }

    /**
     * Make the moves of the players.
     */
    private void makeMoves(){

        int playersTurn = 0;
        int requiredDecisionsMade = 0;
        somebodyMadeABet = false;
        boolean allPlayerFinished = false;

        do {
            Player player = players.get(playersTurn);

            if ( PlayerState.IN == player.getState() ) {

                chooseNextMove(player);

                switch (player.getMove()) {

                    case BET:
                        player.setState(PlayerState.IN);
                        bet(player);
                        requiredDecisionsMade = 0;
                    case CALL:
                        player.setState(PlayerState.IN);
                        call(player);
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
                        /* If a player makes a raise by going all in, all the other players have to choose if they want
                        * to call. If a raise in not made, then the other players don't have to make this choice. */
                        if (player.getChips() > latestRaise){
                            requiredDecisionsMade = 0;
                        }
                        goAllIn(player);
                        break;

                }
                System.out.println("Player " + player.getName() + " did the move " + player.getMove() + "\n\n");
            }
            requiredDecisionsMade++;
            // Update so that the next player can make his/her move.
            playersTurn = (playersTurn+1) % numberOfPlayers;

            if (requiredDecisionsMade == numberOfPlayers){
                allPlayerFinished = true;
            }

        }while (!allPlayerFinished);
    }

    /**
     * Prints the status of a player.
     * @param player - the player who gets its status printed out.
     */
    private void showStatus(Player player){

        System.out.println("\nStatus of player " + player.getName() + ":");
        System.out.println("Chips: " + player.getChips());
        System.out.println("Cards: ");
        System.out.println( player.getHoleCards().getCard1().getRank() + " " + player.getHoleCards().getCard1().getSuit() );
        System.out.println( player.getHoleCards().getCard2().getRank() + " " + player.getHoleCards().getCard2().getSuit() + "\n" );
        if (somebodyMadeABet){
            System.out.println("Somebody has made a bet. The highest bet is now " + highestBid + " chips.");
        }
    }

    /**
     * The player chooses the move (among the possible moves) she/he wants to make.
     * @param player - the player who is going to choose which move to make.
     */
    private void chooseNextMove(Player player){

        int playersChips = player.getChips();
        int chipsToPutIn = highestBid - player.getChipsOnTable();

        ArrayList<PlayerMove> possibleMoves = new ArrayList<>();
        int chosenMove;
        PlayerMove nextMove;
        boolean validMove = false;

        System.out.println("Time for " + player.getName() + " to make his/her move.");
        showStatus(player);

        do {
            if ((!somebodyMadeABet) && (playersChips > minimumRaise)) {
                possibleMoves.add(FOLD);
                possibleMoves.add(CHECK);
                possibleMoves.add(BET);
                possibleMoves.add(GO_ALL_IN);

                System.out.println("Enter your next move. You can: \n" +
                        " 1. Fold,\n" +
                        " 2. Check,\n" +
                        " 3. Bet,\n" +
                        " 4. Go all in ");
            } else if ((!somebodyMadeABet) && (playersChips <= minimumRaise)) {
                possibleMoves.add(FOLD);
                possibleMoves.add(CHECK);
                possibleMoves.add(GO_ALL_IN);

                System.out.println("Enter your next move. You can: \n" +
                        " 1. Fold,\n" +
                        " 2. Check,\n" +
                        " 3. Go all in ");
            } else if ((somebodyMadeABet) && (playersChips >= (chipsToPutIn + minimumRaise))) {
                possibleMoves.add(FOLD);
                possibleMoves.add(CALL);
                possibleMoves.add(RAISE);
                possibleMoves.add(GO_ALL_IN);

                System.out.println("You have to put in at least " + chipsToPutIn + " to join the game.");
                System.out.println("Enter your next move. You can: \n" +
                        " 1. Fold,\n" +
                        " 2. Call,\n" +
                        " 3. Raise,\n" +
                        " 4. Go all in ");
            } else if ((somebodyMadeABet) && (playersChips > (chipsToPutIn))) {
                possibleMoves.add(FOLD);
                possibleMoves.add(CALL);
                possibleMoves.add(GO_ALL_IN);

                System.out.println("You have to put in " + chipsToPutIn + " chips to call.");
                System.out.println("Enter your next move. You can: \n" +
                        " 1. Fold,\n" +
                        " 2. Call,\n" +
                        " 3. Go all in ");
            } else {
                possibleMoves.add(FOLD);
                possibleMoves.add(GO_ALL_IN);

                System.out.println("You have to go all in to join the game.");
                System.out.println("Enter your next move. You can: \n" +
                        " 1. Fold,\n" +
                        " 2. Go all in ");
            }
            chosenMove = intScan.nextInt();

            if ((chosenMove < 1) || (chosenMove > possibleMoves.size())){
                System.out.println("Invalid move.");
                possibleMoves.clear();
            }
            else {
                validMove = true;
            }
        }while ( !validMove );

        nextMove = possibleMoves.get(chosenMove-1);
        player.setMove(nextMove);
    }

    /**
     * Makes a player bet some of his/her chips.
     * @param player - the player who is making the bet.
     */
    private void bet(Player player){

        boolean validBet = false;
        int bet;

        System.out.println("The minimum bet is " + minimumRaise);
        do {
            System.out.println("Enter how much you want to bet: ");
            bet = intScan.nextInt();
            if (bet < minimumRaise) {
                System.out.println("That is not enough chips, you have to bet at least " + minimumRaise);
            }
            else if (bet >= player.getChips()) {
                System.out.println("You want to bet more than you can afford. Do you want to go all in?");
                System.out.println("1. Yes    2. No");
                int answer = intScan.nextInt();
                if (1 == answer){
                    goAllIn(player);
                    return;
                }
            }
            else{
                validBet = true;
            }
        }while (!validBet);

        player.moveChipsToTable(bet);
        highestBid = bet;
        minimumRaise = bet;
        somebodyMadeABet = true;
    }

    /**
     * Makes a player call the bet that is on the table, i.e. put ih the chips
     * needed to join the game.
     * @param player - the player who is calling the bet or calling the raise.
     */
    private void call(Player player){

        int chipsToPutIn = highestBid - player.getChipsOnTable();
        if (player.getChips() > chipsToPutIn){
            player.moveChipsToTable(chipsToPutIn);
        }
        else if (player.getChips() == chipsToPutIn){
            goAllIn(player);
        }
        else {
            System.out.println("You don't have enough chips to make a call. ");
        }
    }

    /**
     * Makes a player raise, i.e. put in even more chips than is necessary to join the game.
     * @param player - the player who is making the raise.
     */
    private void raise(Player player){

        int raise;
        int chipsToPutIn;
        boolean validRaise = false;

        do {
            System.out.println("Enter the how much you want to raise. The minimum amount you can raise is " + minimumRaise);
            raise = intScan.nextInt();
            chipsToPutIn = highestBid + raise - player.getChipsOnTable();

            if (raise < minimumRaise) {
                System.out.println("That is not enough chips, you have to raise at least " + minimumRaise);
            }
            else if (chipsToPutIn > player.getChips()) {
                System.out.println("You want to raise more than you can afford. Do you want to go all in?");
                System.out.println("1. Yes    2. No");
                int answer = intScan.nextInt();
                if (1 == answer){
                    goAllIn(player);
                    return;
                }
            }
            else {
                validRaise = true;
            }
        }while (!validRaise);

        player.moveChipsToTable(chipsToPutIn);
        highestBid += raise;
        latestRaise = raise;
        minimumRaise = latestRaise; // In this app, the rules are that if you want to make a raise, you have to
                                        // raise at least as much as the previous raise.
    }

    /**
     * Makes a player go all in.
     * @param player - the player who is going all in.
     */
    private void goAllIn(Player player){

        int allChips = player.getChips();
        player.moveChipsToTable(allChips);

        if ( player.getChipsOnTable() > highestBid){
            highestBid = player.getChipsOnTable();
            somebodyMadeABet = true;
        }
        player.setState(PlayerState.ALL_IN);
    }

    /**
     * Method that computes the rank of each possible combination of the five cards on the table plus the two cards a
     * player has. The highest of those ranks is saved in the attribute 'rank' in the attribute 'hand' of the player.
     * @param player - a player in the game.
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
            /* Determine the best card if there are two hands with the same rank. */
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

        ArrayList<Player> possibleWinners = new ArrayList();
        for ( Player player : players )
        {
            if( player.getState() == PlayerState.IN  ||
                player.getState() == PlayerState.ALL_IN )
            {
                /* Only players that have not folded or lost the game have the posibilty to win the round */
                possibleWinners.add(player);
            }
        }

        int playerWithBestHand = possibleWinners.get(0).getID();

        for ( Player player : possibleWinners)
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
