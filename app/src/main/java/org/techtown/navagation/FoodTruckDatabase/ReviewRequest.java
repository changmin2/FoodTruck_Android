package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewRequest extends StringRequest {
    final static private String URL="http://dlckdals56.dothome.co.kr/review.php";
    private Map<String,String> map;

    public ReviewRequest(String deviceId, String grade,Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("deviceId",deviceId);
        map.put("grade",grade);
    }
    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return map;
    }
}
