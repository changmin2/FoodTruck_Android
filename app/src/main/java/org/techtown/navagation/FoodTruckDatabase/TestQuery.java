package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TestQuery extends StringRequest {
    final static private String URL = "http://3.35.19.7/get.php";
    private Map<String, String> parameters;

    public TestQuery(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
