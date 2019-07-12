package com.example.changeme.kitchenapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.changeme.kitchenapp.FoodDetails;
import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.Model.Favourites;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.R;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {

    private Context context;
    private List<Favourites> favouritesList;

    public FavouriteAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favourites_item,parent,false);

        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder viewHolder, final int position) {

        viewHolder.food_name.setText(favouritesList.get(position).getFoodname());
        viewHolder.cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Database(context).addToCart(new Order(
                        Common.currentuser.getPhone(),
                        favouritesList.get(position).getFoodid(),
                        favouritesList.get(position).getFoodname(),
                        "1",
                        favouritesList.get(position).getFoodprice(),
                        favouritesList.get(position).getFooddiscount()
                ));


            }
        });

        Picasso.with(context).load(favouritesList.get(position).getFoodimage()).into(viewHolder.food_image);





        final Favourites local = favouritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent life_detail = new Intent(context, FoodDetails.class);
                life_detail.putExtra("FoodId", favouritesList.get(position).getFoodid());
                context.startActivity(life_detail);


            }
        });




    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public void removeItem(int position){
        favouritesList.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Favourites item,int position){
        favouritesList.add(position,item);
        notifyItemInserted(position);

    }

    public Favourites getItem(int position){
        return  favouritesList.get(position);
    }
}
