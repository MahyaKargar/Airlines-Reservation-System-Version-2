public class Admin {
    private final String userName;
    private final String password;
    static final int SIZE = 20;
    static final int FIX_SIZE = 40;

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;

    }

    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
