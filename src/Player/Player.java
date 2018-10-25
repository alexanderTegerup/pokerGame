/**
 * 
 */
/**
 * @author ABXGD2
 *
 */
package Player;

public class Player
{

	public enum States
	{
		GO,
		WAITING,
		CHECK,
		FOLD,
		CALL,
		RAISE,
		ALL_IN,
		OUT;
	}


	public enum Blinds
	{
		BIG,
		SMALL,
		NONE;
	}


	public int card1;
	public int card2;
	public String Name;
	public double Wealth;
	public States State = States.WAITING;
	public Blinds Blind = Blinds.NONE;
}