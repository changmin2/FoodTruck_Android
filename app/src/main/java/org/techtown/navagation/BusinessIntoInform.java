package org.techtown.navagation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruck.MainFragment;
import org.techtown.navagation.FoodTruck.Menu;
import org.techtown.navagation.FoodTruck.MenuReviewAdapter;
import org.techtown.navagation.FoodTruck.Plan;
import org.techtown.navagation.FoodTruck.PlanAdapter;
import org.techtown.navagation.FoodTruck.PlanReviewAdapter;
import org.techtown.navagation.FoodTruckDatabase.AllDeleteRequest;
import org.techtown.navagation.FoodTruckDatabase.AllQuery;
import org.techtown.navagation.FoodTruckDatabase.AscQuery;
import org.techtown.navagation.FoodTruckDatabase.MenuQueryRequest;
import org.techtown.navagation.FoodTruckDatabase.QueryByDeviceId;
import org.techtown.navagation.FoodTruckDatabase.ReviewRequest;
import org.techtown.navagation.FoodTruckDatabase.RevisePlanRequest;

public class BusinessIntoInform extends AppCompatActivity {
    String FoodtruckName="";
    String payment;
    String grade;
    String numOfPeople;
    PlanAdapter plan_adapter;
    Button foodregister;
    Button foodrevise;
    Button backreturn;
    Button foodtruckdelete;
    MenuReviewAdapter menuReviewAdapter;
    PlanReviewAdapter planReviewAdapter;
    MainFragment mainFragment;
    int choose;
    int checking=0;
    int check = 0;
    String deviceId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_into_inform);
        TextView intoname = findViewById(R.id.intoname);
        TextView intopayment = findViewById(R.id.intopayment);
        TextView intograde = findViewById(R.id.intograde);
        foodregister = findViewById(R.id.foodregister);
        foodrevise = findViewById(R.id.foodrevise);
        backreturn = findViewById(R.id.backreturn);

        foodtruckdelete = findViewById(R.id.foodtruckdelete);

        mainFragment = new MainFragment();
        Intent intent = getIntent();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(layoutManager2);
        deviceId = getDeviceId();
        check(deviceId);
        plan_adapter = new PlanAdapter();
        menuReviewAdapter = new MenuReviewAdapter();
        planReviewAdapter = new PlanReviewAdapter();

        foodtruckdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==0){
                    Toast.makeText(v.getContext(),"등록된 푸드트럭이 없습니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    Response.Listener<String> responseListener5 = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("result");

                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };

                    AllDeleteRequest go5= new AllDeleteRequest(deviceId,responseListener5);
                    RequestQueue queue = Volley.newRequestQueue(v.getContext());
                    queue.add(go5);
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("name",6);
                    v.getContext().startActivity(intent);
                }

            }
        });

        backreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("name",6);
                v.getContext().startActivity(intent);
            }
        });
        foodregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check ==1 ){
                    Toast.makeText(v.getContext(),"푸드트럭 등록이 이미 되어있습니다!!.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("name",4);
                    v.getContext().startActivity(intent);
                }

            }
        });
        foodrevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check ==0 ){
                    Toast.makeText(v.getContext(),"푸드트럭 등록을 먼저 해주세요!!.",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("name",5);
                    v.getContext().startActivity(intent);
                }
            }
        });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        FoodtruckName = JSONObject.getString("foodTruckName");
                        payment = JSONObject.getString("payment");
                        grade = JSONObject.getString("grade");
                        numOfPeople = JSONObject.getString("numOfPeople");
                        String final_grade="";
                        if(Double.parseDouble((grade))==0&&Double.parseDouble(numOfPeople)==0){
                            final_grade = "0";
                        }
                        else{
                            final_grade = String.format("%.1f",Double.parseDouble(grade) / Double.parseDouble(numOfPeople));
                        }

                        intoname.setText(FoodtruckName);
                        intopayment.setText(payment);
                        intograde.setText(final_grade+"점 총 "+numOfPeople+"명 참여");

                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        QueryByDeviceId go = new QueryByDeviceId(deviceId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(go);
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);

                        menuReviewAdapter.addItem(new Menu(JSONObject.getString("name"),JSONObject.getString("price")));
                        menuReviewAdapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        MenuQueryRequest go2 = new MenuQueryRequest(deviceId,responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(this);
        queue2.add(go2);
        recyclerView.setAdapter(menuReviewAdapter);

        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        planReviewAdapter.addItem(new Plan(JSONObject.getString("open"),JSONObject.getString("close"),JSONObject.getString("day")));
                        planReviewAdapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        RevisePlanRequest go3 = new RevisePlanRequest(deviceId,responseListener3);
        RequestQueue queue3 = Volley.newRequestQueue(this);
        queue3.add(go3);
        recyclerView2.setAdapter(planReviewAdapter);
    }
    public  String getDeviceId() {
        String deviceId = "";

        String android_id = Settings.Secure.getString(
                this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        deviceId = android_id;
        return deviceId;
    }
    public void check(String deviceId) {

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Response.Listener<String> responseListener6 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject JSONObject = jsonArray.getJSONObject(i);
                        String checkDeviceId = JSONObject.getString("deviceID");
                        if(deviceId.equals(checkDeviceId)){
                            System.out.println("checkehcek");
                            check=1;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AscQuery go6 = new AscQuery(responseListener6);
        RequestQueue queue6 = Volley.newRequestQueue(this);
        queue6.add(go6);

    }
}