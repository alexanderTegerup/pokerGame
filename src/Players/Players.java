/**
 * 
 */
package Players;

import java.util.ArrayList;
import Player.*;

/**
 * @author ABXGD2
 *
 */

public class Players{
	
	
	static ArrayList<Player> playerlist = new ArrayList<Player>();
	
	/**
	 * Create a new player
	 * 
	 * @author ABXGD2
	 *
	 */
	public static void CreatePlayer(String name, double wealth)
	{
		Player newP = new Player();
		newP.Name = name;
		newP.Wealth = wealth;
		playerlist.add(newP);
	}

	
	/**
	 * Delete a player
	 * 
	 * @author ABXGD2
	 *
	 */
	public static void DeletePlayer(String name)
	{
		
		playerlist.remove("name");
	}
	
	
	/**
	 * Show all players
	 * 
	 * @author ABXGD2
	 *
	 */
	public static void showAllPlayers()
	{
		
			for(Player p:playerlist)
				System.out.println(p.Name);	
	}



	/**
	 * Show a player
	 * 
	 * @author ABXGD2
	 *
	 */
	public void showPlayer(Player p)
	{
		
		System.out.println(p.Name);
		System.out.println(p.Wealth);
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CreatePlayer("Aziz", 200.22);
		CreatePlayer("Mario", 20000.002);
		CreatePlayer("Alex", 555.5);
		CreatePlayer("Bojan", 999.99);
		
		//showPlayer(Aziz);
		
		showAllPlayers();
		
		DeletePlayer("Aziz");
		DeletePlayer("Mario");
		
		showAllPlayers();
		
		

	}

}
