package aisr.model;

/**
 *
 * @author gauravdahal
 */
public class SessionUser {

    private Recruit recruit;
    private String email;

    public Recruit getRecruit() {
        return recruit;
    }

    public void setRecruit(Recruit recruit) {
        this.recruit = recruit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SessionUser(String email) {
        this.email = email;
    }

    public SessionUser(String email, Recruit recruit) {
        this.email = email;
        this.recruit = recruit;
    }

}
