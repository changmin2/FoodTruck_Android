package org.techtown.navagation.FoodTruck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.util.List;

public class PlaceMapViewFragment extends Fragment {
    Button placesearch;
    TextView search_text;
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
        View root = inflater.inflate(R.layout.placemapviewfragment, container, false);
        search_text = root.findViewById(R.id.searchtext);
        placesearch = root.findViewById(R.id.searchbutton);
        placesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double> list = mainActivity.getGeocode(search_text.getText().toString());

            }
        });
        RelativeLayout mapViewContainer = root.findViewById(R.id.mapView2);
        MapView mapView = new MapView(getActivity());
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(35.2408321,128.6886606);
        mapViewContainer.addView(mapView);
        return root;
    }
}
