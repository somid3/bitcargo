package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ApplicationTest extends WithApplication {

    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void createCargo() {

        Result result = callAction(
            controllers.routes.ref.Application.create(),
            fakeRequest().withFormUrlEncodedBody(
                    ImmutableMap.of("days", "2", "email", "bob@example.com")
            )
        );


    }
//
//    @Test
//    public void renameProject() {
//        long id = Project.find.where()
//                .eq("members.email", "bob@example.com")
//                .eq("name", "Private").findUnique().id;
//        Result result = callAction(
//                controllers.routes.ref.Projects.rename(id),
//                fakeRequest().withSession("email", "bob@example.com")
//                    .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
//        );
//        assertEquals(200, status(result));
//        assertEquals("New name", Project.find.byId(id).name);
//    }
//
//    @Test
//    public void renameProjectForbidden() {
//        long id = Project.find.where()
//                .eq("members.email", "bob@example.com")
//                .eq("name", "Private").findUnique().id;
//        Result result = callAction(
//                controllers.routes.ref.Projects.rename(id),
//                fakeRequest().withSession("email", "jeff@example.com")
//                        .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
//        );
//        assertEquals(403, status(result));
//        assertEquals("Private", Project.find.byId(id).name);
//    }

}