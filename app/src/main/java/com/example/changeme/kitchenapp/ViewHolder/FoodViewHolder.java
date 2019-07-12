package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.R;

/**
 * Created by SHEGZ on 12/31/2017.
 */
public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ProgressBar progressBar;
    public ImageView food_image, fav_image, cart_image;

    private ItemClickListener itemClickListener;
    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView) itemView.findViewById(R.id.life_name);
        food_image = (ImageView) itemView.findViewById(R.id.life_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);
        cart_image = itemView.findViewById(R.id.carter);
        itemView.setOnClickListener(this);
        progressBar = itemView.findViewById(R.id.progress_load_photo1);

    }
    public  void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }



    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}

