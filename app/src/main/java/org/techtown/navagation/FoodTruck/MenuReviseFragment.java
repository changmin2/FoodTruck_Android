package org.techtown.navagation.FoodTruck;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.MenuQueryRequest;
import org.techtown.navagation.FoodTruckDatabase.MenuRegisterRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;
public class MenuReviseFragment extends Fragment {
    MenuAdapter adapter;
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
    TextView menu;
    TextView price;
    Button register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View root =  inflater.inflate(R.layout.fragment1,container,false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        menu = root.findViewById(R.id.reviseopen);
        price = root.findViewById(R.id.reviseclose);
        register = root.findViewById(R.id.planreviseregister);
        String deviceId = mainActivity.getDeviceId(getContext());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int integercheck=0;
                try{
                    Integer.parseInt(price.getText().toString());
                }
                catch (Exception e){
                    integercheck=1;
                    Toast.makeText(getContext(),"가격에는 숫자만 입력해주세요",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(menu.getText())||TextUtils.isEmpty(price.getText())){
                    Toast.makeText(getContext(),"내용을 빈곳없이 채워주세요",Toast.LENGTH_LONG).show();
                }
                else if(integercheck==0){
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
                    MenuRegisterRequest menuRegisterRequest = new MenuRegisterRequest(deviceId,menu.getText().toString(),price.getText().toString(),responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(menuRegisterRequest);
                    adapter.addItem(new Menu(menu.getText().toString(),price.getText().toString()));
                    adapter.notifyDataSetChanged();
                }

            }
        });
        adapter = new MenuAdapter();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for( int i =0;i<jsonArray.length();i++){
                        JSONObject JSONObject = jsonArray.getJSONObject(i);

                        adapter.addItem(new Menu(JSONObject.getString("name"),JSONObject.getString("price")));
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        MenuQueryRequest go = new MenuQueryRequest(deviceId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(go);
        recyclerView.setAdapter(adapter);
        return root;

    }
}
