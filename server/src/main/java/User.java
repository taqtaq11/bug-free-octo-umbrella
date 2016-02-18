import javax.persistence.*;
import javax.swing.text.StringContent;

/**
 * Created by Alexander on 18/02/16.
 */

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "login")
    private String login;

    @Column(name = "password_hash")
    private String password_hash;

    public User(String name, String surname, String login, String password_hash) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password_hash = password_hash;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    @Override
    public String toString() {
        return String.format("User [id: %d, name: %s, surname: %s, login: %s, pass_hash: %s]",
                id, name, surname, login, password_hash);
    }
}
