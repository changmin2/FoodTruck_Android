package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MenuRegisterRequest extends StringRequest {
    final static private String URL = "http://dlckdals56.dothome.co.kr/MenuRegister.php";
    private Map<String, String> parameters;
    public MenuRegisterRequest(String deviceID,String menu,String price, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("deviceID", deviceID);
        parameters.put("foodname", menu);
        parameters.put("price", price);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

