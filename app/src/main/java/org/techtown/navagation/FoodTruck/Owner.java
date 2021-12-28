package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Owner extends Fragment {
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
    Button placeset;
    MainActivity mainActivity;
    Button placebase;
    Button foodtrucksearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.customerfragment,container,false);
        placeset = root.findViewById(R.id.placeset);
        placebase = root.findViewById(R.id.placebase);
        foodtrucksearch = root.findViewById(R.id.foodtrucksearch);
        getKeyHash(getContext());
        placeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.fragmentChange(3);
            }
        });
        placebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.fragmentChange(11);
            }
        });
        foodtrucksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.fragmentChange(12);
            }
        });
        return root;
    }

    public static void getKeyHash(Context context){
        PackageManager pm = context.getPackageManager();
        try{
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for(int i = 0; i < packageInfo.signatures.length; i++){
                Signature signature = packageInfo.signatures[i];
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("<클래스명>", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
}
