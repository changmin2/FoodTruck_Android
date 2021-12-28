package org.techtown.navagation.FoodTruck;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.DateRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.GetPlanRequest;
import org.techtown.navagation.FoodTruckDatabase.RevisePlanRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.util.Calendar;
import java.util.List;

public class PlanReviseFragment extends Fragment {
    PlanAdapter adapter;
    MainActivity mainActivity;
    private static final int REQUEST_CODE_LOCATION = 2;
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
    TextView open;
    TextView close;
    TextView day;
    Button register;
    LocationManager locationManager;
    String string_latitude="";
    String string_longitude="";
    Button openmanage;
    Button closemanage;
    Button daymanage;
    String deviceId;
    Button revisenowplace;
    Button reviseaddressplace;
    TextView reviseaddress;
    TextView reviseplacecheck;
    int choose;
    int checking_=0;
    private TimePickerDialog.OnTimeSetListener callbackMethod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root =  inflater.inflate(R.layout.planfragment,container,false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.planrecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        open = root.findViewById(R.id.reviseopen);
        close = root.findViewById(R.id.reviseclose);
        day = root.findViewById(R.id.reviseday);
        daymanage = root.findViewById(R.id.daymanage);
        register = root.findViewById(R.id.planreviseregister);
        openmanage = root.findViewById(R.id.openmanage);
        closemanage = root.findViewById(R.id.closemanage);
        revisenowplace = root.findViewById(R.id.revisenowplace);
        reviseaddressplace = root.findViewById(R.id.reviseaddressbutton);
        reviseaddress = root.findViewById(R.id.reviseaddress);
        reviseplacecheck = root.findViewById(R.id.replacecheck);
        reviseplacecheck.setText("0");
        deviceId = mainActivity.getDeviceId(getContext());
        adapter = new PlanAdapter();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        revisenowplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Location userLocation = getMyLocation();
                if( userLocation != null ) {
                    string_latitude = String.valueOf(userLocation.getLatitude());
                    string_longitude = String.valueOf(userLocation.getLongitude());
                    reviseplacecheck.setText("1");
                    Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getContext(),"위치정보를 받아올 수 없습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
        reviseaddressplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(reviseaddress.getText().toString())){
                    Toast.makeText(getContext(),"지역을 입력해주세요",Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        List<Double> list = mainActivity.getGeocode(reviseaddress.getText().toString());
                        mainActivity.setLatitude(list.get(0));
                        mainActivity.setLongitude(list.get(1));
                        string_latitude = String.valueOf(list.get(0));
                        string_longitude = String.valueOf(list.get(1));
                        reviseplacecheck.setText("1");
                        Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();

                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"없는 지역입니다.",Toast.LENGTH_LONG).show();
                    }


                }

            }
        });
        int count = adapter.getItemCount();
        if(count>=7){
            register.setVisibility(View.INVISIBLE);
        }
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        openmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                open.setText(hourOfDay+"시"+minute+"분");
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });
        closemanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                close.setText(hourOfDay+"시"+minute+"분");
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        daymanage.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                                             dlg.setTitle("오픈일 선택"); //제목
                                             final String[] versionArray = new String[]{"월", "화", "수", "목", "금", "토", "일"};
                                             final boolean[] checkArray = new boolean[]{false, false, false, false, false, false, false};

                                             dlg.setMultiChoiceItems(versionArray, checkArray, new DialogInterface.OnMultiChoiceClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                                     choose = which;

                                                 }
                                             });
//                버튼 클릭시 동작
                                             dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     day.setText(versionArray[choose]);
                                                 }
                                             });
                                             dlg.show();
                                         }
                                     });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checking_==1){
                    Toast.makeText(getContext(),"요일이 중복됩니다.",Toast.LENGTH_LONG).show();
                }
                if(open.getText().toString()==""||close.getText().toString()==""||day.getText().toString()==""){
                    Toast.makeText(getContext(),"빠진 부분이 있습니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    if(reviseplacecheck.getText().toString()=="0"){
                        Toast.makeText(getContext(),"지역을 입력해주세요",Toast.LENGTH_LONG).show();
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
                        DateRegisterRequest dateRegisterRequest = new DateRegisterRequest(deviceId,open.getText().toString(),close.getText().toString(),day.getText().toString(),string_latitude,string_longitude,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(dateRegisterRequest);
                        adapter.addItem(new Plan(open.getText().toString(),close.getText().toString(),day.getText().toString()));
                        adapter.notifyDataSetChanged();
                    }

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
                        adapter.addItem(new Plan(JSONObject.getString("open"),JSONObject.getString("close"),JSONObject.getString("day")));
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        RevisePlanRequest go = new RevisePlanRequest(deviceId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);
        recyclerView.setAdapter(adapter);
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

}
