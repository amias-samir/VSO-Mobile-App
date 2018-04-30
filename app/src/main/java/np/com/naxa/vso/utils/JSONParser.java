package np.com.naxa.vso.utils;

import com.google.gson.JsonObject;

public class JSONParser {

    private final JsonObject properties;

    public JSONParser(JsonObject properties) {
        this.properties = properties;
    }

    public String getName() {
        return properties.get("name").toString();
    }

    public String getAddress() {
        return properties.get("Address").toString();
    }


}
