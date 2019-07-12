package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.R;
import com.example.changeme.kitchenapp.common.Common;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        , View.OnCreateContextMenuListener{


    public TextView txt_cartname, txt_price;
    public ElegantNumberButton img_cartCount;
    public RelativeLayout viewbackground;
    public LinearLayout viewforeground;
    private ItemClickListener itemClickListener;
    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cartname = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_price);
        img_cartCount = itemView.findViewById(R.id.button_quantity);
        viewbackground = itemView.findViewById(R.id.viewbackground);
        viewforeground = itemView.findViewById(R.id.viewforeground);

        itemView.setOnCreateContextMenuListener(this);

    }

    public void setTxt_cartname(TextView txt_cartname){
        this.txt_cartname = txt_cartname;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}

