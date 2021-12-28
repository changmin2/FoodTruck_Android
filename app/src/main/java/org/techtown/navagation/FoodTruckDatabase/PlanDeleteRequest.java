package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlanDeleteRequest extends StringRequest {
    final static private String URL = "http://dlckdals56.dothome.co.kr/PlanDelete.php";
    private Map<String, String> parameters;
    public PlanDeleteRequest(String dayOfWeek, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("dayOfWeek", dayOfWeek);


    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
