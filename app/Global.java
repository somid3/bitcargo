import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.libs.Yaml;
import play.mvc.Result;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import static play.mvc.Results.*;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {
            if(Ebean.find(User.class).findRowCount() == 0) {

                @SuppressWarnings("unchecked")
                Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");

                Ebean.save(all.get("users"));

                Ebean.save(all.get("cargos"));

            }
        }

    }





}