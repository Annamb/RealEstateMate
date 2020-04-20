package is.RealEstate.realEstateMate.entities;


public class User {
    public long id;

    private static String userEmail;

    public static String userName;

    private static String userPassword;

    //@OneToMany(mappedBy = "users")

    public User() {
    }
    public User(int id, String userName, String userPassword, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.id = id;
    }
    public User(String userName, String userPassword, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }


    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
    public String getUserEmail() {
        return userEmail;
    }


    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return '{' +
                "ID=" + id +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }

}


