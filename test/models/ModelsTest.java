package models;

import com.avaje.ebean.Ebean;
import models.*;
import org.junit.*;
import static org.junit.Assert.*;

import play.Logger;
import play.libs.Yaml;
import play.test.WithApplication;

import java.util.List;

import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {

    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void retrieveUser() {
        User out = User.findByEmail("bob@example.com");
        assertNotNull(out);
    }

    @Test
    public void retrieveCargoById() {
        Cargo out = Cargo.findById(1L);
        assertNotNull(out);
    }

    @Test
    public void retrieveCargoByIdAndManageUri() {
        Cargo out = Cargo.findByIdAndManageUri(1L, "ABC");
        assertNotNull(out);
    }
}
