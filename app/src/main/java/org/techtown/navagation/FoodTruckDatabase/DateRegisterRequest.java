package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DateRegisterRequest extends StringRequest {
    final static private String URL = "http://dlckdals56.dothome.co.kr/DateRegister.php";
    private Map<String, String> parameters;
    public DateRegisterRequest(String deviceID, String open, String close,String day,String latitude,String longitude, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("deviceID", deviceID);
        parameters.put("open", open);
        parameters.put("close", close);
        parameters.put("day", day);
        parameters.put("latitude", latitude);
        parameters.put("longitude", longitude);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

