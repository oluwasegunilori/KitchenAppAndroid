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

import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Request;
import com.example.changeme.kitchenapp.Model.Utils;
import com.example.changeme.kitchenapp.ViewHolder.OrderDetailsViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class OrderDetails extends AppCompatActivity {
    public  String myphone;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference requests;
    String [] [] datare;
    String [] headers = {"Product", "Name", "Price"};
    FirebaseRecyclerAdapter<Request, OrderDetailsViewHolder> adapter;
    RecyclerView loadOrders;
    RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorlayout;
    private TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        loadOrders = findViewById(R.id.listorders);
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        // FloatingActionButton     fcallle = (FloatingActionButton) findViewById(R.id.callere);
        progressBar = findViewById(R.id.progress_load_photoorders);
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        loadOrders.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        if(Common.currentuser.getPhone() !=null) {
            myphone = Common.currentuser.getPhone();
        }else{
            startActivity(new Intent(OrderDetails.this, MainActivity.class));
        }
        loadOrders.setLayoutManager(layoutManager);
        loadOrders(myphone);
    }

    private void showErrorMessage(int imageview, String title, String message) {
        if (errorlayout.getVisibility() == View.GONE) {
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
    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Order Status: Placed";
        else if(status.equals("1"))
            return "Order Status: On my way";
        else
            return "Order Status: Shipped";

    }

    private void loadOrders(String phone) {


        if (Common.isConnectedToInternet(getBaseContext())) {


            Query orderVyUser = requests.orderByChild("phone")
                    .equalTo(phone);
            FirebaseRecyclerOptions<Request> foodoptions = new FirebaseRecyclerOptions.Builder<Request>()
                    .setQuery(orderVyUser, Request.class)
                    .build();
            adapter = new FirebaseRecyclerAdapter<Request, OrderDetailsViewHolder>(foodoptions) {
                @Override
                protected void onBindViewHolder(@NonNull OrderDetailsViewHolder viewHolder, final int position, @NonNull final Request model) {

                    progressBar.setVisibility(View.GONE);
                    viewHolder.tableLayout.setHeaderAdapter(new SimpleTableHeaderAdapter(getBaseContext(), headers));

                    viewHolder.order_id.setText("Order ID - " + adapter.getRef(position).getKey());

                    viewHolder.order_status.setText(convertCodeToStatus(Common.convertCodeToStatus(model.getStatus())));
                    viewHolder.order_total.setText(model.getTotal());
                    viewHolder.order_time.setText(Utils.DateToTimeFormat(model.getTime()));


                    datare = new String[model.getLifes().size()][3];
                    for(int i = 0; i< model.getLifes().size(); i++){
                        Order or = model.getLifes().get(i);
                        datare[i][0]= or.getProductname();
                        datare[i][1]= or.getQuantity();
                        datare[i][2]= String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity()));

                    }


                    viewHolder.tableLayout.setColumnWeight(0,6);
                    viewHolder.tableLayout.setColumnWeight(1, 2);
                    viewHolder.tableLayout.setColumnWeight(2,2);

                    viewHolder.tableLayout.setDataAdapter(new SimpleTableDataAdapter(getBaseContext(),datare));



                }



                @NonNull
                @Override
                public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.activity_order_details,parent,false);
                    return new OrderDetailsViewHolder(itemView,getBaseContext());

                }
                @Override
                public Request getItem(int position) {
                    return super.getItem(getItemCount() - 1 - position);
                }


                @Override
                public void onDataChanged() {
                    loadOrders.removeAllViews();
                    super.onDataChanged();
                }


            };
            adapter.startListening();
            loadOrders.setAdapter(adapter);

        }else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");


        }
    }







    /**
    private void loadDetails() {
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









                    if (getIntent() != null) {
                order_id_value = getIntent().getStringExtra("OrderId");

            }
            progressBar.setVisibility(View.GONE);



            order_id.setText("Order ID - " + order_id_value);

            order_status.setText(convertCodeToStatus(Common.currentRequest.getStatus()));
            order_total.setText(Common.currentRequest.getTotal());
            order_time.setText(Utils.DateToTimeFormat(Common.currentRequest.getTime()));


            datare = new String[Common.currentRequest.getLifes().size()][3];
            for(int i = 0; i< Common.currentRequest.getLifes().size(); i++){
                Order or = Common.currentRequest.getLifes().get(i);
                datare[i][0]= or.getProductname();
                datare[i][1]= or.getQuantity();
                datare[i][2]= String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity()));

            }
            tableLayout.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headers));
            tableLayout.setColumnWeight(0,6);
            tableLayout.setColumnWeight(1, 2);
            tableLayout.setColumnWeight(2,2);

            tableLayout.setDataAdapter(new SimpleTableDataAdapter(this,datare));









        } else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

        }

    }**/
}