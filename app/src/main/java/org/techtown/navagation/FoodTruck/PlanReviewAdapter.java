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
import org.techtown.navagation.FoodTruckDatabase.PlanDeleteRequest;
import org.techtown.navagation.MainActivity;
import org.techtown.navagation.R;

import java.util.ArrayList;

public class PlanReviewAdapter extends RecyclerView.Adapter<PlanReviewAdapter.ViewHolder> {
    ArrayList<Plan> items = new ArrayList<Plan>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflater.inflate(R.layout.planreview,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Plan item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Plan item){
        items.add(item);
    }
    public void setItems(ArrayList<Plan> items){
        this.items = items;
    }
    public Plan getItem(int position){
        return items.get(position);

    }
    public void setItem(int position,Plan item){
        items.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;
        public ViewHolder(View itemView){

            super(itemView);
            textView = itemView.findViewById(R.id.opentext2);
            textView2 = itemView.findViewById(R.id.closetext2);
            textView3 = itemView.findViewById(R.id.daytext2);

        }
        public void setItem(Plan item){
            textView.setText(item.getOpen());
            textView2.setText(item.getClose());
            textView3.setText(item.getDay());
        }
    }
}
