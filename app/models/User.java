package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Formats.NonEmpty
    public String email;

    @Formats.DateTime(pattern="MM/dd/yy")
    public Date createdOn;

    public String getEmail() {
        return email;
    }

    // -- Queries

    public static Model.Finder<String,User> find = new Model.Finder<String,User>(String.class, User.class);

    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
}
