import com.avaje.ebean.Ebean;
import models.Cargo;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.junit.*;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.F.Function;
import play.libs.WS;
import play.libs.Yaml;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;
import views.html.index;
import views.html.manage;

import javax.xml.ws.Response;

import java.util.List;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest extends WithApplication  {

    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

//    @Test
//    public void test() {
//        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
//            public void invoke(TestBrowser browser) {
//            browser.goTo("http://localhost:3333");
//            assertThat(browser.pageSource()).contains("Your new application is ready.");
//            }
//        });
//    }
//
    @Test
    public void testBitSync () {

        running(fakeApplication(), new Runnable() {
            public void run() {

                final String bitSyncApiUrl = Play.application().configuration().getString("usesync.bitsync.api.url");

                final Promise<WS.Response> p1 = WS.url(bitSyncApiUrl).setQueryParameter("method", "get_folders").get();
                WS.Response r1 = p1.get(10000l);
                assertThat(200).isEqualTo(r1.getStatus());

            }
        });
    }

    @Test
    public void sendTestEmail () {

        Cargo cargo = Cargo.findByIdAndManageUri(1L, "ABC");


        System.out.println(

//            manage.render(cargo)
        );

    }
}
