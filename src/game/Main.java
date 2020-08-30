package game;

/**
 * <h1> Class that contains the main method. </h1>
 */
public class Main {

    public static void main(String[] args)
    {
        Game game = new Game();

        int numberOfPlayers = 4;
        int playerId = 0;

        for (int i=0; i < numberOfPlayers; i++)
        {
            game.login(playerId++);
        }

        game.play();
    }
}
