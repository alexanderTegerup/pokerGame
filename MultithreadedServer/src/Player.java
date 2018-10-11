public class Player {

    private String userName;
    private double stakes;
    private States state;

    public Player(String uname, double gameStakes) {
        userName = uname;
        stakes = gameStakes;
        state = States.WAITING;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getStakes() {
        return stakes;
    }

    public void setStakes(double stakes) {
        this.stakes = stakes;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

}