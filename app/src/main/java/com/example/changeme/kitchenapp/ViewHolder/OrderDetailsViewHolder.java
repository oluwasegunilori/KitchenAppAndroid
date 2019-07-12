package com.example.changeme.kitchenapp.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.changeme.kitchenapp.R;

import de.codecrafters.tableview.SortableTableView;

public class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
    public TextView order_id, order_time, order_status, order_total, p_name, quant, price;
    public SortableTableView<String[]> tableLayout;
    public LinearLayout buts;
    public Button cancel, track;
    public  Context context;
    String [] headers = {"Product", "Name", "Price"};
    public OrderDetailsViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        order_total =  itemView.findViewById(R.id.order_total);

        order_id =  itemView.findViewById(R.id.order_id);
        order_time =  itemView.findViewById(R.id.order_time);
        order_status =  itemView.findViewById(R.id.order_status);
        cancel = itemView.findViewById(R.id.cancel_order);
        track = itemView.findViewById(R.id.track_order);
        buts = itemView.findViewById(R.id.product_list);
        p_name =itemView.findViewById(R.id.productname);
        quant =itemView.findViewById(R.id.quantity);
        price = itemView.findViewById(R.id.price);




    }

}
