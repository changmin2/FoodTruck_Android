package org.techtown.navagation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.techtown.navagation.FoodTruck.Menu;
import org.techtown.navagation.FoodTruck.MenuReviewAdapter;
import org.techtown.navagation.FoodTruck.Plan;
import org.techtown.navagation.FoodTruck.PlanAdapter;
import org.techtown.navagation.FoodTruck.PlanReviewAdapter;
import org.techtown.navagation.FoodTruckDatabase.MenuQueryRequest;
import org.techtown.navagation.FoodTruckDatabase.QueryByDeviceId;
import org.techtown.navagation.FoodTruckDatabase.ReviewRequest;
import org.techtown.navagation.FoodTruckDatabase.RevisePlanRequest;

public class FoodTruckInfo extends AppCompatActivity {
    String FoodtruckName;
    String payment;
    String grade;
    String numOfPeople;
    PlanAdapter plan_adapter;
    Button review;
    Button returnButton;
    MenuReviewAdapter menuReviewAdapter;
    PlanReviewAdapter planReviewAdapter;
    int choose=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_inform);
        TextView intoname = findViewById(R.id.intoname);
        TextView intopayment = findViewById(R.id.intopayment);
        TextView intograde = findViewById(R.id.intograde);
        review = findViewById(R.id.foodregister);
        Intent intent = getIntent();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView2.setLayoutManager(layoutManager2);
        String deviceId =intent.getStringExtra("deviceId");

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("리뷰 남기기"); //제목

                final String[] versionArray = new String[] {"1점","2점","3점","4점","5점"};
                final boolean[] checkArray = new boolean[]{false,false,false,false,false};


                dlg.setSingleChoiceItems(versionArray, -1, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose = which+1;
                    }

                });
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        if(choose==0){
                            Toast.makeText(dlg.getContext(),"리뷰점수를 선택하지 않았습니다.",Toast.LENGTH_LONG).show();
                        }
                        else{
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

                            ReviewRequest reviewRequest = new ReviewRequest(deviceId,String.valueOf(choose),responseListener);
                            RequestQueue reviewqueue = Volley.newRequestQueue(v.getContext());
                            reviewqueue.add(reviewRequest);
                            Intent intent = new Intent(v.getContext(), FoodTruckInfo.class);
                            intent.putExtra("deviceId",deviceId);
                            v.getContext().startActivity(intent);
                        }

                    }
                });
                dlg.show();

            }
        });
        returnButton = findViewById(R.id.foodrevise);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("name",3);
                v.getContext().startActivity(intent);
            }
        });
        plan_adapter = new PlanAdapter();
        menuReviewAdapter = new MenuReviewAdapter();
        planReviewAdapter = new PlanReviewAdapter();
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


}