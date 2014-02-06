package models;

import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
public class Cargo extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @OneToOne
    public User user;

    @Formats.DateTime(pattern="MM/dd/yy")
    public Date createdOn;

    @Formats.DateTime(pattern="MM/dd/yy")
    public Date killOn;

    public String directory;

    public String manageUri;
    public String readOnlyUri;
    public String readWriteUri;

    public String secretReadOnly;
    public String secretReadWrite;
    public String secretEncrypted;

    public static Model.Finder<String, Cargo> find = new Model.Finder<String,Cargo>(String.class, Cargo.class);

    /**
     * Retrieve cargo by id and manageUri
     */
    public static Cargo findById(Long id) {
        return find.where().eq("id", id).findUnique();
    }

    /**
     * Retrieve cargo by id and manageUri
     */
    public static Cargo findByIdAndManageUri(Long id, String uri) {
        return find.where().eq("id", id).eq("manageUri", uri).findUnique();
    }
}
