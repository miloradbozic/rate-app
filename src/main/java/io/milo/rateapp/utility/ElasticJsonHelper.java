package io.milo.rateapp.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ElasticJsonHelper {

    public String getScriptJson(String inline, JsonObject params) {
        Gson gson = new Gson();
        JsonObject paramsWrapper = new JsonObject();
        JsonObject scriptWrapper = new JsonObject();
        JsonObject script = new JsonObject();

        paramsWrapper.add("vote", params);

        scriptWrapper.addProperty("inline", inline);
        scriptWrapper.addProperty("lang", "painless");
        scriptWrapper.add("params",  paramsWrapper);

        script.add("script", scriptWrapper);

        return gson.toJson(script);
    }
}
