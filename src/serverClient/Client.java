/**
 * 
 */
package serverClient;
import java.net.*;
/**
 * @author ABXGD2
 *
 */
public class Client 
{
	static String IP   = "127.0.0.1";
	static int    port = 3000;
	
	
	public static void main (String arg[])
	{
		try {
			Socket s = new Socket(IP, port);
			System.out.println("Connected!!!");
		} catch (Exception e){}
	}
	

}
