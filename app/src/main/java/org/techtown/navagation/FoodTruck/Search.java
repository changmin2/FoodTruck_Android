package org.techtown.navagation.FoodTruck;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.Data;
import org.techtown.navagation.FoodTruckDatabase.AllQuery;
import org.techtown.navagation.FoodTruckDatabase.AscQuery;
import org.techtown.navagation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Search extends Fragment {
    TextView searchFoodTruckName;
    Button GoSearch;
    FoodTruckAdapter foodTruckAdapter;
    Data data;
    Calendar cal = Calendar.getInstance();
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    int timecheck=0;
    String[] array;
    String[] array2;
    String deviceId;
    ArrayList<MapCheck> check_list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.searchnamefragment,container,false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.truckrecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        searchFoodTruckName = root.findViewById(R.id.searchFoodTruckName);
        GoSearch = root.findViewById(R.id.gosearch);
        foodTruckAdapter = new FoodTruckAdapter();
        SimpleDateFormat format = new SimpleDateFormat("HH시MM분");
        String nowtime = format.format(cal.getTime());
        array = nowtime.split("시");
        array2 = array[1].split("분");
        ArrayList<FoodTruck> arr = new ArrayList<>();
        GoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodTruckAdapter.clear();
                arr.clear();
                if(TextUtils.isEmpty(searchFoodTruckName.getText())){
                    Toast.makeText(getContext(),"검색내용이 없어 모든 푸드트럭을 보여줍니다.",Toast.LENGTH_LONG).show();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");

                            System.out.println(jsonArray);
                            for( int i =0;i<jsonArray.length();i++){
                                timecheck=0;
                                JSONObject JSONObject = jsonArray.getJSONObject(i);
                                int check_day = checkDay(JSONObject.getString("dayOfWeek"));
                                String opentime = JSONObject.getString("open");
                                String[] array_open = opentime.split("시");
                                String[] array2_open = array[1].split("분");
                                String closetime = JSONObject.getString("close");
                                String[] array_close = closetime.split("시");
                                String[] array2_close = array[1].split("분");
                                checkTime(array_open[0],array2_open[0],array_close[0],array2_close[0]);
                                String name = JSONObject.getString("foodTruckName");
                                deviceId = JSONObject.getString("deviceID");
                                if(dayOfWeek !=check_day || timecheck!=1){
                                    if(name.contains(searchFoodTruckName.getText().toString())){
                                        arr.add(new FoodTruck(name,"영업종료",deviceId));
                                    }
                                }
                                else{
                                    if(name.contains(searchFoodTruckName.getText().toString())){
                                        check_list.add(new MapCheck(deviceId,1));
                                        foodTruckAdapter.addItem(new FoodTruck(name,"영업중",deviceId));
                                        foodTruckAdapter.notifyDataSetChanged();
                                    }
                                }

                            }
                            for(int i=0;i<arr.size();i++){
                                int checking = 0;
                                for(int j=0;j<check_list.size();j++){
                                    if(check_list.get(j).getDeviceId().equals(arr.get(i).getDeviceId())){
                                        checking=1;
                                    }
                                }
                                if(checking==0){
                                    foodTruckAdapter.addItem(arr.get(i));
                                    foodTruckAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                AllQuery go = new AllQuery(responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(go);

            }
        });

        recyclerView.setAdapter(foodTruckAdapter);
        return root;
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
                System.out.println("inowtime"+inowtime+"ictime"+ictime);
                timecheck=1;
            }
        }
    }

}
