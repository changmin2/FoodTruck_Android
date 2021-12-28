package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.techtown.navagation.BusinessIntoInform;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

public class MainFragment extends Fragment {
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

    Button customer;
    Button business;
    MainActivity mainActivity;
    String deviceId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.mainfragment,container,false);
        customer = root.findViewById(R.id.foodtruckregister);
        business = root.findViewById(R.id.foodtruckrevise);
        deviceId = mainActivity.getDeviceId(getContext());
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainActivity.fragmentChange(1);
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BusinessIntoInform.class);
                intent.putExtra("deviceId",deviceId);
                v.getContext().startActivity(intent);
            }
        });
        return root;
    }
}
