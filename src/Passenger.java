
public class Passenger {

    private final String userName;
    private final String password;
    private final String credit;
    static final int SIZE = 20;
    static final int FIX_SIZE = 60;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCredit() {
        return credit;
    }

    public Passenger(String userName, String password, String credit) {
        this.userName = userName;
        this.password = password;
        this.credit = credit;
    }

}

