package remove_later;

/**
 * subject interface implemented for adding and removing players (observers) from the poker game
 */
public interface Subject
{

    public void register(Observer o);

    public void unregister(Observer o);

}
