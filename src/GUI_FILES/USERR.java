package GUI_FILES;

import java.io.Serializable;

public class USERR implements Serializable {
    private String firstname;
    private String lastname;
    private String email;
    private String password; // Store the password as a number
    private GENDER gender;
    private String username;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GENDER getGender() {
        return gender;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public USERR(String firstname, String lastname, String email, String password, GENDER gender, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        // Convert the password to a number (you can use a more sophisticated conversion algorithm if needed)
        this.password = password;
        this.gender = gender;
        this.username = username;
    }

    // Getters and setters...

    @Override
    public String toString() {
        return "USERR{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password=" + password +
                ", gender=" + gender +
                ", username='" + username + '\'' +
                '}';
    }
}