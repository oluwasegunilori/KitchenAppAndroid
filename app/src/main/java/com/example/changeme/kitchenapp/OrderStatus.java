package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.Model.Request;
import com.example.changeme.kitchenapp.ViewHolder.OrderViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {
public RecyclerView recyclerView;
    public String myphone;
    FirebaseDatabase database;
    DatabaseReference requests;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    private RelativeLayout errorlayout;
    private TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        myphone = Common.currentuser.getPhone();
               recyclerView = (RecyclerView) findViewById(R.id.listorders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        recyclerView.setLayoutManager(layoutManager);


        loadOrders(myphone);

    }

    private void loadOrders(String phone) {


        if (Common.isConnectedToInternet(getBaseContext())) {


            Query orderVyUser = requests.orderByChild("phone")
                .equalTo(phone);
            FirebaseRecyclerOptions<Request> foodoptions = new FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(orderVyUser, Request.class)
                    .build();
            adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(foodoptions) {
                @Override
                protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull final Request model) {

                    progressBar.setVisibility(View.GONE);

                    viewHolder.txtOrderid.setText("Order Id - "+adapter.getRef(position).getKey());
                    viewHolder.txtOrderstatus.setText("Order Status - "+Common.convertCodeToStatus(model.getStatus()));
                    viewHolder.txtOrderdate.setText("Date - " + model.getDate()) ;
                    viewHolder.butndelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Common.isConnectedToInternet(getBaseContext())){
                            if(adapter.getItem(position).getStatus().equals("0")) {
                                deleteOrder(adapter.getRef(position).getKey());
                            }else{
                                    Toast.makeText(OrderStatus.this, "You cannot delete this order. It has been shipped already"
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(OrderStatus.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        }
                    });


                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {

                            if(Common.isConnectedToInternet(getBaseContext())) {
                                if (!isLongClick) {


                                    Intent intent2 = new Intent(OrderStatus.this, OrderDetails.class);
                                    Common.currentRequest = model;
                                    intent2.putExtra("OrderId", adapter.getRef(position).getKey());
                                    startActivity(intent2);
                                }
                            }else{
                                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                            }

                        }
                    });

                }

                private void deleteOrder(final String key) {
                    if(Common.isConnectedToInternet(getBaseContext())){
                        requests.child(key)
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(OrderStatus.this, new StringBuilder("Order ").append(key)
                                        .append(" has been deleted").toString()
                                , Toast.LENGTH_SHORT).show();
                                loadOrders(myphone);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(OrderStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();



                            }
                        });}
                        else{
                        showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                    }
                }

                @NonNull
                @Override
                public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.orderlayout,parent,false);
                    return new OrderViewHolder(itemView);

                }
                @Override
                public Request getItem(int position) {
                    return super.getItem(getItemCount() - 1 - position);
                }


                @Override
                public void onDataChanged() {
                    recyclerView.removeAllViews();
                    super.onDataChanged();
                }


            };
            adapter.startListening();
recyclerView.setAdapter(adapter);

    }else {
            Toast.makeText(OrderStatus.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

        }
        }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Order Status: Placed";
        else if(status.equals("1"))
            return "Order Status: On my way";
        else
            return "Order Status: Shipped";

    }
    private  void showErrorMessage(int imageview, String title, String message){
        if(errorlayout.getVisibility()== View.GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           loadOrders(myphone);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders(myphone);
    }
}
