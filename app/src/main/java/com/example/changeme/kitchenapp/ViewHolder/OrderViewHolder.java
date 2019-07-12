package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.R;

/**
 * Created by SHEGZ on 1/1/2018.
 */
public class OrderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener

{

public TextView txtOrderid, txtOrderstatus, txtOrderdate;
public ImageView butndelete;

private ItemClickListener itemClickListener;
    public OrderViewHolder (View itemView) {
        super(itemView);
       txtOrderid = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderstatus = (TextView) itemView.findViewById(R.id.order_status);
       txtOrderdate = itemView.findViewById(R.id.order_time);
        butndelete = itemView.findViewById(R.id.delete_order);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(),false);

    }
}