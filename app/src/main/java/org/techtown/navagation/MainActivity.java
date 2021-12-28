package org.techtown.navagation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruck.BusinessFragment;
import org.techtown.navagation.FoodTruck.Owner;
import org.techtown.navagation.FoodTruck.Search;
import org.techtown.navagation.FoodTruck.InformRegisterFragment;
import org.techtown.navagation.FoodTruck.MainFragment;
import org.techtown.navagation.FoodTruck.ManageRegisterFragment;
import org.techtown.navagation.FoodTruck.MapViewFragment;
import org.techtown.navagation.FoodTruck.MenuRegisterFragment;
import org.techtown.navagation.FoodTruck.MenuReviseFragment;
import org.techtown.navagation.FoodTruck.PlaceMapViewFragment;
import org.techtown.navagation.FoodTruck.LocationSetting;
import org.techtown.navagation.FoodTruck.PlanReviseFragment;
import org.techtown.navagation.FoodTruck.RegisterFragment;
import org.techtown.navagation.FoodTruck.ReviseChooseFragment;
import org.techtown.navagation.FoodTruckDatabase.TestQuery;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    SQLiteDatabase database;
    DrawerLayout drawer;
    MainFragment mainFragment;
    Owner customerFragment;
    BusinessFragment businessFragment;
    MapViewFragment mapViewFragment;
    RegisterFragment registerFragment;
    InformRegisterFragment informRegisterFragment;
    MenuRegisterFragment menuRegisterFragment;
    ManageRegisterFragment manageRegisterFragment;
    MenuReviseFragment menuReviseFragment;
    ReviseChooseFragment reviseChooseFragment;
    PlanReviseFragment planReviseFragment;
    PlaceMapViewFragment placeMapViewFragment;
    LocationSetting placeSetFragment;
    Search foodTruckSearchFragment;
    Toolbar toolbar;
    public double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public  double longitude;

    final Geocoder geocoder = new Geocoder(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();
        mainFragment = new MainFragment();
        customerFragment = new Owner();
        businessFragment = new BusinessFragment();
        mapViewFragment = new MapViewFragment();
        registerFragment  = new RegisterFragment();
        informRegisterFragment = new InformRegisterFragment();
        menuRegisterFragment  = new MenuRegisterFragment();
        manageRegisterFragment = new ManageRegisterFragment();
        menuReviseFragment = new MenuReviseFragment();
        reviseChooseFragment = new ReviseChooseFragment();
        planReviseFragment = new PlanReviseFragment();
        placeMapViewFragment = new PlaceMapViewFragment();
        placeSetFragment = new LocationSetting();
        foodTruckSearchFragment = new Search();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        Intent intent = getIntent();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit(); //처음화면


        if(intent.getIntExtra("name",0)==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,menuReviseFragment).commit();
        }
        else if(intent.getIntExtra("name",0)==2){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,planReviseFragment).commit();
        }
        else if(intent.getIntExtra("name",0)==3){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,mapViewFragment).commit();
        }
        else if(intent.getIntExtra("name",0)==4){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,informRegisterFragment).commit();
        }
        else if(intent.getIntExtra("name",0)==5){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,reviseChooseFragment).commit();
        }
        else if(intent.getIntExtra("name",0)==6){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
        }

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        checkPermissions(permissions);


    }
    public List<Double> getGeocode(String place){
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(place, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = list.get(0);
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        List<Double> lat = new ArrayList<>();
        lat.add(latitude);
        lat.add(longitude);
        return lat;
    }
    public void checkPermissions(String[] permissions) {
        ArrayList<String> targetList = new ArrayList<String>();

        for (int i = 0; i < permissions.length; i++) {
            String curPermission = permissions[i];
            int permissionCheck = ContextCompat.checkSelfPermission(this, curPermission);
            if (permissionCheck ==0){
                return ;
            }
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, curPermission + " 권한 있음.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, curPermission + " 권한 없음.", Toast.LENGTH_LONG).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, curPermission)) {
                    Toast.makeText(this, curPermission + " 권한 설명 필요함.", Toast.LENGTH_LONG).show();
                } else {
                    targetList.add(curPermission);
                }
            }
        }

        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        ActivityCompat.requestPermissions(this, targets, 101);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int curId = item.getItemId();
        if(curId == R.id.check){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
        }
        else if(curId == R.id.explain){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("앱의 설명서")
                    .setMessage("고객버튼  \n" +
                            "1. 위치설정 : 고객의 현재 위치로 설정하거나 주소를 검색하여 설정가능합니다.\n" +
                            "2. 위치기반 검색 : 설정한 위치기준으로 1km반경 이내에 있는 푸드트럭을 검색해줍니다. 단, 위치를 설정안할시 창원대학교로 설정됩니다.\n" +
                            "3. 푸드트럭명 검색 : 푸드트럭의 이름으로 검색할 수 있습니다.\n" +
                            "푸드트럭 사업자 버튼\n" +
                            "- 사업자가 등록되어있을 경우 정보가 리스트로 나열되며 등록되지 않으면 정보가 뜨지 않습니다.\n" +
                            "- 푸드트럭 등록 : 푸드트럭 이름과 결제수단, 메뉴, 운영정보를 등록합니다.\n" +
                            "- 푸드트럭수정 : 메뉴수정은  메뉴를 추가하고 삭제합니다. 운영정보 수정은 운영시간을 등록합니다.\n");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {

                }
            });
            AlertDialog alertDialog = builder.create();

            alertDialog.show();
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "첫번째 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "첫번째 권한 거부됨.", Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }
    public  String getDeviceId(Context context) {
        String deviceId = "";

        String android_id = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        deviceId = android_id;
        return deviceId;
    }
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.nav_home){
//            getSupportFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
//
//        }return true;
//    }
    public void fragmentChange(int index){
        if(index == 1){ //고객페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, customerFragment).commit();
        }
        else if(index==2){ // 푸드트럭 사업자 페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, businessFragment).commit();
        }
        else if(index==3){// 고객페이지 -> 위치정보검색
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, placeSetFragment).commit();
        }
        else if(index==4){ // 푸드트럭 사업자 페이지 -> 등록 페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, registerFragment).commit();
        }
        else if(index==5){ // 푸드 등록 페이지 -> 푸드트럭 등록 페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, informRegisterFragment).commit();
        }
        else if(index==6){ // 푸드 등록 페이지 -> 메뉴 등록 페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, menuRegisterFragment).commit();
        }
        else if(index==7){ // 푸드 등록 페이지 -> 운영정보등록페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, manageRegisterFragment).commit();
        }
        else if(index==9){ // 푸드 등록 페이지 -> 수정 선택페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, reviseChooseFragment).commit();
        }
        else if(index==8){ // 수정선택페이지 -> 메뉴수정페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, menuReviseFragment).commit();
        }
        else if(index==10){ // 수정선택페이지 -> 메뉴수정페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, planReviseFragment).commit();
        }
        else if(index==11){ // 사용자위치페이지 -> 위치검색페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mapViewFragment).commit();
        }
        else if(index==12){ // 사용자위치페이지 -> 위치검색페이지
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, foodTruckSearchFragment).commit();
        }


    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
    public void getcheck() throws SQLException {
        Connection con = null;
        con = DriverManager.getConnection("jdbc:mysql://3.35.19.7",
                "dlckdals56","gkfdkqjwl11");

        java.sql.Statement st =null;
        ResultSet rs = null;
        st = con.createStatement();
        rs = st.executeQuery("SHOW DATABASES");

        if (st.execute("SHOW DATABASES")) {

            rs = st.getResultSet();

        }


        while (rs.next()) {

            String str = rs.getNString(1);


        }



    }
}


