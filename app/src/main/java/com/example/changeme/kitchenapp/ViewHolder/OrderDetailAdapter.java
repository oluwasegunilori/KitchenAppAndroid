package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.changeme.kitchenapp.R;

import de.codecrafters.tableview.TableView;

/**
 * Created by SHEGZ on 1/6/2018.
 */

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView order_id, order_time, order_status, order_total;
    String order_id_value = "";
    String [] [] datare;
    String [] headers = {"Product", "Name", "Price"};


    public MyViewHolder(View itemView){
        super(itemView);
        order_total =  itemView.findViewById(R.id.order_total);

        order_id =  itemView.findViewById(R.id.order_id);
        order_time =  itemView.findViewById(R.id.order_time);
        order_status =  itemView.findViewById(R.id.order_status);
        final TableView<String[]> tableLayout =  itemView.findViewById(R.id.product_list);


    }
}
