package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.changeme.kitchenapp.Interface.RecyclerItemTouchHelperListener;
import com.example.changeme.kitchenapp.Model.Favourites;
import com.example.changeme.kitchenapp.ViewHolder.FavouriteAdapter;
import com.example.changeme.kitchenapp.ViewHolder.FavouriteViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.example.changeme.kitchenapp.helper.RecyclerItemTouchHelper;

public class FavouriteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    FavouriteAdapter adapter;
    RelativeLayout layout;
    RecyclerView reycler_menu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Favourites");

        setSupportActionBar(toolbar);
        layout = findViewById(R.id.roor_layout);
        reycler_menu = findViewById(R.id.recycler_fav);
        reycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reycler_menu.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(reycler_menu.getContext()
                ,R.anim.layout_fall_down);
                    reycler_menu.setLayoutAnimation(controller);

        ItemTouchHelper.SimpleCallback itemtouchhelpercallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemtouchhelpercallback).attachToRecyclerView(reycler_menu);

        loadFavourites();


    }

    private void loadFavourites() {

        adapter = new FavouriteAdapter(this, new Database(this).getAllFavourites(Common.currentuser.getPhone()));
        reycler_menu.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof FavouriteViewHolder){
            String name =((FavouriteAdapter) reycler_menu.getAdapter()).getItem(position).getFoodname();

            final Favourites deleteitem = ((FavouriteAdapter) reycler_menu.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteindex = viewHolder.getAdapterPosition();

            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFavourites(deleteitem.getFoodid(), Common.currentuser.getPhone());

            Snackbar snackbar = Snackbar.make(layout, name+"removed from favourites!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteitem,deleteindex);
                    new Database(getBaseContext()).addToFavourites(deleteitem);


                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if(item.getItemId() == R.id.carting){
          startActivity(new Intent(FavouriteActivity.this, Cart.class));

        }

        return super.onOptionsItemSelected(item);
    }


 }
