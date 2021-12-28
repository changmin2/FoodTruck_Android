package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.AllQuery;
import org.techtown.navagation.FoodTruckDatabase.TestQuery;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

public class BusinessFragment extends Fragment {
    MainActivity mainActivity;
    public int check=0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    // 메인 액티비티에서 내려온다.
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
    Button register;
    Button revise;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.businessfragment,container,false);
        String deviceId = mainActivity.getDeviceId(getContext());
        register = root.findViewById(R.id.foodtruckregister);
        revise = root.findViewById(R.id.foodtruckrevise);
        check(deviceId);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.fragmentChange(4);
            }
        });
        revise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(check==1){
                    mainActivity.fragmentChange(9);
                }
                else{
                    Toast.makeText(getContext(),"먼저 푸드트럭 등록을 먼저 해주세요",Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    public void check(String deviceId) {

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        String checkDeviceId = JSONObject.getString("deviceID");
                        if(deviceId.equals(checkDeviceId)){
                            check=1;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AllQuery go = new AllQuery(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);

    }
}
