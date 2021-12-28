package org.techtown.navagation.other;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.QueryRequest;
import org.techtown.navagation.R;
import org.techtown.navagation.FoodTruckDatabase.RegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.SearchRequest;

public class Fragment2 extends Fragment {
    TextView id;
    TextView ps;
    Button button;
    Button search;
    TextView search_text;
    RegisterRequest registerRequest;
    Button jdbc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.fragment2,container,false);
        id = root.findViewById(R.id.id);
        ps = root.findViewById(R.id.ps);
        button = root.findViewById(R.id.show);
        search = root.findViewById(R.id.search);
        search_text = root.findViewById(R.id.search_text);
        //등록하기
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Toast.makeText(getContext(),"성공",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };
                String deviceId = getDeviceId(getContext());
                RegisterRequest registerRequest = new RegisterRequest(deviceId,"strawberry","카드",5,5,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(registerRequest);

            }
        });
        //검색하기
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for( int i =0;i<jsonArray.length();i++) {
                                    JSONObject JSONObject = jsonArray.getJSONObject(i);
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };
                SearchRequest queryRequest = new SearchRequest(search_text.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(queryRequest);
            }
        });
        Button query_button = root.findViewById(R.id.query);
        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoad();
            }
        });
        return root;
    }
    //조회하기
    public void clickLoad() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        System.out.println("이름"+ JSONObject.getString("userName"));
                        System.out.println("아이디 "+JSONObject.getString("userID"));
                    }
                    System.out.print("시스템 ID"+getDeviceId(getContext()));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        QueryRequest go = new QueryRequest(1,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);

    }
    public static String getDeviceId(Context context) {
        String deviceId = "";

        String android_id = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        deviceId = android_id;
        return deviceId;
    }
}




