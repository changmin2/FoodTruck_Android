package org.techtown.navagation.FoodTruck;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.navagation.FoodTruckInfo;
import org.techtown.navagation.R;

import java.util.ArrayList;

public class FoodTruckAdapter extends RecyclerView.Adapter<FoodTruckAdapter.ViewHolder> {
    ArrayList<FoodTruck> items = new ArrayList<FoodTruck>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflater.inflate(R.layout.foodtrucklist,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FoodTruck item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FoodTruckInfo.class);
                intent.putExtra("deviceId",String.valueOf(viewHolder.foddtruckdevice.getText().toString()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(FoodTruck item){
        items.add(item);
    }
    public void setItems(ArrayList<FoodTruck> items){
        this.items = items;
    }
    public FoodTruck getItem(int position){
        return items.get(position);

    }
    public void clear(){
        items.clear();
    }
    public void setItem(int position,FoodTruck item){
        items.set(position,item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView listname;
        TextView listpayment;
        Button listbutton;
        TextView foddtruckdevice;
        public ViewHolder(View itemView){

            super(itemView);
            listname = itemView.findViewById(R.id.listname);
            listpayment = itemView.findViewById(R.id.listpayment);
            listbutton = itemView.findViewById(R.id.listbutton);
            foddtruckdevice = itemView.findViewById(R.id.foodTruckDevice);

        }
        public void setItem(FoodTruck item){
            listname.setText(item.getName());
            listpayment.setText(item.getPayment());
            foddtruckdevice.setText(item.getDeviceId());
        }
    }
}
