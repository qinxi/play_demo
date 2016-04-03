package controllers;

import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import models.User;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.StreamedResponse;
import play.libs.ws.WSClient;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    final Logger.ALogger logger = Logger.of(this.getClass());

    @Inject
    WSClient ws;

    @Inject
    FormFactory formFactory;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result submit() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String name =  form.get("name");
        String phone = form.get("phone");
        Map<String,String> map = new HashMap<>();
        map.put("answer","{}");
        map.put("name",name);
        map.put("phone",phone);
        logger.info("name:{}\t phone:{}",name,phone);
        map.put("format","script");
        map.put("callback","surveycallback");
        JsonNode json = Json.mapper().valueToTree(map);
        try {
            final String[] resp = {""};
            ws.url("http://appapns.www.gov.cn/govdata/survey.shtml").setMethod("POST").post(json).thenAccept(wsResponse -> {
                resp[0] = wsResponse.getBody();
            });
        }catch (Exception e){
            return ok(Json.toJson("出错啦!"));
        }
        return ok(Json.toJson("手机号:"+phone+"成功"));
    }
}
