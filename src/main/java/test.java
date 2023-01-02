
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.treeshop.entity.lineitem.LineItemEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {
        // URL
        String URL = "https://v6.exchangerate-api.com/v6/cdd8f9308843227942ec4fd7/latest/USD";

        // Making request to get exchangerate
        URL url = new URL(URL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Using json to read result
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();
        // Get conversion_rates
        Float oneUSDtoVNDValue = jsonObject.getAsJsonObject("conversion_rates").get("VND").getAsFloat();

        System.out.println(oneUSDtoVNDValue);


    }
}
