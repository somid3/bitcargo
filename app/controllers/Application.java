package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Cargo;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.joda.time.DateTime;
import play.*;
import play.data.Form;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

import views.forms.CargoUpdateEmailForm;
import views.html.*;

import java.io.File;
import java.util.Date;

import static play.data.Form.form;

public class Application extends Controller {

    /**
     * Checks to see whether domain lock is enabled and if it has been broken
     */
    public static boolean domainLockError() {

        boolean hostLocking = Play.application().configuration().getBoolean("usesync.host.locking");
        String hostLockedName = Play.application().configuration().getString("usesync.host.name");

        // TODO: change contains to equals, but take care of sub-domain issue (i.e.: www.)
        if (hostLocking & !request().host().contains(hostLockedName)) {
            Logger.debug("Request lock: " + request().host());
            return true;
        }

        return false;
    }

    public static Result index() {
        if (domainLockError()) return badRequest();

        return ok(index.render(main.render()));
    }

    public static class Create {

        public String email;
        public Integer days;

    }

    public static Result create() throws Exception {
        if (domainLockError()) return badRequest();

        final String bitSyncRepository = Play.application().configuration().getString("usesync.bitsync.repository");
        final String bitSyncApiUrl = Play.application().configuration().getString("usesync.bitsync.api.url");
        final Integer bitSyncDtl = Play.application().configuration().getInt("usesync.bitsync.dtl");

        // Creating Cargo
        Cargo cargo = new Cargo();
        cargo.manageUri = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        cargo.readOnlyUri = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        cargo.readWriteUri = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        cargo.createdOn = new Date();

        // Setting default kill date
        DateTime joda = new DateTime(cargo.createdOn);
        cargo.killOn =joda.plusDays(bitSyncDtl).toDate();

        // Creating directory
        cargo.directory = System.currentTimeMillis() + "-" + RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        File file = new File(bitSyncRepository + cargo.directory);
        file.mkdir();

        // Generating secrets
        F.Promise<WS.Response> p0 = WS.url(bitSyncApiUrl)
            .setQueryParameter("method", "get_secrets")
            .setQueryParameter("type", "encryption")
            .get();
        WS.Response r0 = p0.get(10000l);

        // Setting up cargo's secrets
        JsonNode secrets = r0.asJson();
        cargo.secretReadOnly = secrets.findValue("read_only").textValue();
        cargo.secretReadWrite = secrets.findValue("read_write").textValue();
        cargo.secretEncrypted = secrets.findValue("encryption").textValue();

        // Enabling BitSync
        F.Promise<WS.Response> p1 = WS.url(bitSyncApiUrl)
            .setQueryParameter("method", "add_folder")
            .setQueryParameter("secret", cargo.secretReadWrite)
            .setQueryParameter("dir", bitSyncRepository + cargo.directory)
            .get();
        WS.Response r1 = p1.get(10000l);

        // Saving UI success message
        flash("success", "Your Sync Space has been created!");

        // Persisting user and cargo
        cargo.save();

        return redirect(controllers.routes.Application.manage(cargo.id, cargo.manageUri));
    }

    public static Result manage(Long cargoId, String cargoManageUri) {
        if (domainLockError()) return badRequest();

        Cargo cargo = Cargo.findByIdAndManageUri(cargoId, cargoManageUri);

        String newEmail = null;
        if (cargo.user != null) {
            cargo.user.refresh();
            newEmail = cargo.user.email;
        }

        // Creating default cargo update email form
        Form<CargoUpdateEmailForm> cargoUpdateEmailForm = Form.form(CargoUpdateEmailForm.class);
        cargoUpdateEmailForm = cargoUpdateEmailForm.fill(new CargoUpdateEmailForm(cargoId, cargoManageUri, newEmail));

        return ok(index.render(manage.render(cargo, cargoUpdateEmailForm)));
    }


    public static Result manageUpdateEmail() throws Exception {
        if (domainLockError()) return badRequest();

        Form<CargoUpdateEmailForm> updateEmailForm = Form.form(CargoUpdateEmailForm.class).bindFromRequest();
        Cargo cargo = Cargo.findByIdAndManageUri(updateEmailForm.get().cargoId, updateEmailForm.get().cargoManageUri);

        if (cargo == null)
            return redirect(controllers.routes.Application.index());

        if (updateEmailForm.hasErrors())
            return badRequest(manage.render(cargo, updateEmailForm));

        // Do we need to create a new user?
        User newUser = User.findByEmail(updateEmailForm.get().newEmail);
        if (newUser == null) {

            // Creating user
            newUser = new User();
            newUser.email = updateEmailForm.get().newEmail;
            newUser.createdOn = new Date();
            newUser.save();
        }

        // Setting new user to cargo
        cargo.user = newUser;
        cargo.killOn = null;
        cargo.save();

        // Send notification email
        final String smtpHost = Play.application().configuration().getString("usesync.email.smtp.host");
        final String smtpUser = Play.application().configuration().getString("usesync.email.smtp.user");
        final String smtpPass = Play.application().configuration().getString("usesync.email.smtp.pass");
        final Integer smtpPort = Play.application().configuration().getInt("usesync.email.smtp.port");

        String manageUrl = "http://" + request().host() + controllers.routes.Application.manage(cargo.id, cargo.manageUri);

        Email email = new SimpleEmail();
        email.setHostName(smtpHost);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(new DefaultAuthenticator(smtpUser, smtpPass));
        email.setSSLOnConnect(true);
        email.setFrom("omid@usesync.com", "Omid at UseSync!");
        email.setSubject("Your new sync space!");
        email.setMsg(views.html.emails.manager.render(cargo, manageUrl).toString());
        email.addTo(newUser.email);
        email.send();

        // Saving UI success message
        flash("success", "Email updated! Check your inbox for instructions");

        return redirect(controllers.routes.Application.manage(cargo.id, cargo.manageUri));
    }

    public static Result shared(Long cargoId, String cargoManageUri) {
        if (domainLockError()) return badRequest();

        return ok();
    }


    public static Result contact() {
        if (domainLockError()) return badRequest();

        return ok(index.render(contact.render()));
    }

    public static Result terms() {
        if (domainLockError()) return badRequest();

        return ok(index.render(terms.render()));
    }

}
