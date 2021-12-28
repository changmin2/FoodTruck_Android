package org.techtown.navagation.FoodTruckDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlaceRequest extends StringRequest {
    final static private String URL="http://dlckdals56.dothome.co.kr/placequery.php";
    private Map<String,String> map;

    public PlaceRequest(Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
    }
    @Override
    protected Map<String,String> getParams() throws AuthFailureError {
        return map;
    }
}
