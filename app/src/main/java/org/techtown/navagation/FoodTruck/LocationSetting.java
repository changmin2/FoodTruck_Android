package org.techtown.navagation.FoodTruck;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.techtown.navagation.Data;
import org.techtown.navagation.Dataplace;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;
import org.techtown.navagation.other.PersonProvider;

import java.util.List;

public class LocationSetting extends Fragment {
    Button nowplace;
    Button addressplace;
    TextView place;
    LocationManager locationManager;
    private static final int REQUEST_CODE_LOCATION = 2;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.placesetfragment, container, false);
        nowplace = root.findViewById(R.id.nowplace);
        Dataplace dataplace;
        addressplace = root.findViewById(R.id.addressplace);
        place = root.findViewById(R.id.addresstext);
        nowplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Location userLocation = getMyLocation();
                if( userLocation != null ) {
                    mainActivity.setLatitude(userLocation.getLatitude());
                    mainActivity.setLongitude(userLocation.getLongitude());
                    insert(userLocation.getLatitude(),userLocation.getLongitude());
                    Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();
                    mainActivity.fragmentChange(1);

                }
                else{
                    Toast.makeText(getContext(),"위치정보를 받아올 수 없습니다.",Toast.LENGTH_LONG).show();
                }
            }
        });
        addressplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(place.getText().toString())){
                    Toast.makeText(getContext(),"지역을 입력해주세요",Toast.LENGTH_LONG).show();
                }
                else{
                    try{
                        List<Double> list = mainActivity.getGeocode(place.getText().toString());
                        mainActivity.setLatitude(list.get(0));
                        mainActivity.setLongitude(list.get(1));
                        insert(list.get(0),list.get(1));
                        Toast.makeText(getContext(),"위치설정이 완료되었습니다",Toast.LENGTH_LONG).show();
                        mainActivity.fragmentChange(1);

                    }
                    catch (Exception e){
                        Toast.makeText(getContext(),"없는 지역입니다.",Toast.LENGTH_LONG).show();
                    }


                }

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
    public void insert(Double latitude,Double longitude){
        String uriString = "content://org.techtown.navagation/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        ContentValues values = new ContentValues();
        values.put("latitude",String.valueOf(latitude));
        values.put("longitude",String.valueOf(longitude));
        uri = getContext().getContentResolver().insert(uri,values);
    }
}
