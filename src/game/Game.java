package game;

//import common.*;
import player.Player;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Scanner;

import common.Card;

public class Game
{
    private double bigBlindCost;
    private double smallBlindCost;
    private int iD = 0;
    private double wealth = 1000;
    //private int maxAmountPlayers;

    private PokerRules pokerRules;
    
    private Card[] tableCards;
    
    ArrayList<Player> players;


public void login()
{
    Scanner nameScan = new Scanner(System.in);
    System.out.println("Enter your name: ");
    String name = nameScan.nextLine();

    players.add(new Player(name, wealth, iD++));

}


private void play() 
{
    // TODO Auto-generated method stub
    
}


}


public static void main(String[] args)
{
    Game game = new Game();
    for (int i=0; i<5; i++)
    {
        game.login();
    }
    
    game.play();
}