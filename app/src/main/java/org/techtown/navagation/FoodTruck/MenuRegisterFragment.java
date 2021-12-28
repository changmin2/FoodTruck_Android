package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.MenuRegisterRequest;
import org.techtown.navagation.FoodTruckDatabase.RegisterRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

public class MenuRegisterFragment extends Fragment {
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
    Button menuRegister;
    TextView menuName;
    TextView menuPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.menuregisterfragment,container,false);
        menuName = root.findViewById(R.id.menuname);
        menuPrice = root.findViewById(R.id.menuprice);
        menuRegister = root.findViewById(R.id.menuregister);
        menuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceId = mainActivity.getDeviceId(getContext());

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
                MenuRegisterRequest menuRegisterRequest = new MenuRegisterRequest(deviceId,menuName.getText().toString(),menuPrice.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(menuRegisterRequest);
                Toast.makeText(getContext(),"등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                mainActivity.fragmentChange(4);
            }
        });
        return root;
    }
}
