package org.techtown.navagation.FoodTruck;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.navagation.FoodTruckDatabase.MenuDeleteRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.util.ArrayList;

public class MenuReviewAdapter extends RecyclerView.Adapter<MenuReviewAdapter.ViewHolder> {
    ArrayList<Menu> items = new ArrayList<Menu>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflater.inflate(R.layout.review_menu,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Menu item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Menu item){
        items.add(item);
    }
    public void setItems(ArrayList<Menu> items){
        this.items = items;
    }
    public Menu getItem(int position){
        return items.get(position);

    }
    public void setItem(int position,Menu item){
        items.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        Button button;
        public ViewHolder(View itemView){

            super(itemView);
            textView = itemView.findViewById(R.id.menumenu2);
            textView2 = itemView.findViewById(R.id.priceprice2);


        }
        public void setItem(Menu item){
            textView.setText(item.getName());
            textView2.setText(item.getPrice());
        }
    }
}
