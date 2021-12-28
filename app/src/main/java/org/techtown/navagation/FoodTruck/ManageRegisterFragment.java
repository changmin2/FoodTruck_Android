package org.techtown.navagation.FoodTruck;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.DateRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.MenuRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.PlaceRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;
import org.techtown.navagation.other.Fragment3;

import java.util.concurrent.Executor;

// 오픈시간 마감시간 등을 등록하는 프래그먼트
public class ManageRegisterFragment extends Fragment {

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

    Button planbutton;
    TextView open;
    TextView close;
    TextView day;
    MainActivity mainActivity;
    String deviceId;
    String string_latitude = "";
    String string_longitude = "";
    double final_latitude=0;
    private LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dataregisterfragment, container, false);
        planbutton = root.findViewById(R.id.planbutton);
        open = root.findViewById(R.id.open);
        close = root.findViewById(R.id.close);
        day = root.findViewById(R.id.day);
        deviceId = mainActivity.getDeviceId(getContext());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location userLocation = getMyLocation();
        if( userLocation != null ) {
            double latitude = userLocation.getLatitude();
            string_latitude = String.valueOf(latitude);
            double longitude = userLocation.getLongitude();
            string_longitude = String.valueOf(longitude);
        }


        planbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                DateRegisterRequest dateRegisterRequest = new DateRegisterRequest(deviceId,open.getText().toString(),close.getText().toString(),day.getText().toString(),string_latitude,string_longitude,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(dateRegisterRequest);
                Toast.makeText(getContext(),"등록이 완료되었습니다.",Toast.LENGTH_LONG).show();

                mainActivity.fragmentChange(4);
            }
        });
        Button placeget = root.findViewById(R.id.requestplace);
        placeget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoad();
            }
        });

        return root;
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        }
        else {
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
            }
        }
        return currentLocation;

    }

    public void clickLoad(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);

                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        PlaceRequest go = new PlaceRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);

    }





}
