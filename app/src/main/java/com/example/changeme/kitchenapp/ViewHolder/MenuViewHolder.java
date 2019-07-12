package com.example.changeme.kitchenapp.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.R;

/**
 * Created by SHEGZ on 12/30/2017.
 */
public class MenuViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

public TextView textmenuName;
public ProgressBar progressBar;
    public ImageView imageView;

    private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);

        textmenuName = (TextView) itemView.findViewById(R.id.manu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        progressBar = itemView.findViewById(R.id.progress_load_photo);
        itemView.setOnClickListener(this);

    }
    public  void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }



    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
