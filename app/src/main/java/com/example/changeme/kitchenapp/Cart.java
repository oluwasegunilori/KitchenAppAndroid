package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Interface.RecyclerItemTouchHelperListener;
import com.example.changeme.kitchenapp.Model.DataMessage;
import com.example.changeme.kitchenapp.Model.MyResponse;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Token;
import com.example.changeme.kitchenapp.Remote.APIService;
import com.example.changeme.kitchenapp.ViewHolder.CartAdapter;
import com.example.changeme.kitchenapp.ViewHolder.CartViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.example.changeme.kitchenapp.helper.RecyclerItemTouchHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    public  TextView totalprice;
    public RelativeLayout errorlayoutcart;
    public  TextView errormessagecart;
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        FirebaseDatabase database;
    DatabaseReference requesst;
    ImageView imbacj, imbtack;
    FButton buttonplaceorder;
    List<Order> cart = new ArrayList<>();
    APIService mservice;
    CartAdapter adapter;
    RelativeLayout relativeLayout;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage, errorcart;
    private Button retry, addNewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mservice = Common.getFCMService();
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        addNewCart = findViewById(R.id.newCart);
        errorlayoutcart = findViewById(R.id.nocartlayout);
        errorcart = findViewById(R.id.errorimagecart);
        errormessagecart = findViewById(R.id.errortitlecart);
    imbtack = findViewById(R.id.imbtack);
        if(Common.currentuser==null){
            Intent inte = new Intent(Cart.this, MainActivity.class);

            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(inte);

        }

        database     = FirebaseDatabase.getInstance();
        requesst = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        relativeLayout = findViewById(R.id.relativelayout);

        ItemTouchHelper.SimpleCallback itemtouchhelpercallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemtouchhelpercallback).attachToRecyclerView(recyclerView);






        imbacj = findViewById(R.id.imback);
        totalprice = (TextView) findViewById(R.id.total);
        buttonplaceorder = (FButton) findViewById(R.id.placeorer);
        imbacj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart.super.onBackPressed();

            }
        });
        imbtack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    Intent soff = new Intent(Cart.this, SearchActivity.class);
                    soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(soff);
                }
                else{
                    Toast.makeText(Cart.this,"Check Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.size()>0) {
                    Intent life_detail = new Intent(Cart.this, OrderingProcess.class);
                    life_detail.putExtra("Total", String.valueOf(calculate()) );
                    startActivity(life_detail);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_oute);

                } else{

                    Toast.makeText(Cart.this, "Cart is Empty", Toast.LENGTH_SHORT).show();

                }

            }
        });




        loadListFood();


    }

   /** private void showAlertDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(Cart.this, R.style.MyDialogTheme);


        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);

        alertdialog.setTitle("Payment");

        LayoutInflater inflater  = LayoutInflater.from(this);
        View order_address_comment = inflater.inflate(R.layout.order_address_comment, null);
        final MaterialEditText editAddress = (MaterialEditText) order_address_comment.findViewById(R.id.editAddress);

        final MaterialEditText editComment = (MaterialEditText) order_address_comment.findViewById(R.id.editComment);


        alertdialog.setView(order_address_comment);
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertdialog.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(editAddress.getText().length() > 2 ) {


                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        String formatted = df.format(c.getTime());
                        String timeformat = df2.format(c.getTime());
                        PrettyTime p = new PrettyTime();


                        Request request = new Request(

                                Common.currentuser.getPhone(),
                                Common.currentuser.getName(),
                                "Address: " + editAddress.getText().toString() + "\n" + "Comments:" + " " + editComment.getText().toString() ,
                                "0",
                                editComment.getText().toString(),
                                String.valueOf(calculate()),
                                formatted,
                                timeformat,


                                cart
                        );

                        String orderNumber = String.valueOf(System.currentTimeMillis());

                        requesst.child(orderNumber).setValue(request);
                        new Database(getBaseContext()).cleanCart(Common.currentuser.getPhone());

                        sendNotificationOrder(orderNumber);


                        finish();
                    }else{
                        Toast.makeText(Cart.this, "Please Enter a Valid address", Toast.LENGTH_SHORT).show();

                    }
            }else {
                    Toast.makeText(Cart.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
                }
        });
alertdialog.setNegativeButton("Stop Order", new DialogInterface.OnClickListener() {

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        dialogInterface.dismiss();
    }
});
alertdialog.show();
    }
**/
    private void sendNotificationOrder(final String orderNumber) {
            DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final Query data =tokens.orderByChild("serverToken").equalTo(true);
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
                        datasend.put("message", "You have new order" + orderNumber);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(), datasend);


                        mservice.sendNotification(dataMessage)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
           if(response.body().success == 1){



                       Toast.makeText(Cart.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{



                                            Toast.makeText(Cart.this, "Failed!!!", Toast.LENGTH_SHORT).show();

                                        }}


                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                                    }
                                });
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private BigDecimal calculate(){


            if (cart.size()<1){
                BigDecimal d =  BigDecimal.ZERO;
                d.setScale(2, RoundingMode.UNNECESSARY);
                totalprice.setText("₦ "+String.valueOf(d) + "   Cart is empty");
                showErrorCart(R.drawable.no_result,"Error");
            }
            cart = new Database(this).getCart(Common.currentuser.getPhone());
            adapter = new CartAdapter(cart,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal help = BigDecimal.ONE;
            for(Order order:cart){
                help =  BigDecimal.valueOf(Double.parseDouble(order.getPrice()));
                help = help.multiply(BigDecimal.valueOf(Double.parseDouble(order.getQuantity())));
                total = total.add(help);
                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                BigDecimal bigDecimal = BigDecimal.ZERO;

                total.setScale(2, RoundingMode.UNNECESSARY);

            }
            return total;
    }

    private void loadListFood() {
        if(Common.currentuser!=null){

        }
        else{
            Intent inte = new Intent(Cart.this, MainActivity.class);

            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(inte);
        }
        errorlayoutcart.setVisibility(View.GONE);
            cart = new Database(this).getCart(Common.currentuser.getPhone());
            adapter = new CartAdapter(cart, this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal help = BigDecimal.ONE;
            for (Order order : cart) {
                help = BigDecimal.valueOf(Double.parseDouble(order.getPrice()));
                help = help.multiply(BigDecimal.valueOf(Double.parseDouble(order.getQuantity())));
                total = total.add(help);
                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


                total.setScale(2, RoundingMode.UNNECESSARY);

            }
            totalprice.setText("₦" + " " + String.valueOf(total));

        if (cart.size()<1){
            BigDecimal d =  BigDecimal.ZERO;
            d.setScale(2, RoundingMode.UNNECESSARY);
            totalprice.setText("₦ "+String.valueOf(d));
            showErrorCart(R.drawable.no_result,"Cart is Empty");
        }
        }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());

        return true;
    }

    private void deleteCart(int position) {
    cart.remove(position);
        new Database(this).cleanCart(Common.currentuser.getPhone());
        for(Order item:cart){
            new Database(this).addToCart(item);
        }
        loadListFood();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductname();

            final Order deleteitem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());

            final int deleteindex = viewHolder.getAdapterPosition();
            adapter.removeItem(deleteindex);
            new Database(getBaseContext()).removeFromCart(deleteitem.getProductid(), Common.currentuser.getPhone());


            List<Order> orders = new Database(getBaseContext()).getCart(Common.currentuser.getPhone());
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal help;
            for (Order item : orders) {
                help = BigDecimal.valueOf(Double.parseDouble(item.getPrice()));

                help = help.multiply(BigDecimal.valueOf(Double.parseDouble(item.getQuantity())));
                total = total.add(help);

                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                total.setScale(2, RoundingMode.UNNECESSARY);

            }
            totalprice.setText("₦"+" "+ String.valueOf(total));

            Snackbar snackbar = Snackbar.make(relativeLayout, name+"removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                adapter.restoreItem(deleteitem,deleteindex);
                new Database(getBaseContext()).addToCart(deleteitem);

                    List<Order> orders = new Database(getBaseContext()).getCart(Common.currentuser.getPhone());
                    BigDecimal total = BigDecimal.ZERO;
                    BigDecimal help = BigDecimal.ONE;
                    for ( Order item : orders) {

                        help = BigDecimal.valueOf(Double.parseDouble(item.getPrice()));

                        help = help.multiply(BigDecimal.valueOf(Double.parseDouble(item.getQuantity())));
                        total = total.add(help);

                        Locale locale = new Locale("en", "US");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                        total.setScale(2, RoundingMode.UNNECESSARY);
                    }
                    totalprice.setText("₦"+" "+ String.valueOf(total));

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
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
        if(item.getItemId() == R.id.Cart) {
            if(Common.isConnectedToInternet(getBaseContext())) {

                startActivity(new Intent(Cart.this, Home.class));
            }
            else{
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }



        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
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

               loadListFood();
            }
        });
    }
    private void showErrorCart(int imageview, String message){
        if(errorlayoutcart.getVisibility()== GONE){
            errorlayoutcart.setVisibility(View.VISIBLE);
        }
        errormessagecart.setText(message);
        addNewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    Intent soff = new Intent(Cart.this, SearchActivity.class);
                    soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(soff);}
                else {
                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                }

            }
        });
    }

    @Override
    protected void onResume()
    {

        super.onResume();
    loadListFood();
    }
}