package org.techtown.navagation.FoodTruck;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.BusinessIntoInform;
import org.techtown.navagation.FoodTruckDatabase.DateRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.MenuRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.RegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.TestQuery;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.util.Calendar;
import java.util.List;

// 푸드트럭의 이름과 결제수단을 등록하는 프래그먼트
public class InformRegisterFragment extends Fragment {
    MainActivity mainActivity;
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
    private static final int REQUEST_CODE_LOCATION = 2;
    TextView placecheck;
    Button register;
    TextView productname;
    TextView payment;
    String deviceId;
    TextView intomenu;
    TextView intoprice;
    TextView intoopen;
    TextView intoclose;
    TextView intoday;
    Button reopen;
    LocationManager locationManager;
    Button reclose;
    Button reday;
    String string_latitude;
    String string_longitude;
    Button renowplace;
    Button readdressplace;
    TextView readdress;
    final Calendar c = Calendar.getInstance();
    int mHour = c.get(Calendar.HOUR);
    int mMinute = c.get(Calendar.MINUTE);
    int choose;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.informregisterfragment,container,false);
        placecheck = root.findViewById(R.id.placecheck);
        placecheck.setText("0");
        register = root.findViewById(R.id.infromregister);
        productname = root.findViewById(R.id.productname);
        payment = root.findViewById(R.id.payment);
        intomenu  = root.findViewById(R.id.intomenu);
        intoprice = root.findViewById(R.id.intoprice);
        intoopen = root.findViewById(R.id.intoopen);
        intoclose = root.findViewById(R.id.intoclose);
        intoday = root.findViewById(R.id.intoday);
        reopen = root.findViewById(R.id.reopen);
        reclose = root.findViewById(R.id.reclose);
        reday = root.findViewById(R.id.reday);
        readdress = root.findViewById(R.id.readdress);
        renowplace = root.findViewById(R.id.renowplace);
        readdressplace = root.findViewById(R.id.readdressplace);
        renowplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Location userLocation = getMyLocation();
                if( userLocation != null ) {
                    string_latitude = String.valueOf(userLocation.getLatitude());
                    string_longitude = String.valueOf(userLocation.getLongitude());
                    placecheck.setText("1");
                    Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getContext(),"위치정보를 받아올 수 없습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
        readdressplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(readdress.getText().toString())){
                    Toast.makeText(getContext(),"지역을 입력해주세요",Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        List<Double> list = mainActivity.getGeocode(readdress.getText().toString());
                        mainActivity.setLatitude(list.get(0));
                        mainActivity.setLongitude(list.get(1));
                        string_latitude = String.valueOf(list.get(0));
                        string_longitude = String.valueOf(list.get(1));
                        placecheck.setText("1");
                        Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();

                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"없는 지역입니다.",Toast.LENGTH_LONG).show();
                    }


                }

            }
        });
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        deviceId= mainActivity.getDeviceId(getContext());
        reopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                intoopen.setText(hourOfDay+"시"+minute+"분");
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });
        reclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                intoclose.setText(hourOfDay+"시"+minute+"분");
                            }
                        },mHour,mMinute,false);
                timePickerDialog.show();
            }
        });

        reday.setOnClickListener(new View.OnClickListener() {
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
                        intoday.setText(versionArray[choose]);
                    }
                });
                dlg.show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int integercheck=0;
                try{
                    Integer.parseInt(intoprice.getText().toString());
                }
                catch (Exception e){
                    integercheck=1;
                    Toast.makeText(getContext(),"가격에는 숫자만 입력해주세요",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(intoday.getText().toString())||TextUtils.isEmpty(intoclose.getText().toString())||TextUtils.isEmpty(intoopen.getText().toString())||TextUtils.isEmpty(intoprice.getText().toString())||TextUtils.isEmpty(intomenu.getText().toString())||TextUtils.isEmpty(productname.getText().toString())||TextUtils.isEmpty(payment.getText().toString())){
                    Toast.makeText(getContext(),"내용을 다 채워주세요",Toast.LENGTH_LONG).show();
                }
                else if(integercheck==0){
                    if(placecheck.getText().toString()=="0"){
                        Toast.makeText(getContext(),"위치설정을 해주세요",Toast.LENGTH_LONG).show();
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
                        RegisterRequest RegisterRequest = new RegisterRequest(deviceId,productname.getText().toString(),payment.getText().toString(),0,0,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        queue.add(RegisterRequest);
                        Toast.makeText(getContext(),"등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        intomenu(intomenu.getText().toString(),intoprice.getText().toString());
                        intoplan(intoopen.getText().toString(),intoclose.getText().toString(),intoday.getText().toString());
                        Intent intent = new Intent(v.getContext(), BusinessIntoInform.class);
                        intent.putExtra("deviceId",deviceId);
                        v.getContext().startActivity(intent);
                    }


                }

            }
        });

        return root;
    }
    public void intomenu(String menu,String price){
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
        MenuRegisterRequest menuRegisterRequest = new MenuRegisterRequest(deviceId,menu,price,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(menuRegisterRequest);
    }
    public void intoplan(String open,String close,String day){
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
        DateRegisterRequest dateRegisterRequest = new DateRegisterRequest(deviceId,open,close,day,string_latitude,string_longitude,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(dateRegisterRequest);
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
