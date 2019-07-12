package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.changeme.kitchenapp.Model.Food;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Rating;
import com.example.changeme.kitchenapp.Model.Utils;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

import info.hoang8f.widget.FButton;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener {
        TextView life_name, life_decr, life_descr2;
    ImageView life_imag;
    ImageView imag_back;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton  btnrating;
    FButton showcomment,btncart;
    Button Signer;
    ElegantNumberButton numberButton;
    String fooddetailsID = "";
    RatingBar ratingbar;
    FirebaseDatabase database;
    ProgressBar progressBar;
    DatabaseReference ratingstbl;
    DatabaseReference lifes;
    Food currentlife;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarss);
        toolbar.setTitle("  ");
        toolbar.setTitleTextColor(Color.RED);
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        imag_back = findViewById(R.id.imag_back);


        progressBar = findViewById(R.id.progress_load_photo2);
        showcomment = findViewById(R.id.btnshowComment);
        if(Common.currentuser==null){
            Intent inte = new Intent(FoodDetails.this, MainActivity.class);

            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(inte);

        }
        showcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (Common.currentuser != null && fooddetailsID != null
                            && currentlife != null) {

                        Intent intent = new Intent(FoodDetails.this, Showcomment.class);
                        intent.putExtra(Common.INTENT_FOOD_ID, fooddetailsID);
                        startActivity(intent);


                    } else {

                    }

                }else{
                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");


                }


            }
        });

database = FirebaseDatabase.getInstance();
        lifes = database.getReference("Life");
        numberButton =  findViewById(R.id.numberbutton);
        btncart =  findViewById(R.id.btnCart);
        ratingbar  = findViewById(R.id.ratinBar);
        ratingstbl = database.getReference("Rating");

        btnrating = (FloatingActionButton) findViewById(R.id.btn_rating);

        btnrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    showRatinDialog();}
                    else{
                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");


                }
            }
        });



        btncart.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(Common.currentuser !=null && fooddetailsID !=null
                            && currentlife!=null) {

                        if(!(currentlife.getName().contains("-"))) {

                            new Database(getBaseContext()).addToCart(new Order(
                                Common.currentuser.getPhone(),
                                    fooddetailsID,
                                currentlife.getName(),
                                numberButton.getNumber(),
                                currentlife.getPrice(),
                                currentlife.getDiscount()));
                        Toast.makeText(FoodDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        Intent cartintent = new Intent(FoodDetails.this, Cart.class);
                        startActivity(cartintent);

                        }else{

                            Snackbar b = Snackbar.make(view,R.string.notready,Snackbar.LENGTH_LONG);
                            b.show();

                        }

                    }else{

                    }
                } else {
                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                }




            }
        });






        life_name = (TextView) findViewById(R.id.fname);
        life_decr = (TextView) findViewById(R.id.fdescription);
        life_descr2 = (TextView) findViewById(R.id.fdescription2);
            life_imag = (ImageView) findViewById(R.id.imag_life);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        if(getIntent()!= null)
            fooddetailsID = getIntent().getStringExtra("FoodId");




        if(!fooddetailsID.isEmpty()){


            if(Common.isConnectedToInternet(getBaseContext())){

                getDetailLife(fooddetailsID);
                getRatinfFood(fooddetailsID);

            } else{
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");
                return;
            }

        }



    }

    private void getRatinfFood(String FoodId) {
        Query foodRating = ratingstbl.orderByChild("foodId").equalTo(FoodId);
        foodRating.addValueEventListener(new ValueEventListener() {

            int count =0, sum =0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;


                }

                if(count != 0){
                float average  = sum/count;
                ratingbar.setRating(average);
            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showRatinDialog() {

         new AppRatingDialog.Builder()
                 .setPositiveButtonText("Submit")
                 .setNegativeButtonText("Cancel")
                 .setNoteDescriptions(Arrays.asList("Very Bad,","Not Good", "Quite ok", "Very Good", "Excellent"))
                 .setDefaultRating(1)
                 .setTitle("Rate this food")
                 .setDescription("Please select some stars and give your feedback")
                 .setTitleTextColor(R.color.colorPrimary)
                 .setHint("Please write your comments here")
                 .setHintTextColor(R.color.colorAccent)
                 .setCommentTextColor(android.R.color.white)
                 .setCommentBackgroundColor(R.color.colorPrimaryDark)
                 .setWindowAnimation(R.style.RatingDialogFadeAnim)
                 .create(FoodDetails.this)
                 .show();



    }

    public  void getDetailLife(String FoodId) {
        if(Common.isConnectedToInternet(getBaseContext())) {
            errorlayout.setVisibility(View.GONE);
            lifes.child(FoodId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentlife = dataSnapshot.getValue(Food.class);


                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(Utils.getRandomDrawbleColor());
                    requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                    requestOptions.centerCrop();
                    requestOptions.timeout(3000);

                    Glide.with(getBaseContext())
                            .load(currentlife.getImage())
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);

                                    return false;
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(life_imag)
                            ;
                    Glide.with(getBaseContext())
                            .load(currentlife.getImage())
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);

                                    return false;
                                }
                            })
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imag_back)
                    ;




                    collapsingToolbarLayout.setTitle(currentlife.getName());
                    life_name.setText(currentlife.getName());
                    life_decr.setText(currentlife.getDescription());

                    life_descr2.setText(currentlife.getPrice());


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

        }
        }


    @Override
    public void onPositiveButtonClicked(int value,  String comments) {
        final Rating rating = new Rating(Common.currentuser.getPhone(),
                fooddetailsID,String.valueOf(value),comments);


        ratingstbl.push().setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   Toast.makeText(FoodDetails.this, "Thanks for submitting rating!!", Toast.LENGTH_SHORT).show();
                    }
                });
/**
        ratingstbl.child(Common.currentuser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentuser.getPhone()).exists()){

                    ratingstbl.child(Common.currentuser.getPhone()).removeValue();

                    ratingstbl.child(Common.currentuser.getPhone()).setValue(rating);
                }
                else{


                    ratingstbl.child(Common.currentuser.getPhone()).setValue(rating);

                }
                Toast.makeText(FoodDetails.this, "Thank You For Submitting Rating", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
**/
    }

    @Override
    public void onNegativeButtonClicked() {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.carts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.Cart){
            if(Common.isConnectedToInternet(getBaseContext())){
            Intent orders = new Intent(FoodDetails.this, Cart.class);
            startActivity(orders);
            finish();}
            else{
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }


        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Common.isConnectedToInternet(getBaseContext())) {
            finish();
        }        else{

            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");


        }


    }
    private  void showErrorMessage(int imageview, String title, String message){
        if(errorlayout.getVisibility()== View.GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);
        new loadBackground().execute();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDetailLife(fooddetailsID);
            }
        });
    }
    public  class loadBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            if(Common.isConnectedToInternet(getBaseContext())){
                errorlayout.setVisibility(View.GONE);
                recreate();

            }



            return null;



        }
    }
}
