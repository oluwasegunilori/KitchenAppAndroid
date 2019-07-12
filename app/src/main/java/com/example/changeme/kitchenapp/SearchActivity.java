package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.Model.Favourites;
import com.example.changeme.kitchenapp.Model.Food;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Utils;
import com.example.changeme.kitchenapp.ViewHolder.FoodViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SearchActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchadapter;
    List<String> suggestlist = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    FirebaseDatabase database;
    DatabaseReference foodlist;
    ProgressBar progressBar2;
    TextView txtFullname;
    SwipeRefreshLayout swipeRefreshLayout;
    Food currentfood;
    RecyclerView reycler_menu;
    RecyclerView.LayoutManager layoutManager;
    Database localDB;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Life");

        progressBar2 = findViewById(R.id.progress_load_photosearch);

        localDB = new Database(this);
        reycler_menu = findViewById(R.id.recycler_search);
        reycler_menu.setHasFixedSize(true);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 2));
        }else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 1));
        }

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(reycler_menu.getContext()
                ,R.anim.layout_fall_down);
            reycler_menu.setLayoutAnimation(controller);
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.gotoCart);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irt = new Intent(SearchActivity.this, Cart.class);
                startActivity(irt);

            }
        });
        if(Common.currentuser==null){
            Intent inte = new Intent(SearchActivity.this, MainActivity.class);
            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(inte);

        }
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchbar);
        materialSearchBar.setHint("Search Food");
        loadSuggest();
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest  = new ArrayList<String>();
                for(String search:suggestlist){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    loadAllFoods();
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {


                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                    loadAllFoods();
            }
        });


        loadAllFoods();



    }

    private void loadAllFoods() {
        if(Common.isConnectedToInternet(getBaseContext())){
        errorlayout.setVisibility(View.GONE);
        Query searchByName = foodlist;
        FirebaseRecyclerOptions<Food> foodoptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodoptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {
                progressBar2.setVisibility(View.GONE);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.centerCrop();
                requestOptions.timeout(3000);

                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.food_image);


                viewHolder.food_name.setText(model.getName());


                if (localDB.isFavourites(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);


                viewHolder.cart_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            if(!(model.getName().contains("-"))) {

                                getDetailLife(adapter.getRef(position).getKey());
                            new Database(getBaseContext()).addToCart(new Order(
                                    Common.currentuser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getPrice(),
                                    model.getDiscount()
                            ));

                            Toast.makeText(SearchActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        }else{

                            Snackbar b = Snackbar.make(v,"Food is not Available",Snackbar.LENGTH_LONG);
                            b.show();
                        }


                    } else {
                            Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            Favourites favourites = new Favourites();
                            favourites.setFoodid(adapter.getRef(position).getKey());
                            favourites.setFoodname(model.getName());
                            favourites.setFooddescription(model.getDescription());
                            favourites.setFooddiscount(model.getDiscount());
                            favourites.setFoodimage(model.getImage());
                            favourites.setFoodmenuid(model.getMenuid());
                            favourites.setUserphone(Common.currentuser.getPhone());
                            favourites.setFoodprice(model.getPrice());


                            if (!localDB.isFavourites(adapter.getRef(position).getKey())) {

                                localDB.addToFavourites(favourites);
                                viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(SearchActivity.this, "" + model.getName() + "was added to favourites", Toast.LENGTH_SHORT).show();

                            } else {

                                localDB.removeFavourites(favourites.getFoodid(), favourites.getUserphone());
                                viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(SearchActivity.this, "" + model.getName() + "was removed from  favourites", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (Common.isConnectedToInternet(getBaseContext())) {
                    if (!(model.getName().contains("-"))){
                            Intent life_detail = new Intent(SearchActivity.this, FoodDetails.class);
                            life_detail.putExtra("FoodId", adapter.getRef(position).getKey());
                            startActivity(life_detail);


                        }else{

                            Snackbar b = Snackbar.make(view,"Food is not Available",Snackbar.LENGTH_LONG);
                            b.show();
                        }
                        } else {
                            Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }
        };
        adapter.startListening();
        reycler_menu.setAdapter(adapter);


    } else{
        showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

    }
    }

    private void startSearch(CharSequence text) {

        Query searchByName = foodlist.orderByChild("name").equalTo(text.toString());
        FirebaseRecyclerOptions<Food> foodoptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();

        searchadapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodoptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {

                progressBar2.setVisibility(View.GONE);


                RequestOptions requestOptions  = new RequestOptions();
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.centerCrop();
                requestOptions.timeout(3000);

                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.food_image);





                viewHolder.food_name.setText(model.getName());
                viewHolder.cart_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            if(!(model.getName().contains("-"))) {

                                getDetailLife(adapter.getRef(position).getKey());
                                new Database(getBaseContext()).addToCart(new Order(
                                        Common.currentuser.getPhone(),
                                        adapter.getRef(position).getKey(),
                                        model.getName(),
                                        "1",
                                        model.getPrice(),
                                        model.getDiscount()
                                ));

                                Toast.makeText(SearchActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            }else{

                                Snackbar b = Snackbar.make(v,"Food is not Available",Snackbar.LENGTH_LONG);
                                b.show();
                            }


                        } else {
                            Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            Favourites favourites = new Favourites();
                            favourites.setFoodid(adapter.getRef(position).getKey());
                            favourites.setFoodname(model.getName());
                            favourites.setFooddescription(model.getDescription());
                            favourites.setFooddiscount(model.getDiscount());
                            favourites.setFoodimage(model.getImage());
                            favourites.setFoodmenuid(model.getMenuid());
                            favourites.setUserphone(Common.currentuser.getPhone());
                            favourites.setFoodprice(model.getPrice());


                            if (!localDB.isFavourites(adapter.getRef(position).getKey())) {

                                localDB.addToFavourites(favourites);
                                viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(SearchActivity.this, "" + model.getName() + "was added to favourites", Toast.LENGTH_SHORT).show();

                            } else {

                                localDB.removeFavourites(favourites.getFoodid(), favourites.getUserphone());
                                viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(SearchActivity.this, "" + model.getName() + "was removed from  favourites", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            if(!(model.getName().contains("-"))){
                            Intent life_detail = new Intent(SearchActivity.this, FoodDetails.class);
                            life_detail.putExtra("FoodId", searchadapter.getRef(position).getKey());
                            startActivity(life_detail);

                        }else{

                            Snackbar b = Snackbar.make(view,"Food is not Available",Snackbar.LENGTH_LONG);
                            b.show();
                        }


                    }
else {
                                Toast.makeText(SearchActivity.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                            }

                    }
                });


            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent,false);
                return new FoodViewHolder(itemView);

            }
        };
        searchadapter.startListening();
        reycler_menu.setAdapter(searchadapter);

    }

    private void loadSuggest() {

        foodlist.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                            Food item = postSnapshot.getValue(Food.class);
                            suggestlist.add(item.getName());
                        }

                        materialSearchBar.setLastSuggestions(suggestlist);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {




                    }
                });



    }
    private void getDetailLife(String FoodId) {

        foodlist.child(FoodId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentfood = dataSnapshot.getValue(Food.class);






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
        );
    }

    @Override
    protected void onStop() {
        if(adapter!=null){
            adapter.stopListening();

        }
        if(searchadapter!=null){

            searchadapter.stopListening();
        }
        super.onStop();
    }
    private  void showErrorMessage(int imageview, String title, String message){
        if(errorlayout.getVisibility()== GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadAllFoods();
            }
        });
    }

    @Override
    protected void onResume() {

       super.onResume();
       adapter.startListening();




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Common.isConnectedToInternet(getBaseContext())) {


            Intent soff = new Intent(SearchActivity.this, Home.class);
            soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(soff);}
        else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

        }

    }
}
