package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MenuDeleteRequest extends StringRequest {
    final static private String URL = "http://dlckdals56.dothome.co.kr/menuDelete.php";
    private Map<String, String> parameters;
    public MenuDeleteRequest(String foodName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("foodName", foodName);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
