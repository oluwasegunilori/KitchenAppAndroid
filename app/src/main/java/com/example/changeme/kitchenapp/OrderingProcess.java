package com.example.changeme.kitchenapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.DataMessage;
import com.example.changeme.kitchenapp.Model.MyResponse;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Request;
import com.example.changeme.kitchenapp.Model.Token;
import com.example.changeme.kitchenapp.Remote.APIService;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.ocpsoft.prettytime.PrettyTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class OrderingProcess extends AppCompatActivity {

    String total;
    TextView txt, txtTotal;
    ImageView imageView, imageView2;
    MaterialEditText editAddress, editMessage;
    RadioButton pikup, deliver, paynow, paydeliver;
    RadioGroup group1, group2;
    Boolean pickup = false, deliverme = true, payNow = true, payOnDeliver = false;
    List<Order> cart = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference requesst;
    Button myOrders;
    APIService mservice;
    Button but;
    private RelativeLayout order_complete;
    private  TextView order_number;
    private ProgressDialog mDialog;
    private CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_process);
        pikup= findViewById(R.id.pick_up);
       editAddress = findViewById(R.id.editAddress);
       editMessage  = findViewById(R.id.editMessage);
        mservice = Common.getFCMService();
        card = findViewById(R.id.cont);
        order_complete  =findViewById(R.id.ordercompleted);
        order_number = findViewById(R.id.ordernumber);
        myOrders = findViewById(R.id.myOrders);
        paynow = findViewById(R.id.pay_on_cash);
        database     = FirebaseDatabase.getInstance();
        txtTotal = findViewById(R.id.total_due);
        group1 = findViewById(R.id.grouping);
        group2 = findViewById(R.id.grouping2);
        requesst = database.getReference("Requests");

        paydeliver = findViewById(R.id.pay_delivery);
        deliver = findViewById(R.id.pay_on);
        txt = findViewById(R.id.getLocation);
        imageView = findViewById(R.id.goback);
        imageView2 = findViewById(R.id.gohome);
        but = findViewById(R.id.continues);




        if(Common.currentuser== null){
            Intent inte = new Intent(OrderingProcess.this, MainActivity.class);

            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(inte);

        }
        else{

            cart = new Database(this).getCart(Common.currentuser.getPhone());

        }
        if(getIntent()!=null){
            total = getIntent().getStringExtra("Total");
            txtTotal.setText("Total: ₦"+total);
        }
        else{

            List<Order> orders = new Database(getBaseContext()).getCart(Common.currentuser.getPhone());
            BigDecimal totalinitial = BigDecimal.ZERO;
            BigDecimal help;
            for (Order item : orders) {
                help = BigDecimal.valueOf(Double.parseDouble(item.getPrice()));

                help = help.multiply(BigDecimal.valueOf(Double.parseDouble(item.getQuantity())));
                totalinitial = totalinitial.add(help);

                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                totalinitial.setScale(2, RoundingMode.UNNECESSARY);

            }
            txtTotal.setText("Total: ₦"+" "+ String.valueOf(totalinitial));
        }


        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.pick_up:
                        editAddress.setEnabled(false);
                        pickup = true;
                        deliverme = false;
                        break;
                    case R.id.pay_on:
                        editAddress.setEnabled(true);
                        deliverme = true;
                        pickup = false;
                }
            }
        });


        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.pay_delivery:
                        payNow = false;
                        payOnDeliver = true;
                        break;
                    case R.id.pay_on_cash:
                        payNow = true;
                        payOnDeliver = false;
                        break;
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(OrderingProcess.this, Cart.class);
                goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goback);
            }
        });


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goback = new Intent(OrderingProcess.this, Home.class);
                goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goback);

            }
        });

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deliverme && payOnDeliver ){
                    showAlertDialog();

                }
                else if(pickup && payOnDeliver){
                  showPickDialog();







                }else if(payNow && deliverme){
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String formatted = df.format(c.getTime());
                    String timeformat = df2.format(c.getTime());
                    if(editMessage.getText().length()<1){
                        editMessage.setText("No message");
                    }

                    Intent intent = new Intent(OrderingProcess.this, PayBill.class);
                    intent.putExtra("order","Delivery");
                    intent.putExtra("address", editAddress.getText().toString());
                    intent.putExtra("date", formatted);
                    intent.putExtra("time", timeformat);
                    intent.putExtra("total",total);
                    intent.putExtra("message",editMessage.getText().toString());
                    startActivity(intent);

                }else if(payNow && pickup){
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String formatted = df.format(c.getTime());
                    String timeformat = df2.format(c.getTime());
                    if(editMessage.getText().length()<1){
                        editMessage.setText("No message");
                    }
                    Intent intent = new Intent(OrderingProcess.this, PayBill.class);
                    intent.putExtra("order","Pick Up");
                    intent.putExtra("address", "Pick Up");
                    intent.putExtra("date", formatted);
                    intent.putExtra("time", timeformat);
                    intent.putExtra("message",editMessage.getText().toString());

                    startActivity(intent);


                }


            }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });

    }
    private void sendNotificationOrder(final String orderNumber) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data =tokens.orderByChild("serverToken").equalTo(true);

        mDialog = new ProgressDialog(OrderingProcess.this);
        mDialog.setMessage("Please Wait......");
        mDialog.setCancelable(false);

        mDialog.show();

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Token serverToken = null;
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren() ) {


                    serverToken = postSnapshot.getValue(Token.class);
                }




                //      Notification notification = new Notification("Shegz Kitchen","You have new order"+"\n"+ orderNumber);
                //    Sender content = new Sender(serverToken.getToken(),notification);
                Map<String,String> datasend = new HashMap<>();
                datasend.put("title","Shegz Kitchen");
                datasend.put("message", "You have new order " + orderNumber);
                DataMessage dataMessage = new DataMessage(serverToken.getToken(), datasend);


                mservice.sendNotification(dataMessage)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.body().success == 1){



                                       mDialog.dismiss();

                                }else{


                                mDialog.dismiss();
                                    Toast.makeText(OrderingProcess.this, "Bad Connection, Check your Orders to confirm if it was sent!!", Toast.LENGTH_LONG).show();

                                }}


                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                              mDialog.dismiss();
                                Toast.makeText(OrderingProcess.this, "Failed!!!", Toast.LENGTH_LONG).show();

                                Log.d("Error", t.getMessage());
                            }
                        });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void showPickDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(OrderingProcess.this, R.style.MyDialogTheme);


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);

        alertdialog.setTitle("Confirmation");
        alertdialog.setMessage("Are you sure you want to place this order? ");


        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(editMessage.getText().length()<1){
                        editMessage.setText("No message");
                    }

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    String formatted = df.format(c.getTime());
                    String timeformat = df2.format(c.getTime());
                    PrettyTime p = new PrettyTime();


                    Request request = new Request(

                            Common.currentuser.getPhone(),
                            Common.currentuser.getName(),
                            "Pick Up",
                            "0",
                            "Pay on Delivery",
                            editMessage.getText().toString(),
                            "Pick Up",
                            Common.currentuser.getPhone()+"_0",
                            total,
                            formatted,
                            timeformat,


                            cart
                    );

                    String orderNumber = String.valueOf(System.currentTimeMillis());

                    requesst.child(orderNumber).setValue(request);
                    new Database(getBaseContext()).cleanCart(Common.currentuser.getPhone());

                    sendNotificationOrder(orderNumber);
                imageView.setVisibility(View.GONE);
                card.setVisibility(GONE);
                    if(order_complete.getVisibility()== GONE){
                        order_complete.setVisibility(View.VISIBLE);
                    }

                order_number.setText(orderNumber);

                myOrders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent soff = new Intent(OrderingProcess.this, Orders.class);
                        soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(soff);

                    }
                });

                }else {
                    Toast.makeText(OrderingProcess.this, "Check Internet Connection", Toast.LENGTH_LONG).show();

                }




            }

        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }


    private void showAlertDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(OrderingProcess.this, R.style.MyDialogTheme);


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);

        alertdialog.setTitle("Confirmation");
        alertdialog.setMessage("Are you sure you want to place this order? ");


        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(editAddress.getText().length() > 2 ) {
                        if(editMessage.getText().length()<1){
                            editMessage.setText("No message");
                        }

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        String formatted = df.format(c.getTime());
                        String timeformat = df2.format(c.getTime());
                        PrettyTime p = new PrettyTime();


                        Request request = new Request(

                                Common.currentuser.getPhone(),
                                Common.currentuser.getName(),
                                "Address: " + editAddress.getText().toString() ,
                                "0",
                                "Pay on Delivery",
                                editMessage.getText().toString(),
                                "Delivery",
                                Common.currentuser.getPhone()+"_0",
                                total,
                                formatted,
                                timeformat,


                                cart
                        );

                        String orderNumber = String.valueOf(System.currentTimeMillis());

                        requesst.child(orderNumber).setValue(request);
                        new Database(getBaseContext()).cleanCart(Common.currentuser.getPhone());

                        sendNotificationOrder(orderNumber);
                        card.setVisibility(GONE);
                        but.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        order_complete.setVisibility(View.VISIBLE);
                        order_number.setText(orderNumber);

                        myOrders.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent soff = new Intent(OrderingProcess.this, Orders.class);
                                soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(soff);

                            }
                        });




                    }else{
                        Toast.makeText(OrderingProcess.this, "Please Enter a Valid address OR click get location to get your current location", Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(OrderingProcess.this, "Check Internet Connection", Toast.LENGTH_LONG).show();

                }






            }

        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        alertdialog.show();
    }




}
