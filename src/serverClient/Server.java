/**
 * 
 */
package serverClient;
import java.net.*;

/**
 * @author ABXGD2
 *
 */
public class Server {
	
	static int port = 3000;
	
	public static void main(String arg[])
	{
		try 
		{
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		
	
		System.out.println("Connected");
		} catch(Exception e){}
		
	}
	
}
