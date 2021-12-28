package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.AllQuery;
import org.techtown.navagation.FoodTruckDatabase.ByDayAllQuery;
import org.techtown.navagation.FoodTruckInfo;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapViewFragment extends Fragment  implements MapView.POIItemEventListener {
    MapView mapView;
    GoogleMap map;
    double nowlatitude;
    double nowlongitude;
    Calendar cal = Calendar.getInstance();
    MainActivity mainActivity;
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    SQLiteDatabase database;
    Button reload;
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
    String[] array;
    String[] array2;
    int timecheck=0;
    int checking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getData();
        View root = inflater.inflate(R.layout.mapviewfragment, container, false);
        RelativeLayout mapViewContainer = root.findViewById(R.id.mapView);
        MapView mapView = new MapView(getActivity());
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(nowlatitude,nowlongitude);
        mapView.setMapCenterPoint(mapPoint,true);
        mapView.setPOIItemEventListener(this);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("내 위치");
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mapView.addPOIItem(marker);
        mapViewContainer.addView(mapView);
        LocationManager manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        SimpleDateFormat format = new SimpleDateFormat("HH시MM분");
        String nowtime = format.format(cal.getTime());
        array = nowtime.split("시");
        array2 = array[1].split("분");
        ArrayList<MapCheck> check_list = new ArrayList<>();
        String cday = checkint(dayOfWeek);
        reload = root.findViewById(R.id.reload);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapView.removeAllPOIItems();
                marker.setItemName("내 위치");
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                mapView.addPOIItem(marker);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            double latitude_;
                            double longitude_;
                            System.out.println(jsonArray);
                            for( int i =0;i<jsonArray.length();i++){
                                timecheck=0;
                                Location mylocation = new Location(manager.NETWORK_PROVIDER);
                                Location foodtruck = new Location(manager.NETWORK_PROVIDER);
                                mylocation.setLatitude(nowlatitude);
                                mylocation.setLongitude(nowlongitude);
                                JSONObject JSONObject = jsonArray.getJSONObject(i);
                                String opentime = JSONObject.getString("open");
                                String[] array_open = opentime.split("시");
                                String[] array2_open = array[1].split("분");
                                String closetime = JSONObject.getString("close");
                                String[] array_close = closetime.split("시");
                                String[] array2_close = array[1].split("분");
                                String jdeviceId = JSONObject.getString("deviceID");
                                checkTime(array_open[0],array2_open[0],array_close[0],array2_close[0]);
                                MapPOIItem marker = new MapPOIItem();
                                latitude_ = Double.parseDouble(JSONObject.getString("latitude"));
                                longitude_ = Double.parseDouble(JSONObject.getString("longitude"));
                                foodtruck.setLatitude(latitude_);
                                foodtruck.setLongitude(longitude_);
                                float distance = mylocation.distanceTo(foodtruck);
                                if(distance>1000){
                                    continue;
                                }
                                if(timecheck!=1){ //현재 영업시간이 지났거나 영업날이아닐때
                                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude_,longitude_);
                                    marker.setItemName("준비중");
                                    marker.setUserObject(JSONObject.getString("deviceID"));
                                    marker.isShowCalloutBalloonOnTouch();
                                    marker.setMapPoint(mapPoint);
                                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomImageResourceId(R.drawable.sign);
                                    mapView.addPOIItem(marker);
                                    check_list.add(new MapCheck(JSONObject.getString("deviceID"),1));
                                }
                                else{
                                    MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude_,longitude_);
                                    marker.setItemName(JSONObject.getString("foodTruckName"));
                                    marker.setUserObject(JSONObject.getString("deviceID"));
                                    marker.isShowCalloutBalloonOnTouch();
                                    marker.setMapPoint(mapPoint);
                                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                    marker.setCustomImageResourceId(R.drawable.markericon);
                                    mapView.addPOIItem(marker);



                                }

                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                ByDayAllQuery go = new ByDayAllQuery(cday,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(go);
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    double latitude_;
                    double longitude_;
                    System.out.println(jsonArray);
                    for( int i =0;i<jsonArray.length();i++){
                        timecheck=0;
                        Location mylocation = new Location(manager.NETWORK_PROVIDER);
                        Location foodtruck = new Location(manager.NETWORK_PROVIDER);
                        mylocation.setLatitude(nowlatitude);
                        mylocation.setLongitude(nowlongitude);
                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        String opentime = JSONObject.getString("open");
                        String[] array_open = opentime.split("시");
                        String[] array2_open = array[1].split("분");
                        String closetime = JSONObject.getString("close");
                        String[] array_close = closetime.split("시");
                        String[] array2_close = array[1].split("분");
                        String jdeviceId = JSONObject.getString("deviceID");
                        checkTime(array_open[0],array2_open[0],array_close[0],array2_close[0]);
                        MapPOIItem marker = new MapPOIItem();
                        latitude_ = Double.parseDouble(JSONObject.getString("latitude"));
                        longitude_ = Double.parseDouble(JSONObject.getString("longitude"));
                        foodtruck.setLatitude(latitude_);
                        foodtruck.setLongitude(longitude_);
                        float distance = mylocation.distanceTo(foodtruck);
                        if(distance>1000){
                            continue;
                        }
                        if(timecheck!=1){ //현재 영업시간이 지났거나 영업날이아닐때
                            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude_,longitude_);
                            marker.setItemName("준비중");
                            marker.setUserObject(JSONObject.getString("deviceID"));
                            marker.isShowCalloutBalloonOnTouch();
                            marker.setMapPoint(mapPoint);
                            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                            marker.setCustomImageResourceId(R.drawable.sign);
                            mapView.addPOIItem(marker);
                            check_list.add(new MapCheck(JSONObject.getString("deviceID"),1));
                        }
                        else{
                                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude_,longitude_);
                                marker.setItemName(JSONObject.getString("foodTruckName"));
                                marker.setUserObject(JSONObject.getString("deviceID"));
                                marker.isShowCalloutBalloonOnTouch();
                                marker.setMapPoint(mapPoint);
                                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                                marker.setCustomImageResourceId(R.drawable.markericon);
                                mapView.addPOIItem(marker);



                        }

                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        ByDayAllQuery go = new ByDayAllQuery(cday,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);
        return root;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }
    public void refresh(){

    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Object deviceId =mapPOIItem.getUserObject();
        Intent intent = new Intent(getContext(), FoodTruckInfo.class);
        String latitude_ = String.valueOf(mainActivity.getLatitude());
        String longitude_ = String.valueOf(mainActivity.getLongitude());
        intent.putExtra("deviceId",String.valueOf(deviceId));
        intent.putExtra("latitude",latitude_);
        intent.putExtra("longitude",longitude_);
        getContext().startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
    public void getData(){
        String uriString = "content://org.techtown.navagation/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String[] columns=new String[]{"latitude","longitude"};
        Cursor cursor = getContext().getContentResolver().query(uri,columns,null,null,"_id DESC");

        try{
            cursor.moveToLast();
            nowlatitude = Double.parseDouble(cursor.getString(1));
            nowlongitude = Double.parseDouble(cursor.getString(2));
        }
        catch (Exception e){
            nowlatitude = 35.2455994;
            nowlongitude = 128.6897643;
            Toast.makeText(getContext(),"위치값이 설정이 안되어서 창원대로 설정되었습니다.",Toast.LENGTH_LONG).show();
        }

    }
    public int checkDay(String day){
        switch (day) {
            case "일":
                return 1;
            case "월":
                return 2;
            case "화":
                return 3;
            case "수":
                return 4;
            case "목":
                return 5;
            case "금":
                return 6;
            case "토":
                return 7;
        }
        return 0;
    }
    public String checkint(int day){
        switch (day) {
            case 1:
                return "일";
            case 2:
                return "월";
            case 3:
                return "화";
            case 4:
                return "수";
            case 5:
                return "목";
            case 6:
                return "금";
            case 7:
                return "토";
                
        }
        return "good";
    }
    public void checkTime(String otime,String ominute,String ctime,String cminute){
        String nowtime = array[0];
        String nowminute = array2[0];
        int inowtime = Integer.parseInt(nowtime);
        int inowminute = Integer.parseInt(nowminute);
        int iotime = Integer.parseInt(otime);
        int iominute = Integer.parseInt(ominute);
        int ictime = Integer.parseInt(ctime);
        int icminute = Integer.parseInt(cminute);
        if(inowtime>=iotime){

            if(ictime>=inowtime){
                timecheck=1;
            }
        }
    }
}

