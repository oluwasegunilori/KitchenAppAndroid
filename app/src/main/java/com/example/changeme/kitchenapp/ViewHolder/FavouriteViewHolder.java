package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.R;

public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_image, fav_image, cart_image;
    public RelativeLayout viewbackground;
    public RelativeLayout viewforeground;

    private ItemClickListener itemClickListener;

    public FavouriteViewHolder(View itemView) {
        super(itemView);

        food_name =  itemView.findViewById(R.id.life_name);
        food_image = itemView.findViewById(R.id.life_image);
        fav_image =  itemView.findViewById(R.id.fav);
        cart_image = itemView.findViewById(R.id.carter);
        viewbackground = itemView.findViewById(R.id.viewbackground);
        viewforeground = itemView.findViewById(R.id.viewforeground);


        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}