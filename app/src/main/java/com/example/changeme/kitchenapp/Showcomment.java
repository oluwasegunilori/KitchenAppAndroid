package com.example.changeme.kitchenapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.Rating;
import com.example.changeme.kitchenapp.ViewHolder.ShowCommentViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Showcomment extends AppCompatActivity {
RecyclerView recyclerView;
FirebaseDatabase firebaseDatabase;
ProgressBar progressBar;
DatabaseReference ratingTbl;
FirebaseRecyclerAdapter<Rating,ShowCommentViewHolder> adapter;
RecyclerView.LayoutManager layoutManager;
String foodId = "";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
          }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showcomment);
        firebaseDatabase  = FirebaseDatabase.getInstance();
        ratingTbl = firebaseDatabase.getReference("Rating");
        progressBar = findViewById(R.id.progress_load_photoshowComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclercomment);
        recyclerView.setLayoutManager(layoutManager);
        if(Common.currentuser==null){
            Intent inte = new Intent(Showcomment.this, MainActivity.class);
            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            startActivity(inte);

        }
    if(getIntent() !=null)
        foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
        if(!foodId.isEmpty() && foodId !=null){
            final Query query = ratingTbl.orderByChild("foodId").equalTo(foodId);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position, @NonNull Rating model) {

                            progressBar.setVisibility(View.GONE);
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUser.setText(model.getUserPhone());

                        }

                        @Override
                        public int getItemCount() {
                            return super.getItemCount();
                        }

                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.showcommentlayout,parent,false );
                            return new ShowCommentViewHolder(view);

                        }
                    };
                    loadComment(foodId);



                }else{
                    progressBar.setVisibility(View.GONE);
                    Snackbar b = Snackbar.make(getWindow().getDecorView().getRootView(),"No Comments have been made",Snackbar.LENGTH_LONG);
                    b.show();  }










                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }
        else{
            progressBar.setVisibility(View.GONE);

            Toast.makeText(Showcomment.this, "No Comments made yet", Toast.LENGTH_LONG).show();


        }

    }

    private void loadComment(String foodId) {
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onResume() {
        super.onResume();


    }

}
