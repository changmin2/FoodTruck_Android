package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://dlckdals56.dothome.co.kr/FoodTruckRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String deviceID,String foodTruckName,String payment,int grade,int numOfPeople, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("deviceID", deviceID);
        parameters.put("foodTruckName", foodTruckName);
        parameters.put("payment", payment);
        parameters.put("grade", String.valueOf(grade));
        parameters.put("numOfPeople", String.valueOf(numOfPeople));
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}